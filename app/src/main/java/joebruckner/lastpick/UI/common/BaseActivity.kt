package joebruckner.lastpick.ui.common

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import joebruckner.lastpick.R
import joebruckner.lastpick.utils.find

abstract class BaseActivity : AppCompatActivity() {
    protected abstract val layoutId: Int
    protected open val fabId = R.id.fab
    protected open val appBarId = R.id.appBar
    protected open val toolbarId = R.id.toolbar
    protected open val tabLayoutId = R.id.tab_layout
    protected open val collapsingToolbarId = R.id.collapsingToolbar
    protected open var menuId: Int = R.menu.menu_empty

    val logTag = javaClass.simpleName!!

    var isFirstStart: Boolean = true
    var isThemeLocked: Boolean = false

    val fab                 by lazy { find<FloatingActionButton?>(fabId) }
    val toolbar             by lazy { find<Toolbar?>(toolbarId) }
    val appBar              by lazy { find<AppBarLayout?>(appBarId) }
    val tabLayout           by lazy { find<TabLayout?>(tabLayoutId) }
    val collapsingToolbar   by lazy { find<CollapsingToolbarLayout?>(collapsingToolbarId) }
    var menu: Menu? = null

    var colorPrimary: Int = Color.GRAY
    var colorPrimaryDark: Int = Color.GRAY
    var colorAccent: Int = Color.GRAY

    var title: String = " "
        set(value: String) {
            field = value
            supportActionBar?.title = value
        }

    private var fabIsEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setSupportActionBar(toolbar)

        resetTheme()
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

    fun resetTheme() {
        val array = obtainStyledAttributes(intArrayOf(
                R.attr.colorPrimary,
                R.attr.colorPrimaryDark,
                R.attr.colorAccent
        ))
        setPrimary(array.getColor(0, Color.GRAY))
        setDark(array.getColor(1, Color.DKGRAY))
        setAccent(array.getColor(2, Color.CYAN))
    }

    fun setPrimary(color: Int) {
        if (isThemeLocked) return
        val primaryAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorPrimary, color)
        primaryAnimator.addUpdateListener { animator ->
            appBar?.setBackgroundColor(animator.animatedValue as Int)
            tabLayout?.setBackgroundColor(animator.animatedValue as Int)
            collapsingToolbar?.setBackgroundColor(animator.animatedValue as Int)
            collapsingToolbar?.setContentScrimColor(animator.animatedValue as Int)
        }
        primaryAnimator.duration = 250
        primaryAnimator.start()
        colorPrimary = color
    }

    fun setDark(color: Int) {
        if (isThemeLocked) return
        val darkAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorPrimaryDark, color)
        darkAnimator.addUpdateListener { animator ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                window.statusBarColor = animator.animatedValue as Int
        }
        darkAnimator.duration = 250
        darkAnimator.start()
        colorPrimaryDark = color
    }

    fun setAccent(color: Int) {
        if (isThemeLocked) return
        val animator = ValueAnimator.ofObject(ArgbEvaluator(), colorAccent, color)
        animator.addUpdateListener { animator ->
            fab?.backgroundTintList = ColorStateList.valueOf(color)
            tabLayout?.setSelectedTabIndicatorColor(color)
        }
        animator.duration = 200
        animator.start()
        colorAccent = color
    }

    fun enableFab() {
        fabIsEnabled = true
        fab?.show(object: FloatingActionButton.OnVisibilityChangedListener() {
            override fun onShown(f: FloatingActionButton) {
                if (!fabIsEnabled) disableFab()
            }
        })
    }

    fun disableFab() {
        fabIsEnabled = false
        fab?.hide(object: FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(f: FloatingActionButton) {
                if (fabIsEnabled) enableFab()
            }
        })
    }

    open fun inject(fragment: Fragment) {
        throw Exception("$logTag cannot inject ${fragment.javaClass.simpleName}")
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackPressListener && it.onBackPressed()) return
        }
        super.onBackPressed()
    }
}