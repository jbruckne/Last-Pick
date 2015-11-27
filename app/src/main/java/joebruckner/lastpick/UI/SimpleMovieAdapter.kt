package joebruckner.lastpick.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import joebruckner.lastpick.data.Movie
import java.util.*

class SimpleMovieAdapter(): RecyclerView.Adapter<SimpleMovieViewHolder>() {
    var movies = listOf<Movie>()

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleMovieViewHolder? {
        val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        return SimpleMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleMovieViewHolder, position: Int) {
        holder.title.text = movies[position].title
    }

    public fun setNewMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }
}
