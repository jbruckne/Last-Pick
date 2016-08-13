package joebruckner.lastpick.widgets

import android.support.v7.widget.StaggeredGridLayoutManager

class GridAutofitLayoutManager(
        span: Int,
        orientation: Int
): StaggeredGridLayoutManager(span, orientation) {

    override fun canScrollVertically() = false
    override fun canScrollHorizontally() = false
}