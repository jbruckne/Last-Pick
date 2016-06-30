package joebruckner.lastpick.widgets

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import joebruckner.lastpick.data.Theme

class PaletteTheme(val resource: Bitmap) {

    fun generateMutedTheme(listener: (Theme) -> Unit) {
        var accentSwatch: Palette.Swatch
        var primarySwatch: Palette.Swatch
        var primaryDarkSwatch: Palette.Swatch
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
            } else {
                primarySwatch = palette.swatches[0]
                primaryDarkSwatch = palette.swatches[1]
            }
            accentSwatch = if (palette.vibrantSwatch != null) palette.vibrantSwatch!!
            else if (palette.lightVibrantSwatch != null) palette.lightVibrantSwatch!!
            else if (palette.darkVibrantSwatch != null) palette.darkVibrantSwatch!!
            else if (palette.lightMutedSwatch != null) palette.lightMutedSwatch!!
            else palette.mutedSwatch!!
            val theme = Theme(
                    primarySwatch.rgb,
                    accentSwatch.rgb,
                    primaryDarkSwatch.rgb,
                    primarySwatch.titleTextColor
            )
            listener.invoke(theme)
        }
    }
}