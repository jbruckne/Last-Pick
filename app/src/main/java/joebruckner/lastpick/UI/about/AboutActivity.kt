package joebruckner.lastpick.ui.about

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.widget.TextView
import joebruckner.lastpick.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.action_about);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val glide = findViewById(R.id.glide) as TextView
        val gson = findViewById(R.id.gson) as TextView
        val okhttp = findViewById(R.id.okhttp) as TextView
        val retrofit = findViewById(R.id.retrofit) as TextView

        val method = LinkMovementMethod.getInstance()
        glide.movementMethod = method
        gson.movementMethod = method
        okhttp.movementMethod = method
        retrofit.movementMethod = method
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
