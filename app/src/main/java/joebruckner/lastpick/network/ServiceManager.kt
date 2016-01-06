package joebruckner.lastpick.network

import android.content.Context
import android.net.ConnectivityManager
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.data.*
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import java.net.SocketTimeoutException

class ServiceManager(val app: LastPickApp, val bus: Bus) {
    val service = Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbService::class.java)

    init {
        bus.register(this)
    }

    fun fetchMovie(id: Int): Movie? {
        if (!isConnected()) return null
        try {
            val response = service.getMovie(id).execute()
            return if (response.isSuccess && response.code() == 200) response.body()
            else null
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            return null
        }
    }

    fun fetchPage(page: Int, genres: List<Genre>?): Page? {
        if (!isConnected()) return null
        val genreIds = genres?.map { genre -> genre.id }
        val formattedIds = StringBuilder()
        genreIds?.forEach { id -> formattedIds.append("""%7C$id""") }
        val response = service.discoverMovies(page,
                if (genres == null) null
                else formattedIds.substring(3)
        ).execute()
        return if (response.isSuccess && response.code() == 200) response.body()
               else null
    }

    private fun isConnected(): Boolean {
        val networkInfo = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return networkInfo.activeNetworkInfo != null
    }
}