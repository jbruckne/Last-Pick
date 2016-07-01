package joebruckner.lastpick

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.bumptech.glide.RequestManager
import joebruckner.lastpick.network.*


class LastPickApp : Application() {
    val jsonFileManager = JsonFileManager(this)
    val bookmarkManager = BookmarkManager(jsonFileManager)
    val historyManager = HistoryManager()
    lateinit var glide: RequestManager
    lateinit var serviceManager: ServiceManager
    lateinit var movieManager: MovieManager

    override fun onCreate() {
        super.onCreate()
        serviceManager = ServiceManager(
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
        movieManager = MovieManager(serviceManager, historyManager)
        bookmarkManager.loadBookmarksFromFile()
    }

    override fun getSystemService(name: String): Any? {
        return when (name) {
            BOOKMARKS_MANAGER -> bookmarkManager
            HISTORY_MANAGER -> historyManager
            MOVIE_MANAGER -> movieManager
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
