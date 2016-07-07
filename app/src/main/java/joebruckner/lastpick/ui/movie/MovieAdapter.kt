package joebruckner.lastpick.ui.movie

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import java.util.*

class MovieAdapter(val context: Context): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    var movies = listOf<Movie>()
    var onItemClickListeners = ArrayList<(Int) -> Unit>()

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder? {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_simple_recent, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.title.text = movies[position].title
        Glide.with(context)
                .load(movies[position].getFullPosterPath())
                .crossFade()
                .into(holder.poster)
        holder.view.setOnClickListener {
            onItemClickListeners.forEach { it.invoke(position) }
        }
    }

    fun addOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListeners.add(listener)
    }

    fun setNewMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    class MovieViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val title = view.findViewById(R.id.title) as TextView
        val poster = view.findViewById(R.id.poster) as ImageView
    }
}
