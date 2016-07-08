package joebruckner.lastpick.ui.home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import joebruckner.lastpick.*
import joebruckner.lastpick.ui.about.AboutActivity
import joebruckner.lastpick.ui.common.BaseActivity
import joebruckner.lastpick.ui.movie.MovieFragment
import jonathanfinerty.once.Once
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView
import java.util.*

class HomeActivity : BaseActivity() {
    override val layoutId = R.layout.activity_home
    override var menuId = R.menu.menu_home

    val drawerLayout by lazy { find<DrawerLayout>(R.id.drawer_layout) }
    val navigationView by lazy { find<NavigationView>(R.id.navigation_view) }

    var lastSelectedItem = R.id.action_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Once.beenDone(Once.THIS_APP_INSTALL, "onboarding")) {
            startActivity(Intent(this, IntroActivity::class.java))
            Once.markDone("onboarding")
        }

        replaceFrame(R.id.frame, MovieFragment.newInstance(savedInstanceState != null), false)
        setupNavDrawer()

        if (!Once.beenDone(Once.THIS_APP_VERSION, "showcase")) {
            setupShowcase()
        }
    }

    fun setupShowcase() {
        val sequence = MaterialShowcaseSequence(this, "4815")
        sequence.addSequenceItem(
                MaterialShowcaseView.Builder(this)
                        .setTarget(fab)
                        .setContentText("Click this to shuffle the movie results")
                        .setDismissText("GOT IT")
                        .setShapePadding(20)
                        .setDelay(1000)
                        .build()
        )
        //sequence.addSequenceItem(find<View>(R.id.action_filter), "Use these tools to bookmark, share, or filter movie results", "GOT IT")
        //sequence.addSequenceItem(find<View>(android.R.id.home), "Navigate to your results history, stored bookmarks, or other pages through here", "GOT IT")
        sequence.start()
    }

    fun setupNavDrawer() {
        setHomeAsUpEnabled(true)
        val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setCheckedItem(lastSelectedItem)
        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()
            title = item.title.toString()
            if (item.groupId == R.id.section_main) lastSelectedItem = item.itemId
            when (item.itemId) {
                R.id.action_home -> consume {
                    setupHomePage()
                    replaceFrame(R.id.frame, MovieFragment.newInstance(false), false)
                }
                R.id.action_history -> consume {
                    setupDefaultPage()
                    replaceFrame(R.id.frame, HistoryFragment(), false)
                }
                R.id.action_bookmarks -> consume {
                    setupDefaultPage()
                    replaceFrame(R.id.frame, BookmarksFragment(), false)
                }
                R.id.action_settings, R.id.action_send_feedback -> consume {
                    Snackbar.make(drawerLayout, "Coming Soon", Snackbar.LENGTH_SHORT).show()
                }
                R.id.action_about -> consume {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                else -> false
            }
        }
    }

    fun setupHomePage() {
        menuId = R.menu.menu_home
        supportInvalidateOptionsMenu()
        appBar.isActivated = true
        enableFab()
    }

    fun setupDefaultPage() {
        menuId = R.menu.menu_empty
        supportInvalidateOptionsMenu()
        appBar.setExpanded(false, false)
        appBar.isActivated = false
        resetTheme()
        disableFab()
    }

    override fun onStart() {
        super.onStart()
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
        navigationView.menu.findItem(lastSelectedItem).isChecked = true
    }
}