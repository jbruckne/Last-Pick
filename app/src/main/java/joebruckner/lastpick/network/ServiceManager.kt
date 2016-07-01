package joebruckner.lastpick.network

import android.net.ConnectivityManager
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.data.Filter
import joebruckner.lastpick.data.Genre
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Page
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import java.util.concurrent.TimeUnit

class ServiceManager(val connectivityManager: ConnectivityManager) {
    val CONTENT_ERROR = "Failed to retrieve content"
    val CONNECTION_ERROR = "Failed to connect to the internet"
    val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()
    val service: TmdbService = Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(TmdbService::class.java)

    fun getMovie(id: Int): Observable<Movie> {
        return Observable.create { subscriber ->
            if (!isConnected()) subscriber.onError(Throwable(CONNECTION_ERROR))
            val response = service.getMovie(id, LastPickApp.TMDB_API_KEY).execute()
            val body = response.body()
            val success = response.isSuccessful && response.code() == 200
            if (success) subscriber.onNext(body)
            else subscriber.onError(Throwable(CONTENT_ERROR))
            subscriber.onCompleted()
        }

    }

    fun getPage(page: Int, filter: Filter): Observable<Page> {
        return Observable.create { subscriber ->
            if (!isConnected()) subscriber.onError(Throwable(CONNECTION_ERROR))
            val response = service.discoverMovies(
                    LastPickApp.TMDB_API_KEY,
                    page,
                    if (filter.allGenresSelected()) null
                    else formatGenreIds(filter.genres),
                    filter.yearLte,
                    filter.yearGte
            ).execute()
            val body = response.body()
            val success = response.isSuccessful && response.code() == 200
            if (success) subscriber.onNext(body)
            else subscriber.onError(Throwable(CONTENT_ERROR))
            subscriber.onCompleted()
        }

    }

    fun formatGenreIds(genres: List<Genre>): String {
        if (genres.isEmpty()) return ""
        val genreIds = genres.map { it.id }
        val formattedIds = StringBuilder()
        genreIds.forEach { id -> formattedIds.append("""%7C$id""") }
        return formattedIds.substring(3)
    }

    private fun isConnected(): Boolean = connectivityManager.activeNetworkInfo != null
}