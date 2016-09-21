package joebruckner.lastpick.utilities

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.view.ViewAnimationUtils

fun dpToPixels(context: Context, dp: Int): Int {
    val dpi = context.resources.displayMetrics.densityDpi
    return dp * (dpi / 160)
}

fun pixelsToDp(context: Context, pixels: Int): Int {
    val dpi = context.resources.displayMetrics.densityDpi
    return pixels * (160 / dpi)
}

fun View.visibleIf(bool: Boolean, duration: Long = 100) {
    this.visibility = if (bool) View.VISIBLE else View.INVISIBLE
}

fun View.existsIf(boolean: Boolean, duration: Long = 100) {
    this.visibility = if (boolean) View.VISIBLE else View.GONE
}

fun NestedScrollView.simpleOnScrollListener(listener: (NestedScrollView) -> Unit) {
    this.setOnScrollChangeListener {
        nestedScrollView: NestedScrollView, x: Int, y: Int, oldX: Int, oldY: Int ->
        listener.invoke(nestedScrollView)
    }
}

fun Int.withAlpha(alpha: Byte): Int = (this and 0x00FFFFFF) or (alpha.toInt() shl 24)

@TargetApi(Build.VERSION_CODES.N)
fun View.animateBackgroundColor(color: Int, speed: Long = 100, retainAlpha: Boolean = true) {
    val background = this.background
    var oldColor = Color.GRAY
    var newColor = color
    if (background is ColorDrawable) oldColor = background.color
    if (retainAlpha) {
        val alpha = Color.alpha(oldColor).toByte()
        newColor = newColor.withAlpha(alpha)
    }
    if (isLollipop()) {
        val animator = ValueAnimator.ofArgb(oldColor, newColor)
        animator.addUpdateListener {
            setBackgroundColor(it.animatedValue as Int)
        }
        animator.duration = 300
        animator.start()
    } else {
        this.setBackgroundColor(newColor)
    }
}

@TargetApi(Build.VERSION_CODES.N)
fun FloatingActionButton.animateTint(newColor: Int, speed: Long = 100, retainAlpha: Boolean = true) {
    var oldColor = Color.GRAY
    this.backgroundTintList?.let { oldColor = it.defaultColor }
    if (isLollipop()) {
        val animator = ValueAnimator.ofArgb(oldColor, newColor)
        animator.addUpdateListener {
            this.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
        }
        animator.duration = 300
        animator.start()
    } else {
        this.backgroundTintList = ColorStateList.valueOf(newColor)
    }
}

@TargetApi(Build.VERSION_CODES.N)
fun View.circleEnter(
        delay: Long = 0,
        duration: Long = 100,
        x: Int = measuredWidth / 2,
        y: Int = measuredHeight / 2
) {
    this.post {
        val radius = Math.max(width, height)

        if (isLollipop() && isAttachedToWindow) {
            clearAnimation()
            val anim = ViewAnimationUtils
                    .createCircularReveal(this, x, y, 0f, radius.toFloat())
                    .setDuration(duration)
            anim.startDelay = delay
            anim.start()
        }
        visibility = View.VISIBLE
    }
}

@TargetApi(Build.VERSION_CODES.N)
fun View.circleExit(
        delay: Long = 0,
        duration: Long = 100,
        x: Int = measuredWidth / 2,
        y: Int = measuredHeight / 2
) {
    this.post {
        val radius = Math.max(width, height)

        if (isLollipop() && isAttachedToWindow) {
            clearAnimation()
            val anim = ViewAnimationUtils
                    .createCircularReveal(this, x, y, radius.toFloat(), 0f)
                    .setDuration(duration)
            anim.startDelay = delay
            anim.addListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {
                    this@circleExit.visibility = View.INVISIBLE
                }
                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationStart(p0: Animator?) {}
            })
            anim.start()
        }
    }
}