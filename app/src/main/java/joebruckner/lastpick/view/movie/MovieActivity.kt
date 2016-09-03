package joebruckner.lastpick.view.movie

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.MainApp
import joebruckner.lastpick.R
import joebruckner.lastpick.view.common.BaseActivity
import joebruckner.lastpick.view.movie.fragments.MovieFragment
import joebruckner.lastpick.view.movie.fragments.MovieInfoFragment
import joebruckner.lastpick.view.movie.fragments.MovieMediaFragment
import joebruckner.lastpick.view.movie.fragments.MovieReviewFragment
import joebruckner.lastpick.utils.consume
import joebruckner.lastpick.utils.getFragment
import joebruckner.lastpick.utils.replaceFrame
import joebruckner.lastpick.utils.setHomeAsUpEnabled
import java.util.*

class MovieActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_movie

    private var inDiscoveryMode = false
    private val component by lazy {
        (application as MainApp)
                .component
                .getMovieComponent(ActivityModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }

        val id: Int?
        if (Intent.ACTION_VIEW == intent.action)
            id = intent.data.lastPathSegment.substringBefore('-').toInt()
        else if (intent.getIntExtra("movie", -1) != -1)
            id = intent.getIntExtra("movie", 0)
        else {
            id = null
            inDiscoveryMode = true
        }

        replaceFrame(
                R.id.frame,
                MovieFragment.newInstance(inDiscoveryMode, savedInstanceState != null, id)
        )
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
                1 -> R.drawable.ic_dice_1
                2 -> R.drawable.ic_dice_2
                3 -> R.drawable.ic_dice_3
                4 -> R.drawable.ic_dice_4
                5 -> R.drawable.ic_dice_5
                else -> R.drawable.ic_dice_6
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

    override fun inject(injectee: Any) {
        when (injectee) {
            is MovieFragment -> component.inject(injectee)
            is MovieInfoFragment -> component.inject(injectee)
            is MovieReviewFragment -> component.inject(injectee)
            is MovieMediaFragment -> component.inject(injectee)
            else -> super.inject(injectee)
        }
    }
}
