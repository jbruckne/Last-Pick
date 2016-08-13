package joebruckner.lastpick.source.movie

import android.net.ConnectivityManager
import android.util.Log
import joebruckner.lastpick.MainApp
import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.Showcase
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.tmdb.Genre
import joebruckner.lastpick.model.tmdb.Page
import joebruckner.lastpick.source.GuideboxService
import joebruckner.lastpick.source.TmdbService
import rx.Observable
import javax.inject.Inject

class NetworkMovieDataSource @Inject constructor(
        val connectivityManager: ConnectivityManager,
        val themoviedb: TmdbService,
        val guidebox: GuideboxService
): MovieDataSource {

    companion object {
        val CONNECTION_ERROR = "Failed to connect to the internet"
    }

    override fun getMovie(id: Int): Observable<Movie> {
        if (!isConnected()) return Observable.error<Movie>(Throwable(CONNECTION_ERROR))
        val tmdbMovie = themoviedb.getMovie(id, MainApp.TMDB_API_KEY)
        Log.d("Movie", id.toString())
        val gbMovie = guidebox
                .findMovieWithId(id)
                .flatMap { guidebox.getMovie(it.id) }
        return Observable
                .zip(tmdbMovie, gbMovie) { i, s -> Movie.createMovie(i, s) }
                .onErrorResumeNext { tmdbMovie.map { Movie.createMovie(it, null) } }
    }

    override fun getPage(page: Int, filter: Filter): Observable<Page> {
        if (!isConnected()) return Observable.error<Page>(Throwable(CONNECTION_ERROR))
        return themoviedb.discoverMovies(
                MainApp.TMDB_API_KEY,
                page,
                if (filter.showAll || filter.allGenresSelected()) null
                else formatGenreIds(filter.genres),
                filter.yearLte,
                filter.yearGte
        )
    }

    override fun getSpecialList(type: Showcase): Observable<Page> {
        if (!isConnected()) return Observable.error(Throwable(CONNECTION_ERROR))
        return when (type) {
            Showcase.POPULAR -> themoviedb.getPopular(MainApp.TMDB_API_KEY)
            Showcase.UPCOMING -> themoviedb.getUpcoming(MainApp.TMDB_API_KEY)
            Showcase.TOP_RATED -> themoviedb.getTopRated(MainApp.TMDB_API_KEY)
            Showcase.NOW_PLAYING -> themoviedb.getNowPlaying(MainApp.TMDB_API_KEY)
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