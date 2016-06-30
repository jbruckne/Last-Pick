package joebruckner.lastpick.widgets

import android.graphics.Bitmap
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

open class SimpleRequestListener(val listener: (Bitmap) -> Unit): RequestListener<String, Bitmap> {

    override fun onException(e: Exception?, model: String, target: Target<Bitmap>?,
                             isFirstResource: Boolean): Boolean {
        e?.printStackTrace()
        return false
    }

    override fun onResourceReady(resource: Bitmap, model: String, target: Target<Bitmap>?,
                                 isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
        listener.invoke(resource)
        return false
    }
}