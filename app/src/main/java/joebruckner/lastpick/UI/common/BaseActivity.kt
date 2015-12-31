package joebruckner.lastpick.ui.common

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.support.v7.widget.ViewStubCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import joebruckner.lastpick.R
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.ui.about.AboutActivity

abstract class BaseActivity : AppCompatActivity() {
    abstract val layoutId: Int
    open val fabId = R.id.fab
    open val appBarId = R.id.appBar
    open val toolbarId = R.id.toolbar
    open val toolbarStubId: Int = R.id.stub
    open val collapsingToolbarId: Int = R.id.collapsingToolbar
    open val menuId: Int = R.menu.menu_about

    val logTag = javaClass.simpleName

    var fab: FloatingActionButton? = null
    lateinit var root: View
    lateinit var appBar: AppBarLayout
    lateinit var toolbar: Toolbar
    lateinit var toolbarStub: ViewStubCompat
    lateinit var collapsingToolbar: CollapsingToolbarLayout

    var title: String = ""
        set(new: String) {
            field = new
            collapsingToolbar.title = new
        }

    private var fabIsEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        root = window.decorView.rootView

        val f = findViewById(fabId)
        fab = if (f != null) f as FloatingActionButton else null
        appBar = findViewById(appBarId) as AppBarLayout
        toolbar = findViewById(toolbarId) as Toolbar
        toolbarStub = findViewById(toolbarStubId) as ViewStubCompat
        collapsingToolbar = findViewById(collapsingToolbarId) as CollapsingToolbarLayout

        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(menuId, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, javaClass<AboutActivity>()))
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    fun sendAction(action: String) {
        supportFragmentManager.fragments.forEach {
            if (it is BaseFragment) it.handleAction(Action(action))
        }
    }

    fun replaceFrame(frameId: Int, fragment: Fragment, backstack: Boolean = false) {
        var transaction = supportFragmentManager.beginTransaction().replace(frameId, fragment)
        if (backstack) transaction.addToBackStack(null).commit()
        else transaction.commit()
    }

    fun setToolbarStubLayout(layoutId: Int) {
        toolbarStub.layoutResource = layoutId
        toolbarStub.inflate()
    }

    fun setTheme(primary: Int, dark: Int, accent: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = dark
        appBar.setBackgroundColor(primary)
        collapsingToolbar.setBackgroundColor(primary)
        collapsingToolbar.setContentScrimColor(primary)
        fab?.backgroundTintList = ColorStateList.valueOf(accent)
    }

    fun color(colorRes: Int): Int {
        if (Build.VERSION.SDK_INT >= 23)
            return ContextCompat.getColor(this, colorRes)
        return resources.getColor(colorRes)
    }

    fun displayHomeAsUp(homeAsUp: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUp)
    }

    fun enableFab() {
        fabIsEnabled = true
        fab?.show(object: FloatingActionButton.OnVisibilityChangedListener() {
            override fun onShown(f: FloatingActionButton?) {
                if (!fabIsEnabled) disableFab()
            }
        })
    }

    fun disableFab() {
        fabIsEnabled = false
        fab?.hide(object: FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(f: FloatingActionButton?) {
                if (fabIsEnabled) enableFab()
            }
        })
    }

    fun removeFab() {
        fab?.visibility = View.GONE
    }
}