package joebruckner.lastpick

import android.app.Activity
import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer
import joebruckner.lastpick.network.*

class LastPickApp : Application() {
    lateinit var glide: RequestManager
    val bus = Bus(com.squareup.otto.ThreadEnforcer.ANY)
    val service = ServiceManager(this, bus)
    val jsonFileManager = JsonFileManager(this)
    val bookmarkManager = BookmarkManager(bus, jsonFileManager)
    val historyManager = HistoryManager(bus, 15)
    val movieManager = MovieManager(bus, service)
    val APP_NAME = "Last Pick"

    override fun onCreate() {
        super.onCreate()
        glide = Glide.with(this)
        bookmarkManager.loadSavedBookmarks()
    }

    override fun getSystemService(name: String): Any? {
        return when (name) {
            BUS -> bus
            GLIDE -> glide
            else -> super.getSystemService(name)
        }
    }

    companion object {
        const val BUS = "BUS"
        const val GLIDE = "GLIDE"
    }
}
