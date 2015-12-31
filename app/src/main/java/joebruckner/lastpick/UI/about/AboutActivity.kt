package joebruckner.lastpick.ui.about

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import joebruckner.lastpick.R
import kotlinx.android.synthetic.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.about);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val method = LinkMovementMethod.getInstance()
        glide.movementMethod = method
        gson.movementMethod = method
        okhttp.movementMethod = method
        retrofit.movementMethod = method
        picasso.movementMethod = method
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
