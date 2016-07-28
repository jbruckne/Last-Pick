package joebruckner.lastpick

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import joebruckner.lastpick.interactors.BookmarkInteractor
import joebruckner.lastpick.interactors.HistoryInteractor
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.interactors.impl.BookmarkInteractorImpl
import joebruckner.lastpick.interactors.impl.HistoryInteractorImpl
import joebruckner.lastpick.interactors.impl.MovieInteractorImpl
import joebruckner.lastpick.sources.DatabaseHelper
import joebruckner.lastpick.sources.JsonFileManager
import joebruckner.lastpick.sources.bookmark.LocalBookmarkDataSource
import joebruckner.lastpick.sources.history.LocalHistoryDataSource
import joebruckner.lastpick.sources.movie.LocalMovieDataSource
import joebruckner.lastpick.sources.movie.NetworkMovieDataSource
import jonathanfinerty.once.Once


class LastPickApp : Application() {
    val jsonFileManager = JsonFileManager(this)
    lateinit var networkMovieSource: NetworkMovieDataSource
    lateinit var localMovieSource: LocalMovieDataSource
    lateinit var movieInteractor: MovieInteractor
    lateinit var localHistorySource: LocalHistoryDataSource
    lateinit var historyInteractor: HistoryInteractor
    lateinit var localBookmarkSource: LocalBookmarkDataSource
    lateinit var bookmarkInteractor: BookmarkInteractor

    override fun onCreate() {
        super.onCreate()
        networkMovieSource = NetworkMovieDataSource(
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
        localMovieSource = LocalMovieDataSource(DatabaseHelper(this))
        localHistorySource = LocalHistoryDataSource(DatabaseHelper(this))
        localBookmarkSource = LocalBookmarkDataSource(DatabaseHelper(this))
        movieInteractor = MovieInteractorImpl(networkMovieSource, localMovieSource)
        historyInteractor = HistoryInteractorImpl(localHistorySource)
        bookmarkInteractor = BookmarkInteractorImpl(jsonFileManager, localBookmarkSource)
        Once.initialise(this)
    }

    override fun getSystemService(name: String): Any? {
        return when (name) {
            BOOKMARKS_MANAGER -> bookmarkInteractor
            HISTORY_MANAGER -> historyInteractor
            MOVIE_MANAGER -> movieInteractor
            else -> super.getSystemService(name)
        }
    }

    companion object {
        const val BOOKMARKS_MANAGER = "BOOKMARKS_MANAGER"
        const val HISTORY_MANAGER = "HISTORY_MANAGER"
        const val MOVIE_MANAGER = "MOVIE_MANAGER"
        const val TMDB_API_KEY = "9856c489f3e6e5b5b972c6373c440210"
    }
}
