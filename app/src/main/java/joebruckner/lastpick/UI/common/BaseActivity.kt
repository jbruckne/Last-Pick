package joebruckner.lastpick.ui.common

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
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
import joebruckner.lastpick.ui.about.AboutActivity
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    protected abstract val layoutId: Int
    protected open val fabId = R.id.fab
    protected open val appBarId = R.id.appBar
    protected open val toolbarId = R.id.toolbar
    protected open val toolbarStubId: Int = R.id.stub
    protected open val collapsingToolbarId: Int = R.id.collapsingToolbar
    protected open val menuId: Int = R.menu.menu_about

    val logTag = javaClass.simpleName

    var isFirstStart: Boolean = true

    var toolbarStub: ViewStubCompat? = null
    var fab: FloatingActionButton? = null
    var menu: Menu? = null
    lateinit var root: View
    lateinit var appBar: AppBarLayout
    lateinit var toolbar: Toolbar
    lateinit var collapsingToolbar: CollapsingToolbarLayout

    var colorPrimary: Int = Color.GRAY
    var colorPrimaryDark: Int = Color.GRAY
    var colorAccent: Int = Color.GRAY

    var title: String = " "
        set(newTitle: String) {
            field = newTitle
            collapsingToolbar.title = newTitle
            supportActionBar?.title = newTitle
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
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.Transparent)
    }

    override fun onResume() {
        super.onResume()
        supportInvalidateOptionsMenu()
    }

    override fun onPause() {
        super.onPause()
        isFirstStart = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(menuId, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    fun replaceFrame(frameId: Int, fragment: Fragment, backstack: Boolean = false) {
        var transaction = supportFragmentManager.beginTransaction().replace(frameId, fragment)
        if (backstack) transaction.addToBackStack(null).commit()
        else transaction.commit()
    }

    fun getFragment(id: Int): Fragment {
        return supportFragmentManager.findFragmentById(id)
    }

    fun setToolbarStubLayout(layoutId: Int) {
        toolbarStub?.layoutResource = layoutId
        toolbarStub?.inflate()
        toolbarStub = null
    }

    fun resetTheme() {
        val theme = getThemeMap()
        setTheme(
                theme["colorPrimary"] ?: Color.BLACK,
                theme["colorPrimaryDark"] ?: Color.BLACK,
                theme["colorAccent"] ?: Color.BLACK
        )
    }

    fun setTheme(primary: Int, dark: Int, accent: Int) {
        val primaryAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorPrimary, primary)
        primaryAnimator.addUpdateListener { animator ->
            appBar.setBackgroundColor(animator.animatedValue as Int)
            collapsingToolbar.setBackgroundColor(animator.animatedValue as Int)
            collapsingToolbar.setContentScrimColor(animator.animatedValue as Int)
        }
        primaryAnimator.setDuration(250)
        primaryAnimator.start()
        val darkAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorPrimaryDark, dark)
        darkAnimator.addUpdateListener { animator ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                window.statusBarColor = animator.animatedValue as Int
        }
        darkAnimator.setDuration(250)
        darkAnimator.start()
        fab?.backgroundTintList = ColorStateList.valueOf(accent)
        colorPrimary = primary
        colorPrimaryDark = dark
        colorAccent = accent
    }

    fun getThemeMap(): HashMap<String, Int> {
        val attr = arrayOf(
                R.attr.colorPrimary,
                R.attr.colorPrimaryDark,
                R.attr.colorAccent
        )
        val array = obtainStyledAttributes(R.style.AppTheme, attr.toIntArray())
        val map = hashMapOf<String, Int>()
        map.put("colorPrimary", array.getColor(0, Color.BLACK))
        map.put("colorPrimaryDark", array.getColor(1, Color.BLACK))
        map.put("colorAccent", array.getColor(2, Color.BLACK))
        array.recycle()
        return map
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
}