package joebruckner.lastpick.ui.history

import android.os.Bundle
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.ui.common.BaseActivity

class HistoryActivity : BaseActivity() {
    override val layoutId = R.layout.activity_history

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "History"

        replaceFrame(R.id.frame, HistoryFragment())

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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
