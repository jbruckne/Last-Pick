package joebruckner.lastpick.ui.movie

import android.os.Bundle
import android.view.MenuItem
import com.google.gson.Gson
import joebruckner.lastpick.R
import joebruckner.lastpick.consume
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.replaceFrame
import joebruckner.lastpick.sethomeAsUpEnabled
import joebruckner.lastpick.ui.common.BaseActivity

class MovieActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFrame(R.id.frame, MovieFragment())
        sethomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()

        val movieString = intent.extras?.getString("movie")
        val movie = Gson().fromJson(movieString, Movie::class.java)
        val fragment = supportFragmentManager.findFragmentById(R.id.frame)
        if (fragment is MovieFragment) fragment.presenter.setMovie(movie)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            android.R.id.home -> consume { onBackPressed() }
            else -> super.onOptionsItemSelected(item)
    }
}
