package joebruckner.lastpick.ui.common

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.view.Menu
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.ui.common.BaseFragment

abstract class BaseActivity : AppCompatActivity() {

    abstract val layoutId: Int
    abstract val menuId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(menuId, menu)
        return super.onCreateOptionsMenu(menu)
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
}