package joebruckner.lastpick.widgets

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.graphics.Palette
import joebruckner.lastpick.data.Theme

class PaletteTheme(val resource: Bitmap) {

    fun generateMutedTheme(listener: (Theme) -> Unit) {
        var accentSwatch: Palette.Swatch? = null
        var primarySwatch: Palette.Swatch? = null
        var primaryDarkSwatch: Palette.Swatch? = null
        Palette.from(resource).generate { palette ->
            if (palette.mutedSwatch != null && palette.darkMutedSwatch != null) {
                primarySwatch = palette.mutedSwatch!!
                primaryDarkSwatch = palette.darkMutedSwatch!!
            } else if (palette.vibrantSwatch != null && palette.darkVibrantSwatch != null) {
                primarySwatch = palette.vibrantSwatch!!
                primaryDarkSwatch = palette.darkVibrantSwatch!!
            } else if (palette.lightMutedSwatch != null && palette.mutedSwatch != null) {
                primarySwatch = palette.lightMutedSwatch!!
                primaryDarkSwatch = palette.mutedSwatch!!
            } else if (palette.lightVibrantSwatch != null && palette.vibrantSwatch != null) {
                primarySwatch = palette.lightVibrantSwatch!!
                primaryDarkSwatch = palette.vibrantSwatch!!
            }
            accentSwatch = if (palette.vibrantSwatch != null) palette.vibrantSwatch!!
            else if (palette.lightVibrantSwatch != null) palette.lightVibrantSwatch!!
            else if (palette.darkVibrantSwatch != null) palette.darkVibrantSwatch!!
            else if (palette.lightMutedSwatch != null) palette.lightMutedSwatch!!
            else palette.mutedSwatch!!
            val theme = Theme(
                    primarySwatch?.rgb ?: Color.GRAY,
                    accentSwatch?.rgb ?: Color.CYAN,
                    primaryDarkSwatch?.rgb ?: Color.DKGRAY,
                    primarySwatch?.titleTextColor ?: Color.WHITE
            )
            listener.invoke(theme)
        }
    }
}