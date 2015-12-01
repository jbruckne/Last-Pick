package joebruckner.lastpick.widgets

import android.view.animation.Animation

abstract class OnAnimationFinishedListener : Animation.AnimationListener {

    override fun onAnimationStart(animation: Animation) {
    }

    abstract override fun onAnimationEnd(animation: Animation)

    override fun onAnimationRepeat(animation: Animation) {
    }
}
