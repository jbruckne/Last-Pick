package joebruckner.lastpick.ui.movie

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import joebruckner.lastpick.*
import joebruckner.lastpick.ui.common.BaseActivity
import org.json.JSONObject
import java.util.*

class MovieActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_movie

    private var inDiscoveryMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id: Int?
        if (Intent.ACTION_VIEW == intent.action)
            id = intent.data.lastPathSegment.substringBefore('-').toInt()
        else if (!intent.getStringExtra("movie").isNullOrBlank())
            id = JSONObject(intent.getStringExtra("movie")).getInt("id")
        else {
            id = null
            inDiscoveryMode = true
        }

        replaceFrame(R.id.frame, MovieFragment.newInstance(savedInstanceState != null, id))
        setHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        if (!inDiscoveryMode) {
            disableFab()
            return
        }
        fab?.setOnClickListener {
            (getFragment(R.id.frame) as MovieFragment).callForUpdate()
            fab?.setImageResource(when (Random().nextInt(6) + 1) {
                1 -> R.drawable.ic_dice_one_48dp
                2 -> R.drawable.ic_dice_two_48dp
                3 -> R.drawable.ic_dice_three_48dp
                4 -> R.drawable.ic_dice_four_48dp
                5 -> R.drawable.ic_dice_five_48dp
                else -> R.drawable.ic_dice_six_48dp
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (inDiscoveryMode) menuInflater.inflate(R.menu.menu_filter, menu)
        menuInflater.inflate(R.menu.menu_movie, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            android.R.id.home -> consume { onBackPressed() }
            else -> super.onOptionsItemSelected(item)
    }
}
