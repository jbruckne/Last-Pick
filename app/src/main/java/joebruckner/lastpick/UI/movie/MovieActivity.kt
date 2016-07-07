package joebruckner.lastpick.ui.movie

import android.os.Bundle
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.replaceFrame
import joebruckner.lastpick.setHomeAsUpEnabled
import joebruckner.lastpick.ui.common.BaseActivity

class MovieActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieJson = intent.extras?.getString("movie")
        replaceFrame(R.id.frame, MovieFragment.newInstance(movieJson))
        setHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            //android.R.id.home -> consume { onBackPressed() }
            else -> super.onOptionsItemSelected(item)
    }
}
