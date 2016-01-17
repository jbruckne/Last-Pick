package joebruckner.lastpick.ui.bookmarks

import android.os.Bundle
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.ui.common.BaseActivity
import joebruckner.lastpick.ui.movie.MovieFragment

class BookmarksActivity : BaseActivity() {
    override val layoutId = R.layout.activity_bookmarks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFrame(R.id.frame, BookmarksFragment())

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.isTitleEnabled = false
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
