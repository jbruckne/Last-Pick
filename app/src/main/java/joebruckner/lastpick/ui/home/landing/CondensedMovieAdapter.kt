package joebruckner.lastpick.ui.home.landing

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.tmdb.CondensedMovie
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.inflate
import joebruckner.lastpick.utils.load
import java.util.*

class CondensedMovieAdapter(val context: Context):
        RecyclerView.Adapter<CondensedMovieAdapter.CondensedMovieViewHolder>() {
    var movies: List<CondensedMovie> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var clickListeners = ArrayList<(Int) -> Unit>()

    override fun onBindViewHolder(holder: CondensedMovieViewHolder, position: Int) {
        holder.title.text = movies[position].title
        holder.poster.load(context, movies[position].getFullPosterPath())
        holder.view.setOnClickListener {
            clickListeners.forEach { it(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CondensedMovieViewHolder {
        val view = parent.inflate(R.layout.card_movie_slide)
        return CondensedMovieViewHolder(view)
    }

    override fun getItemCount() = movies.size

    fun addOnItemClickListener(listener: (Int) -> Unit) {
        clickListeners.add(listener)
    }

    class CondensedMovieViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val title = view.find<TextView>(R.id.title)
        val poster = view.find<ImageView>(R.id.poster)
    }
}