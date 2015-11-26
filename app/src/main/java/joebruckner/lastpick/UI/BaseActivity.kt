package joebruckner.lastpick.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import joebruckner.lastpick.events.Action

abstract class BaseActivity : AppCompatActivity() {
    abstract val layoutId: Int
    abstract val menuId: Int

    abstract fun setTitle(title: String);

    abstract fun setBackdrop(imagePath: String);

    abstract fun setPoster(imagePath: String);

    abstract fun enableFab();

    abstract fun disableFab();

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

    fun replaceFrame(frameId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(frameId, fragment)
                .commit()
    }

    fun color(colorRes: Int): Int {
        if (Build.VERSION.SDK_INT >= 23)
            return ContextCompat.getColor(this, colorRes)
        return resources.getColor(colorRes)
    }
}