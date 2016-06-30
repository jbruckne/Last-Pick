package joebruckner.lastpick

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.bumptech.glide.Glide
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
        glide = Glide.with(this)
        serviceManager = ServiceManager(
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
        movieManager = MovieManager(serviceManager, historyManager)
        bookmarkManager.loadBookmarksFromFile()
    }

    override fun getSystemService(name: String): Any? {
        return when (name) {
            GLIDE -> glide
            BOOKMARKS_MANAGER -> bookmarkManager
            HISTORY_MANAGER -> historyManager
            MOVIE_MANAGER -> movieManager
            else -> super.getSystemService(name)
        }
    }

    companion object {
        const val GLIDE = "GLIDE"
        const val BOOKMARKS_MANAGER = "BOOKMARKS_MANAGER"
        const val HISTORY_MANAGER = "HISTORY_MANAGER"
        const val MOVIE_MANAGER = "MOVIE_MANAGER"
    }
}
