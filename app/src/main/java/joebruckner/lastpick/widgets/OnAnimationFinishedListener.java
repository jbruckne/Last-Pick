package joebruckner.lastpick.widgets;

import android.view.animation.Animation;

public abstract class OnAnimationFinishedListener implements Animation.AnimationListener {

	@Override public void onAnimationStart(Animation animation) {}

	@Override public abstract void onAnimationEnd(Animation animation);

	@Override public void onAnimationRepeat(Animation animation) {}
}
