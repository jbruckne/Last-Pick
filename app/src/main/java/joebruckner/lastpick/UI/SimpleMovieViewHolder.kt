package joebruckner.lastpick.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import joebruckner.lastpick.R

class SimpleMovieViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val title = view.findViewById(R.id.title) as TextView
    val poster = view.findViewById(R.id.poster) as ImageView
}