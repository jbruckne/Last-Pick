package joebruckner.lastpick

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

inline fun <reified T : View?> Activity.find(id: Int): T = findViewById(id) as T

fun Activity.viewUri(uri: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

fun Activity.getColor(colorRes: Int): Int {
    return if (Build.VERSION.SDK_INT >= 23) ContextCompat.getColor(this, colorRes)
    else resources.getColor(colorRes)
}

fun FragmentActivity.replaceFrame(frameId: Int, fragment: Fragment, backstack: Boolean = false) {
    val transaction = supportFragmentManager.beginTransaction().replace(frameId, fragment)
    if (backstack) transaction.addToBackStack(null).commit()
    else transaction.commit()
}

fun FragmentActivity.getFragment(id: Int): Fragment {
    return supportFragmentManager.findFragmentById(id)
}

fun AppCompatActivity.sethomeAsUpEnabled(isEnabled: Boolean) {
    supportActionBar?.setDisplayHomeAsUpEnabled(isEnabled)
}

fun AppCompatActivity.setTitleEnabled(isEnabled: Boolean) {
    supportActionBar?.setDisplayShowTitleEnabled(isEnabled)
}

inline fun <reified T : View?> Fragment.find(id: Int): T = view!!.findViewById(id) as T

inline fun <reified T : View?> View.find(id: Int): T = findViewById(id) as T

fun ViewGroup.inflate(id: Int): View = LayoutInflater.from(context).inflate(id, this, false)

fun Int.toRuntimeFormat(): String = "${this/60}:${if (this % 60 < 10) "0" else ""}${this % 60}"

inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}