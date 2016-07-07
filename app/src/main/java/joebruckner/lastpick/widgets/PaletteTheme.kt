package joebruckner.lastpick.widgets

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.graphics.Palette

class PaletteTheme(val palette: Palette) {
    val defaultPrimary = Color.GRAY
    val defaultPrimaryDark = Color.DKGRAY
    val defaultAccent = Color.CYAN

    fun getPrimaryColor() = when (true) {
        palette.mutedSwatch != null        -> palette.mutedSwatch!!.rgb
        palette.lightMutedSwatch != null   -> palette.lightMutedSwatch!!.rgb
        palette.vibrantSwatch != null      -> palette.vibrantSwatch!!.rgb
        palette.lightVibrantSwatch != null -> palette.lightVibrantSwatch!!.rgb
        else -> defaultPrimary
    }

    fun getPrimaryDarkColor() = when (true) {
        palette.darkMutedSwatch != null    -> palette.darkMutedSwatch!!.rgb
        palette.mutedSwatch != null        -> palette.mutedSwatch!!.rgb
        palette.darkVibrantSwatch != null  -> palette.darkVibrantSwatch!!.rgb
        palette.vibrantSwatch != null      -> palette.vibrantSwatch!!.rgb
        else -> defaultPrimaryDark
    }

    fun getAccentColor() = when (true) {
        palette.vibrantSwatch != null      -> palette.vibrantSwatch!!.rgb
        palette.lightVibrantSwatch != null -> palette.lightVibrantSwatch!!.rgb
        palette.darkVibrantSwatch != null  -> palette.darkVibrantSwatch!!.rgb
        palette.lightMutedSwatch != null   -> palette.lightMutedSwatch!!.rgb
        else -> defaultAccent
    }

    class Builder(val resource: Bitmap) {
        fun generateFrom(listener: (PaletteTheme) -> Unit) {
            Palette.from(resource).generate { palette ->
                listener(PaletteTheme(palette))
            }
        }
    }
}