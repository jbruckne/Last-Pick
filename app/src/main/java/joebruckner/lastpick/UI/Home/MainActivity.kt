package joebruckner.lastpick.ui.home


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.ui.common.BaseActivity
import joebruckner.lastpick.ui.history.HistoryActivity

class MainActivity : BaseActivity() {
    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movie = intent.extras?.getString("movie")
        val fragment = MovieFragment(movie)

        replaceFrame(R.id.frame, fragment, false)

        fab?.setOnClickListener({ sendAction(Action.SHUFFLE) });
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                showHistory()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showHistory() {
        startActivity(Intent(this, javaClass<HistoryActivity>()))
    }
}