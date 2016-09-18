package joebruckner.lastpick.view.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.MainApp
import joebruckner.lastpick.R
import joebruckner.lastpick.R.*
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.utils.consume
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.replaceFrame
import joebruckner.lastpick.utils.setHomeAsUpEnabled
import joebruckner.lastpick.view.common.BaseActivity
import joebruckner.lastpick.view.home.bookmark.BookmarkFragment
import joebruckner.lastpick.view.home.history.HistoryFragment
import joebruckner.lastpick.view.home.landing.LandingFragment
import jonathanfinerty.once.Once
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView.Builder

class HomeActivity : BaseActivity() {
    // Parameters
    override val layoutId = layout.activity_home
    override var menuId = R.menu.menu_home

    // Objects
    lateinit var navigator: FlowNavigator

    // Views
    val drawerLayout by lazy { find<DrawerLayout>(id.drawer_layout) }
    val navigationView by lazy { find<NavigationView>(id.navigation_view) }

    val component by lazy {
        (application as MainApp)
                .component
                .getHomeComponent(ActivityModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = component.getNavigator()

        replaceFrame(id.frame, LandingFragment(), false)
        setupNavDrawer()

        if (!Once.beenDone(Once.THIS_APP_VERSION, "showcase")) {
            Once.markDone("showcase")
            setupShowcase()
        }
        fab?.setOnClickListener { navigator.showRandom() }
    }

    fun setupShowcase() {
        val sequence = MaterialShowcaseSequence(this, "4815")
        val accent = ContextCompat.getColor(this, color.accent_blue)
        val color = ColorStateList.valueOf(accent).withAlpha(225).defaultColor
        sequence.addSequenceItem(
                Builder(this)
                        .setTarget(fab)
                        .setContentText("Click the die to get a random movie suggestion.")
                        .setDismissText("GOT IT")
                        .setShapePadding(40)
                        .setMaskColour(color)
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
                string.open_drawer,
                string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()
            when (item.itemId) {
                id.action_home -> consume {
                    setupHomePage()
                    replaceFrame(id.frame, LandingFragment(), false)
                    title = "Last Pick"
                }
                id.action_history -> consume {
                    setupDefaultPage()
                    replaceFrame(id.frame, HistoryFragment(), false)
                    title = "History"
                }
                id.action_bookmarks -> consume {
                    setupDefaultPage()
                    replaceFrame(id.frame, BookmarkFragment(), false)
                    title = "Bookmarks"
                }
                id.action_send_feedback -> {
                    navigator.sendFeedback()
                    false
                }
                id.action_about -> {
                    navigator.showAbout()
                    false
                }
                id.action_login -> {
                    navigator.showLogin()
                    false
                }
                else -> false
            }
        }
    }

    fun setupHomePage() {
        menuId = R.menu.menu_home
        supportInvalidateOptionsMenu()
    }

    fun setupDefaultPage() {
        menuId = R.menu.menu_empty
        supportInvalidateOptionsMenu()
    }

    override fun inject(injectee: Any) {
        when (injectee) {
            is HomeActivity -> component.inject(injectee)
            is LandingFragment -> component.inject(injectee)
            is HistoryFragment -> component.inject(injectee)
            is BookmarkFragment -> component.inject(injectee)
            else -> super.inject(injectee)
        }
    }
}