package joebruckner.lastpick.widgets

import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import joebruckner.lastpick.R.id.design_bottom_sheet

class ExpandedBottomSheetDialog : BottomSheetDialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)

    constructor(context: Context,
                cancelable: Boolean,
                cancelListener: DialogInterface.OnCancelListener
    ) : super(context, cancelable, cancelListener)

    override fun show() {
        super.show()
        val sheetView = findViewById(design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(sheetView!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}