package joebruckner.lastpick.ui.bookmarks

import android.os.Bundle
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.consume
import joebruckner.lastpick.replaceFrame
import joebruckner.lastpick.sethomeAsUpEnabled
import joebruckner.lastpick.ui.common.BaseActivity

class BookmarksActivity : BaseActivity() {
    override val layoutId = R.layout.activity_bookmarks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFrame(R.id.frame, BookmarksFragment())
        sethomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> consume { onBackPressed() }
        else -> super.onOptionsItemSelected(item)
    }
}
