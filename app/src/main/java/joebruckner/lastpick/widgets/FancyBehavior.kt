package joebruckner.lastpick.widgets

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

class FancyBehavior<V : View> : CoordinatorLayout.Behavior<V> {

    constructor()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}
