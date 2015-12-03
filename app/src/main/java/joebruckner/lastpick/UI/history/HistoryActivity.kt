package joebruckner.lastpick.ui.history

import android.os.Bundle
import android.util.Log
import android.view.MenuItem

import joebruckner.lastpick.R
import joebruckner.lastpick.ui.common.BaseActivity
import kotlinx.android.synthetic.activity_history.*

class HistoryActivity : BaseActivity() {
    override val layoutId = R.layout.activity_history

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFrame(R.id.frame, HistoryFragment())
        toolbar.title = "History"
        setSupportActionBar(toolbar)
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
