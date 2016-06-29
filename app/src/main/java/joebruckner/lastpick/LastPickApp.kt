package joebruckner.lastpick

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.squareup.otto.Bus
import joebruckner.lastpick.network.*


class LastPickApp : Application() {
    val bus = Bus(com.squareup.otto.ThreadEnforcer.ANY)
    val jsonFileManager = JsonFileManager(this)
    val bookmarkManager = BookmarkManager(jsonFileManager)
    val historyManager = HistoryManager(bus, 15)
    lateinit var glide: RequestManager
    lateinit var serviceManager: ServiceManager
    lateinit var movieManager: MovieManager

    override fun onCreate() {
        super.onCreate()
        glide = Glide.with(this)
        serviceManager = ServiceManager(
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
        movieManager = MovieManager(serviceManager)
        bookmarkManager.loadBookmarksFromFile()
    }

    override fun getSystemService(name: String): Any? {
        return when (name) {
            BUS -> bus
            GLIDE -> glide
            BOOKMARKS_MANAGER -> bookmarkManager
            MOVIE_MANAGER -> movieManager
            else -> super.getSystemService(name)
        }
    }

    companion object {
        const val BUS = "BUS"
        const val GLIDE = "GLIDE"
        const val BOOKMARKS_MANAGER = "BOOKMARKS_MANAGER"
        const val MOVIE_MANAGER = "MOVIE_MANAGER"
    }
}
