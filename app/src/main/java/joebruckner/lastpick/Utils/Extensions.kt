package joebruckner.lastpick.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import joebruckner.lastpick.widgets.PaletteTheme
import joebruckner.lastpick.widgets.SimpleRequestListener
import java.util.*

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

fun AppCompatActivity.setHomeAsUpEnabled(isEnabled: Boolean) {
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

fun View.visibleIf(bool: Boolean) {
    this.visibility = if (bool) View.VISIBLE else View.GONE
}

fun ImageView.load(fragment: Fragment, path: String) {
    Glide.with(fragment).load(path).centerCrop().into(this)
}

fun ImageView.load(context: Context, path: String) {
    Glide.with(context).load(path).centerCrop().into(this)
}

fun ImageView.loadWithPalette(context: Context, path: String, duration: Long, alpha: Float,
                              listener: (PaletteTheme) -> Unit) {
    Glide.with(context)
            .load(path)
            .asBitmap()
            .listener(SimpleRequestListener { resource ->
                PaletteTheme.Builder(resource).generateFrom { listener(it) }
                animate().alpha(alpha).duration = duration
            })
            .centerCrop()
            .into(this)
}

fun Int.darkenColor(): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    hsv[2] *= 0.8f
    return Color.HSVToColor(hsv)
}

fun AppBarLayout.onExpanse(f: () -> Unit) {
    this.setExpanded(true, true)
    this.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener {
        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            val expansion = Math.abs(
                    verticalOffset.toFloat() / appBarLayout.totalScrollRange.toFloat()
            )
            if (expansion <= 0) {
                f()
                this@onExpanse.removeOnOffsetChangedListener(this)
            }
        }
    })
}

fun <T> Collection<T>.shuffle(): List<T> {
    val positions = IntRange(0, this.size-1).toMutableList()
    val random = Random(System.currentTimeMillis())
    val shuffled = ArrayList<T>(this)
    this.forEach { item ->
        val x = random.nextInt(positions.size)
        val position = positions.removeAt(x)
        shuffled.removeAt(position)
        shuffled.add(position, item)
    }
    return shuffled
}

fun <T> MutableCollection<T>.addIfNotContained(item: T) {
    if (!this.contains(item)) this.add(item)
}