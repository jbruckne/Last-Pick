package joebruckner.lastpick.ui.movie.adapters

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
import joebruckner.lastpick.utils.find
import java.util.*

class MovieAdapter(
        val context: Context,
        val layout: Int
): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    var movies = listOf<Movie>()
    var onItemClickListeners = ArrayList<(Int) -> Unit>()

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder? {
        val view = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.year?.text = movies[position].releaseDate.substring(0..3)
        holder.title.text = movies[position].title
        var genreText = ""
        movies[position].genres.forEachIndexed { i, genre ->
            if (i < 3) genreText += "${genre.name}, "
        }
        if (genreText.isNotBlank()) genreText = genreText.dropLast(2)
        holder.genres?.text = genreText
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
        val title = view.find<TextView>(R.id.title)
        val genres: TextView? = view.find<TextView>(R.id.genres)
        val year: TextView? = view.find<TextView>(R.id.year)
        val poster = view.find<ImageView>(R.id.poster)
    }
}
