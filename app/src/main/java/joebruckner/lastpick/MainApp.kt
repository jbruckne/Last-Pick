package joebruckner.lastpick

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import jonathanfinerty.once.Once


class MainApp : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Once.initialise(this)
        component.inject(this)
    }

    companion object {
        init {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        const val TMDB_API_KEY = "9856c489f3e6e5b5b972c6373c440210"
        const val GUIDEBOX_API_KEY = "rKgZSrQUxpqjtgi2F5NBFNQpYZ5tNrGh"
    }
}
