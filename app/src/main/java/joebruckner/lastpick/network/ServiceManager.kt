package joebruckner.lastpick.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.enqueue
import joebruckner.lastpick.events.*
import retrofit.GsonConverterFactory
import retrofit.Retrofit

class ServiceManager(val app: LastPickApp, val bus: Bus) {
    val service = Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbService::class.java)

    init {
        bus.register(this)
    }

    @Subscribe fun getMovieRequest(request: MovieRequest) {
        if (!isConnected()) {
            bus.post(RequestError("Not connected to internet", 408))
            return
        }
        service.getMovie(request.id).enqueue { response, retrofit ->
            if (response.isSuccess && response.code() == 200)
                bus.post(MovieResponse(response.body()))
            else
                bus.post(RequestError("Unable to fetch movie ${request.id}", response.code()))
        }
    }

    @Subscribe fun getPageRequest(request: PageRequest) {
        if (!isConnected()) {
            bus.post(RequestError("Not connected to internet", 408))
            return
        }
        service.getTopRatedMovies(request.page).enqueue { response, retrofit ->
            if (response.isSuccess && response.code() == 200)
                bus.post(PageResponse(response.body()))
            else
                bus.post(RequestError("Unable to fetch page ${request.page}", response.code()))
        }
    }

    private fun isConnected(): Boolean {
        val networkInfo = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return networkInfo.activeNetworkInfo != null
    }
}