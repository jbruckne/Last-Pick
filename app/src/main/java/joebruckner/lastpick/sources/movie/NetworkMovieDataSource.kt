package joebruckner.lastpick.sources.movie

import android.net.ConnectivityManager
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.data.*
import joebruckner.lastpick.sources.TmdbService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import java.util.concurrent.TimeUnit

class NetworkMovieDataSource(val connectivityManager: ConnectivityManager): MovieDataSource {
    val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()
    val service: TmdbService = Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(TmdbService::class.java)

    companion object {
        val CONNECTION_ERROR = "Failed to connect to the internet"
    }

    override fun getMovie(id: Int): Observable<Movie> {
        if (!isConnected()) return Observable.error<Movie>(Throwable(CONNECTION_ERROR))
        return service.getMovie(id, LastPickApp.TMDB_API_KEY)
    }

    override fun getPage(page: Int, filter: Filter): Observable<Page> {
        if (!isConnected()) return Observable.error<Page>(Throwable(CONNECTION_ERROR))
        return service.discoverMovies(
                LastPickApp.TMDB_API_KEY,
                page,
                if (filter.showAll || filter.allGenresSelected()) null
                else formatGenreIds(filter.genres),
                filter.yearLte,
                filter.yearGte
        )
    }

    override fun getSpecialList(type: ListType): Observable<Page> {
        if (!isConnected()) return Observable.error(Throwable(CONNECTION_ERROR))
        return when (type) {
            ListType.POPULAR -> service.getPopular(LastPickApp.TMDB_API_KEY)
            ListType.UPCOMING -> service.getUpcoming(LastPickApp.TMDB_API_KEY)
            ListType.TOP_RATED -> service.getTopRated(LastPickApp.TMDB_API_KEY)
            ListType.NOW_PLAYING -> service.getNowPlaying(LastPickApp.TMDB_API_KEY)
        }
    }

    override fun saveMovieEntry(movie: Movie) {
        throw UnsupportedOperationException("Cannot update movie on the network.")
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