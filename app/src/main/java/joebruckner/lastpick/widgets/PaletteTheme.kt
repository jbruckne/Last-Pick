package joebruckner.lastpick.widgets

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.graphics.Palette
import android.util.Log
import joebruckner.lastpick.darkenColor

class PaletteTheme(val palette: Palette) {
    val defaultPrimary = Color.GRAY
    val defaultPrimaryDark = Color.DKGRAY
    val defaultAccent = Color.CYAN

    private fun choosePrimarySwatch() = when (true) {
        palette.mutedSwatch         != null -> {
            Log.d("Palette", "Muted")
            palette.mutedSwatch
        }
        palette.vibrantSwatch       != null -> {
            Log.d("Palette", "vibrant")
            palette.vibrantSwatch
        }
        palette.darkVibrantSwatch   != null -> {
            Log.d("Palette", "darkVibrant")
            palette.darkVibrantSwatch
        }
        palette.darkMutedSwatch     != null -> {
            Log.d("Palette", "darkMuted")
            palette.darkMutedSwatch
        }
        else -> palette.swatches.elementAtOrNull(0)
    }

    private fun chooseAccentSwatch() = when (true) {
        palette.vibrantSwatch       != null -> palette.vibrantSwatch
        palette.lightVibrantSwatch  != null -> palette.lightVibrantSwatch
        palette.darkVibrantSwatch   != null -> palette.darkVibrantSwatch
        palette.lightMutedSwatch    != null -> palette.lightMutedSwatch
        else -> palette.swatches.elementAtOrNull(0)
    }

    fun getPrimaryColor() = choosePrimarySwatch()?.rgb ?: defaultPrimary

    fun getPrimaryDarkColor() = choosePrimarySwatch()?.rgb?.darkenColor() ?: defaultPrimaryDark

    fun getAccentColor() = chooseAccentSwatch()?.rgb ?: defaultAccent

    class Builder(val resource: Bitmap) {
        fun generateFrom(listener: (PaletteTheme) -> Unit) {
            Palette.from(resource).generate { palette ->
                listener(PaletteTheme(palette))
            }
        }
    }
}