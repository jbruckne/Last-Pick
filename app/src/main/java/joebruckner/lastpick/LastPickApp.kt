package joebruckner.lastpick

import android.app.Activity
import android.app.Application
import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer
import joebruckner.lastpick.network.*

class LastPickApp : Application() {
    val bus = Bus(com.squareup.otto.ThreadEnforcer.ANY)
    val service = ServiceManager(this, bus)
    val jsonFileManager = JsonFileManager(this)
    val bookmarkManager = BookmarkManager(bus, jsonFileManager)
    val historyManager = HistoryManager(bus, 15)
    val movieManager = MovieManager(bus, service)
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
