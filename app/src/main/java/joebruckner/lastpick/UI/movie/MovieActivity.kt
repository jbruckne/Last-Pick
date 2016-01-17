package joebruckner.lastpick.ui.movie

import android.os.Bundle
import android.view.MenuItem
import com.google.gson.Gson
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.MovieEvent
import joebruckner.lastpick.data.ErrorEvent
import joebruckner.lastpick.ui.common.BaseActivity
import joebruckner.lastpick.ui.movie.MovieFragment

class MovieActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFrame(R.id.frame, MovieFragment())

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()

        val movieString = intent.extras?.getString("movie")
        val movie = Gson().fromJson(movieString, javaClass<Movie>())
        val bus = application.getSystemService(LastPickApp.BUS) as Bus
        if (movie != null) bus.post(MovieEvent(movie))
        else bus.post(ErrorEvent("Couldn't find movie", 0))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
