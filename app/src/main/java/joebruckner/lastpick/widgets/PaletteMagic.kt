package joebruckner.lastpick.widgets

import android.graphics.Color
import android.support.v7.graphics.Palette
import android.util.Log

class PaletteMagic(val palette: Palette) {
    var primary: Int
    var accent: Int
    var dark: Int

    init {
        val muted = palette.mutedSwatch;
        val vibrant = palette.vibrantSwatch;
        primary = Color.parseColor("#455A64");
        dark = Color.parseColor("#263238");
        accent = Color.parseColor("#607D8B");
        if (muted != null) {
            Log.d("Palette", "Muted swatch used");
            primary = palette.getMutedColor(Color.BLACK);
            val hsl = muted.hsl;
            hsl[2] *= 0.9f;
            dark = Color.HSVToColor(hsl);
            hsl[2] *= 1.8f;
            accent = Color.HSVToColor(hsl);
        } else if (vibrant != null) {
            Log.d("Palette", "Vibrant swatch used");
            primary = palette.getVibrantColor(Color.BLACK);
            val hsl = vibrant.hsl;
            hsl[2] *= 0.9f;
            dark = Color.HSVToColor(hsl);
            hsl[2] *= 1.8f;
            accent = Color.HSVToColor(hsl);
        } else Log.d("Palette", "Default swatch used");
    }
}