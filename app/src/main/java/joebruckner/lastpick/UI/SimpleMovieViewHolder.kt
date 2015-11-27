package joebruckner.lastpick.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class SimpleMovieViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val title = view.findViewById(android.R.id.text1) as TextView
}