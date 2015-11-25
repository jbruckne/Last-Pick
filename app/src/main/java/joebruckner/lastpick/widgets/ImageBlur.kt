package joebruckner.lastpick.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

class ImageBlur(val context: Context, val radius: Float) : BitmapTransformation(context) {
    override fun transform(pool: BitmapPool?, image: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        val converted = image.copy(Config.ARGB_8888, true)
        val rs = RenderScript.create(context);

        val input = Allocation.createFromBitmap(rs, converted);
        val output = Allocation.createTyped(rs, input.type);
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(converted);

        return converted
    }

    override fun getId(): String? {
        return "Image Blur"
    }
}