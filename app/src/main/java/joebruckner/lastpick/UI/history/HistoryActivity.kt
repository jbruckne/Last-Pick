package joebruckner.lastpick.ui.history

import android.os.Bundle
import android.util.Log

import joebruckner.lastpick.R
import joebruckner.lastpick.ui.common.BaseActivity
import kotlinx.android.synthetic.activity_history.*

class HistoryActivity : BaseActivity() {
    override val layoutId = R.layout.activity_history
    override val menuId = R.menu.menu_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Test", "Created!!!!")
        replaceFrame(R.id.frame, HistoryFragment())
        toolbar.title = "History"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
