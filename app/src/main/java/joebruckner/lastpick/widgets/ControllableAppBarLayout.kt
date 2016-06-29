/**
 * Copyright 2015 Bartosz Lipinski
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package joebruckner.lastpick.widgets

import android.content.Context
import android.graphics.Canvas
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet

import java.lang.ref.WeakReference

class ControllableAppBarLayout : AppBarLayout {
    private var mBehavior: AppBarLayout.Behavior? = null
    private var mParent: WeakReference<CoordinatorLayout>? = null
    private var mQueuedChange = ToolbarChange.NONE
    private var mAfterFirstDraw = false

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (layoutParams !is CoordinatorLayout.LayoutParams || parent !is CoordinatorLayout) {
            throw IllegalStateException("ControllableAppBarLayout must be a direct child of CoordinatorLayout.")
        } else {
            mParent = WeakReference(parent as CoordinatorLayout)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mBehavior == null) {
            mBehavior = (layoutParams as CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (r - l > 0 && b - t > 0 && mAfterFirstDraw && mQueuedChange != ToolbarChange.NONE) {
            analyzeQueuedChange()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!mAfterFirstDraw) {
            mAfterFirstDraw = true
            if (mQueuedChange != ToolbarChange.NONE) {
                analyzeQueuedChange()
            }
        }
    }

    @Synchronized private fun analyzeQueuedChange() {
        when (mQueuedChange) {
            ControllableAppBarLayout.ToolbarChange.COLLAPSE -> performCollapsingWithoutAnimation()
            ControllableAppBarLayout.ToolbarChange.COLLAPSE_WITH_ANIMATION -> performCollapsingWithAnimation()
            ControllableAppBarLayout.ToolbarChange.EXPAND -> performExpandingWithoutAnimation()
            ControllableAppBarLayout.ToolbarChange.EXPAND_WITH_ANIMATION -> performExpandingWithAnimation()
        }

        mQueuedChange = ToolbarChange.NONE
    }

    @JvmOverloads fun collapseToolbar(withAnimation: Boolean = false) {
        mQueuedChange = if (withAnimation) ToolbarChange.COLLAPSE_WITH_ANIMATION else ToolbarChange.COLLAPSE
        requestLayout()
    }

    @JvmOverloads fun expandToolbar(withAnimation: Boolean = false) {
        mQueuedChange = if (withAnimation) ToolbarChange.EXPAND_WITH_ANIMATION else ToolbarChange.EXPAND
        requestLayout()
    }

    private fun performCollapsingWithoutAnimation() {
        if (mParent!!.get() != null) {
            mBehavior!!.onNestedPreScroll(mParent!!.get(), this, null, 0, height, intArrayOf(0, 0))
        }
    }

    private fun performCollapsingWithAnimation() {
        if (mParent!!.get() != null) {
            mBehavior!!.onNestedFling(mParent!!.get(), this, null, 0f, height.toFloat(), true)
        }
    }

    private fun performExpandingWithoutAnimation() {
        if (mParent!!.get() != null) {
            mBehavior!!.topAndBottomOffset = 0
        }
    }

    private fun performExpandingWithAnimation() {
        if (mParent!!.get() != null) {
            mBehavior!!.onNestedFling(mParent!!.get(), this, null, 0f, (-height * 5).toFloat(), false)
        }
    }

    private enum class ToolbarChange {
        COLLAPSE,
        COLLAPSE_WITH_ANIMATION,
        EXPAND,
        EXPAND_WITH_ANIMATION,
        NONE
    }

}