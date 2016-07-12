package joebruckner.lastpick.widgets

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.util.Log
import android.view.View

class FixedNestedScrollView : NestedScrollView {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        Log.d("ScrollView", "$target: dx=$dx, dy=$dy, consumed=$consumed")
        dispatchNestedPreScroll(dx, dy, consumed, null)
    }

    override fun onNestedFling(target: View?, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        Log.d("ScrollView", "$target: x=$velocityX, y=$velocityY, consumed=$consumed")
        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }
}