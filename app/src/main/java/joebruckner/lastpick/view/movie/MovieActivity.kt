package joebruckner.lastpick.view.movie

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.MainApp
import joebruckner.lastpick.R
import joebruckner.lastpick.utilities.consume
import joebruckner.lastpick.utilities.getFragment
import joebruckner.lastpick.utilities.replaceFrame
import joebruckner.lastpick.utilities.setHomeAsUpEnabled
import joebruckner.lastpick.view.common.BaseActivity
import java.util.*

class MovieActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_movie

    private var id: Int = -1
    private val component by lazy {
        (application as MainApp)
                .component
                .getMovieComponent(ActivityModule(this))
    }

    companion object {
        val NO_ID = -1
        val MOVIE = "movie"
        val ID = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) return

        if (Intent.ACTION_VIEW == intent.action)
            id = intent.data.lastPathSegment.substringBefore('-').toInt()
        else
            id = intent.getIntExtra(MOVIE, NO_ID)


        replaceFrame(R.id.frame, MovieFragment.newInstance(id))
        setHomeAsUpEnabled(true)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        id = savedInstanceState.getInt(ID, NO_ID)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (id != NO_ID) fab?.hide()
        setupRandomButton()
    }

    private fun setupRandomButton() {
        fab?.setOnClickListener {
            (getFragment(R.id.frame) as MovieFragment).callForUpdate()
            fab?.setImageResource(when (Random().nextInt(6) + 1) {
                1 -> R.drawable.ic_dice_1
                2 -> R.drawable.ic_dice_2
                3 -> R.drawable.ic_dice_3
                4 -> R.drawable.ic_dice_4
                5 -> R.drawable.ic_dice_5
                else -> R.drawable.ic_dice_6
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ID, id)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (id == NO_ID) menuInflater.inflate(R.menu.menu_filter, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            android.R.id.home -> consume { onBackPressed() }
            else -> super.onOptionsItemSelected(item)
    }

    override fun inject(injectee: Any) {
        when (injectee) {
            is MovieFragment -> component.inject(injectee)
            else -> super.inject(injectee)
        }
    }
}
