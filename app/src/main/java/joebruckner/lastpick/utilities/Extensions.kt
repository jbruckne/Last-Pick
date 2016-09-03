package joebruckner.lastpick.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import joebruckner.lastpick.widgets.PaletteTheme
import joebruckner.lastpick.widgets.SimpleRequestListener
import java.util.*

inline fun <reified T : View?> Activity.find(id: Int): T = findViewById(id) as T

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

fun ImageView.load(context: Context, path: String,
                   success: (GlideDrawable) -> Unit = {},
                   error: (Exception) -> Unit = {}
) {
    Glide.with(context).load(path).listener(object: RequestListener<String, GlideDrawable> {
        override fun onException(e: Exception, model: String?, target: Target<GlideDrawable>?,
                                 isFirstResource: Boolean): Boolean {
            error.invoke(e)
            return false
        }

        override fun onResourceReady(resource: GlideDrawable, model: String?,
                                     target: Target<GlideDrawable>?, isFromMemoryCache: Boolean,
                                     isFirstResource: Boolean): Boolean {
            success.invoke(resource)
            return false
        }
    }).into(this)
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

fun Int.isEven(): Boolean = this % 2 == 0