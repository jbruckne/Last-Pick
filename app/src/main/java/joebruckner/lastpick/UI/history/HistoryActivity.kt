package joebruckner.lastpick.ui.history

import android.os.Bundle
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.consume
import joebruckner.lastpick.replaceFrame
import joebruckner.lastpick.sethomeAsUpEnabled
import joebruckner.lastpick.ui.common.BaseActivity

class HistoryActivity : BaseActivity() {
    override val layoutId = R.layout.activity_history

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFrame(R.id.frame, HistoryFragment())
        sethomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> consume { onBackPressed() }
        else -> super.onOptionsItemSelected(item)
    }
}
