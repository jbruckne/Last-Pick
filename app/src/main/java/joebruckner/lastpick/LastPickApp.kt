package joebruckner.lastpick

import android.app.Application
import android.util.Log
import com.squareup.otto.Bus
import joebruckner.lastpick.network.MovieManager

class LastPickApp : Application() {
    val bus = Bus()
    val movieManager = MovieManager(bus, "en", 40)
    val APP_NAME = "Last Pick"

    override fun onCreate() {
        super.onCreate()
        Log.d(APP_NAME, "App launched :)")
    }
}
