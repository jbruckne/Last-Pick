package joebruckner.lastpick.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import joebruckner.lastpick.R
import joebruckner.lastpick.ui.about.AboutActivity
import joebruckner.lastpick.ui.common.BaseActivity
import joebruckner.lastpick.ui.home.bookmark.BookmarkFragment
import joebruckner.lastpick.ui.home.history.HistoryFragment
import joebruckner.lastpick.ui.home.landing.LandingFragment
import joebruckner.lastpick.ui.intro.IntroActivity
import joebruckner.lastpick.ui.movie.MovieActivity
import joebruckner.lastpick.ui.settings.SettingsActivity
import joebruckner.lastpick.utils.consume
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.replaceFrame
import joebruckner.lastpick.utils.setHomeAsUpEnabled
import jonathanfinerty.once.Once
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView

class HomeActivity : BaseActivity() {
    override val layoutId = R.layout.activity_home
    override var menuId = R.menu.menu_home

    val drawerLayout by lazy { find<DrawerLayout>(R.id.drawer_layout) }
    val navigationView by lazy { find<NavigationView>(R.id.navigation_view) }

    val navBackstack = mutableSetOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Once.beenDone(Once.THIS_APP_INSTALL, "onboarding")) {
            startActivity(Intent(this, IntroActivity::class.java))
            Once.markDone("onboarding")
        }

        replaceFrame(R.id.frame, LandingFragment(), false)
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
        sequence.start()
    }

    override fun onResume() {
        super.onResume()
        drawerLayout.closeDrawers()
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
        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()
            when (item.itemId) {
                R.id.action_home -> consume {
                    setupHomePage()
                    replaceFrame(R.id.frame, LandingFragment(), false)
                    title = "Last Pick"
                }
                R.id.action_history -> consume {
                    setupDefaultPage()
                    replaceFrame(R.id.frame, HistoryFragment(), false)
                    title = "History"
                }
                R.id.action_bookmarks -> consume {
                    setupDefaultPage()
                    replaceFrame(R.id.frame, BookmarkFragment(), false)
                    title = "Bookmarks"
                }
                R.id.action_settings ->  {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    false
                }
                R.id.action_send_feedback -> {
                    sendFeedback()
                    false
                }
                R.id.action_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    false
                }
                else -> false
            }
        }
    }

    fun sendFeedback() {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("joebrucknerdev@gmail.com") +
                "?subject=" + Uri.encode("Feedback")
        val uri = Uri.parse(uriText)
        send.data = uri
        startActivity(Intent.createChooser(send, "Send Feedback..."))
    }

    fun setupHomePage() {
        isThemeLocked = false
        menuId = R.menu.menu_home
        supportInvalidateOptionsMenu()
        enableFab()
    }

    fun setupDefaultPage() {
        resetTheme()
        isThemeLocked = true
        menuId = R.menu.menu_empty
        supportInvalidateOptionsMenu()
        appBar?.setExpanded(false, false)
        disableFab()
    }

    override fun onStart() {
        super.onStart()
        fab?.setOnClickListener {
            startActivity(Intent(this, MovieActivity::class.java))
        }
    }
}