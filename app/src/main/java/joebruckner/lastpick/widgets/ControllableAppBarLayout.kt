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
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import java.lang.ref.WeakReference

class ControllableAppBarLayout : AppBarLayout {
    private var mBehavior: AppBarLayout.Behavior? = null
    private var mParent: WeakReference<CoordinatorLayout>? = null

    var isCollapsed: Boolean = true
    var isFullyExpanded: Boolean = false

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
        addOnOffsetChangedListener { appBarLayout, i ->
            val collapsedPercentage = Math.abs(i) / totalScrollRange.toFloat()
            isCollapsed = collapsedPercentage >= 0.8
            isFullyExpanded = collapsedPercentage <= 0.3
        }
    }


}