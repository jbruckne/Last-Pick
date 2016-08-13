package joebruckner.lastpick.widgets

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

class FixedScrollingViewBehavior : AppBarLayout.ScrollingViewBehavior {

    constructor() {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    override fun onMeasureChild(parent: CoordinatorLayout, child: View, parentWidthMeasureSpec: Int,
                       widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int): Boolean {
        if (child.layoutParams.height === -1) {
            val dependencies = parent.getDependencies(child)
            if (dependencies.isEmpty()) {
                return false
            }

            val appBar = findFirstAppBarLayout(dependencies)
            if (appBar != null && ViewCompat.isLaidOut(appBar)) {
                if (ViewCompat.getFitsSystemWindows(appBar)) {
                    ViewCompat.setFitsSystemWindows(child, true)
                }

                val scrollRange = appBar.totalScrollRange
                val height = parent.height - appBar.measuredHeight + Math.min(scrollRange, parent.height - heightUsed)
                val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed)
                return true
            }
        }

        return false
    }

    private fun findFirstAppBarLayout(views: List<View>): AppBarLayout? {
        var i = 0

        val z = views.size
        while (i < z) {
            if (views[i] is AppBarLayout) {
                return views[i] as AppBarLayout
            }
            ++i
        }

        return null
    }
}