package joebruckner.lastpick

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.View

inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id) as T

fun Activity.viewUri(uri: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

inline fun <reified T : View> Fragment.find(id: Int): T = view!!.findViewById(id) as T

fun Int.toRuntimeFormat(): String = "${this/60}:${if (this % 60 < 10) "0" else ""}${this % 60}"

inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}