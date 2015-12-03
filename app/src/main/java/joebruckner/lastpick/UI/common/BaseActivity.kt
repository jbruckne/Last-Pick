package joebruckner.lastpick.ui.common

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.ui.about.AboutActivity
import joebruckner.lastpick.ui.common.BaseFragment

abstract class BaseActivity : AppCompatActivity() {
    abstract val layoutId: Int
    open val menuId: Int = R.menu.menu_about

    val logTag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        if (savedInstanceState == null) Log.d(logTag, "Initial Creation!!!")
        else Log.d(logTag, "Returning!!!")
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

    fun color(colorRes: Int): Int {
        if (Build.VERSION.SDK_INT >= 23)
            return ContextCompat.getColor(this, colorRes)
        return resources.getColor(colorRes)
    }

    fun displayHomeAsUp(homeAsUp: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUp)
    }
}