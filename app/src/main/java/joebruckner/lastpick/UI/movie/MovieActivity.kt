package joebruckner.lastpick.ui.movie

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.replaceFrame
import joebruckner.lastpick.setHomeAsUpEnabled
import joebruckner.lastpick.ui.common.BaseActivity
import org.json.JSONObject

class MovieActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Intent.ACTION_VIEW == intent.action) {
            val id = intent.data.lastPathSegment.substringBefore('-').toInt()
            replaceFrame(R.id.frame, MovieFragment.newInstance(id))
        } else {
            val id = JSONObject(intent.getStringExtra("movie")).getInt("id")
            replaceFrame(R.id.frame, MovieFragment.newInstance(id))
        }

        setHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            //android.R.id.home -> consume { onBackPressed() }
            else -> super.onOptionsItemSelected(item)
    }
}
