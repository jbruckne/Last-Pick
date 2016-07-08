package joebruckner.lastpick.ui.movie

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.consume
import joebruckner.lastpick.replaceFrame
import joebruckner.lastpick.setHomeAsUpEnabled
import joebruckner.lastpick.ui.common.BaseActivity
import org.json.JSONObject

class MovieActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id: Int
        if (Intent.ACTION_VIEW == intent.action)
            id = intent.data.lastPathSegment.substringBefore('-').toInt()
        else
            id = JSONObject(intent.getStringExtra("movie")).getInt("id")

        replaceFrame(R.id.frame, MovieFragment.newInstance(savedInstanceState != null, id))
        setHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            android.R.id.home -> consume { onBackPressed() }
            else -> super.onOptionsItemSelected(item)
    }
}
