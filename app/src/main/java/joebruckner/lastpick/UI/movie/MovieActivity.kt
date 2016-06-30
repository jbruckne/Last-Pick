package joebruckner.lastpick.ui.movie

import android.os.Bundle
import android.view.MenuItem
import com.google.gson.Gson
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.ui.common.BaseActivity

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
        val movie = Gson().fromJson(movieString, Movie::class.java)
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
