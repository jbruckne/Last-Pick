package joebruckner.lastpick.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.widget.TextView
import com.appyvet.rangebar.RangeBar
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.tmdb.Genre
import joebruckner.lastpick.view.movie.adapters.GenreAdapter
import joebruckner.lastpick.utils.darkenColor
import joebruckner.lastpick.utils.find

class FilterSheetDialogBuilder(
        val context: Context,
        val filter: Filter,
        val color: Int = Color.WHITE,
        val listener: (Filter) -> Unit
) {
    fun create(): BottomSheetDialog {
        // Create sheet view
        val sheetView = LayoutInflater
                .from(context)
                .inflate(R.layout.sheet_filter, null)

        // Set up genre picker
        val switchAll = sheetView.find<SwitchCompat>(R.id.switch_all)
        switchAll.thumbTintList = ColorStateList.valueOf(color)
        switchAll.trackTintList = ColorStateList.valueOf(color.darkenColor())
        switchAll.isChecked = filter.showAll
        val recyclerView = sheetView.find<RecyclerView>(R.id.genres)
        val layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)
        val adapter = GenreAdapter(filter.genresToBooleanArray(), color) {
            switchAll.isChecked = false
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        // Set up release year range bar
        val rangeBar = sheetView.find<RangeBar>(R.id.years)
        val gteText = sheetView.find<TextView>(R.id.year_gte)
        val lteText = sheetView.find<TextView>(R.id.year_lte)
        rangeBar.setConnectingLineColor(color)
        rangeBar.setSelectorColor(color)
        gteText.text = filter.yearGte
        lteText.text = filter.yearLte
        rangeBar.setRangePinsByValue(
                filter.yearGte.toFloat(),
                filter.yearLte.toFloat()
        )
        rangeBar.setOnRangeBarChangeListener { rangeBar, l, r, lv, rv ->
                gteText.text = lv
                lteText.text = rv
        }

        // Set up filter bottom sheet dialog
        val sheet = ExpandedBottomSheetDialog(context)
        sheet.setContentView(sheetView)
        sheet.setOnDismissListener {
            val selection = Genre.getAll().filterIndexed { i, genre -> adapter.selected[i] }
            val newFilter = Filter(
                    switchAll.isChecked,
                    selection,
                    rangeBar.leftPinValue,
                    if (rangeBar.rightPinValue.toFloat() > 2020) "2020" else rangeBar.rightPinValue
            )
                listener.invoke(newFilter)
        }
        return sheet
    }
}