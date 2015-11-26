package joebruckner.lastpick.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.enqueue
import joebruckner.lastpick.events.Request
import joebruckner.lastpick.events.RequestError
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import java.util.*

class MovieManager(val bus: Bus, val language: String, val scope: Int, val app: LastPickApp) {
    val service = Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbService::class.java)
    val idStack = Stack<Int>()

    init {
        bus.register(this)
    }

    @Subscribe fun requestAvailable(request: Request) {
        if (idStack.isEmpty())
            getNewIds(getRandomPage(1..scope))
        else getNewMovie(idStack.pop())
    }

    private fun getNewIds(page: Int) {
        if (isConnected()) {
            bus.post(RequestError("Not connected to internet", 408))
            return
        }
        service.getTopRatedMovies(page).enqueue { response, retrofit ->
            if (response.isSuccess && response.code() == 200) {
                response.body().getIds().forEach { idStack.push(it) }
                getNewMovie(idStack.pop())
            } else {
                bus.post(RequestError("Unable to fetch page of top rated", response.code()))
            }
        }
    }

    private fun getNewMovie(id: Int) {
        if (isConnected()) {
            bus.post(RequestError("Not connected to internet", 408))
            return
        }
        service.getMovie(id).enqueue { response, retrofit ->
            if (response.isSuccess && response.code() == 200) {
                Log.d("Manager", "New Movie Fetched")
                bus.post(response.body())
            } else {
                Log.e("ERROR", response.message())
                bus.post(RequestError("Unable to fetch movie with id $id", response.code()))
            }
        }
    }

    private fun getRandomPage(range: Range<Int>): Int {
        val random = Random(System.currentTimeMillis())
        return random.nextInt(range.end - range.start) + range.start
    }

    private fun isConnected(): Boolean {
        val networkInfo = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return networkInfo.activeNetworkInfo == null
    }
}
