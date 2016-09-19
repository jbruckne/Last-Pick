package joebruckner.lastpick.utilities

import android.content.Context

fun dpToPixels(context: Context, dp: Int): Int {
    val dpi = context.resources.displayMetrics.densityDpi
    return dp * (dpi / 160)
}

fun pixelsToDp(context: Context, pixels: Int): Int {
    val dpi = context.resources.displayMetrics.densityDpi
    return pixels * (160 / dpi)
}