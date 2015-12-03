package joebruckner.lastpick

import android.app.Application
import android.util.Log
import com.squareup.otto.Bus
import joebruckner.lastpick.network.HistoryManager
import joebruckner.lastpick.network.MovieManager
import joebruckner.lastpick.network.ServiceManager

class LastPickApp : Application() {
    val bus = Bus()
    val service = ServiceManager(this, bus)
    val history = HistoryManager(bus, 15)
    val movies = MovieManager(bus, 50)
    val APP_NAME = "Last Pick"

    override fun onCreate() {
        super.onCreate()
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
