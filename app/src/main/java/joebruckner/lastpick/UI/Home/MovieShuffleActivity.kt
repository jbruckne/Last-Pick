package joebruckner.lastpick.ui.home


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.ui.bookmarks.BookmarksActivity
import joebruckner.lastpick.ui.common.BaseActivity
import joebruckner.lastpick.ui.history.HistoryActivity

class MovieShuffleActivity : BaseActivity() {
    override val layoutId = R.layout.activity_movie_shuffle
    override val menuId = R.menu.menu_shuffle_movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Home"

        replaceFrame(R.id.frame, MovieFragment(), false)
    }

    override fun onStart() {
        super.onStart()
        sendAction(Action.UPDATE)
        fab?.setOnClickListener({ sendAction(Action.UPDATE) });
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                startActivity(Intent(this, javaClass<HistoryActivity>()))
                return true
            }
            R.id.action_view_bookmarks -> {
                startActivity(Intent(this, javaClass<BookmarksActivity>()))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}