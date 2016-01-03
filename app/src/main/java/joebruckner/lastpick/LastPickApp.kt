package joebruckner.lastpick

import android.app.Application
import android.util.Log
import com.squareup.otto.Bus
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.network.*

class LastPickApp : Application() {
    val bus = Bus()
    val service = ServiceManager(this, bus)
    val jsonFileManager = JsonFileManager(this)
    val bookmarkManager = BookmarkManager(bus, jsonFileManager)
    val historyManager = HistoryManager(bus, 15)
    val movieManager = MovieManager(bus, 50)
    val APP_NAME = "Last Pick"

    override fun onCreate() {
        super.onCreate()
        bookmarkManager.loadSavedBookmarks()
    }

    override fun getSystemService(name: String): Any? {
        when (name) {
            BUS -> return bus
            else -> return super.getSystemService(name)
        }
    }

    companion object {
        const val BUS = "BUS"
    }
}
