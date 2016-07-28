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
import joebruckner.lastpick.data.CondensedMovie
import joebruckner.lastpick.data.Genre
import joebruckner.lastpick.utils.find
import java.util.*

class CondensedMovieAdapter(
        val context: Context,
        val layout: Int
): RecyclerView.Adapter<CondensedMovieAdapter.CondensedMovieViewHolder>() {
    var movies = listOf<CondensedMovie>()
    var onItemClickListeners = ArrayList<(Int) -> Unit>()

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CondensedMovieViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        return CondensedMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: CondensedMovieViewHolder, position: Int) {
        holder.year?.text = movies[position].releaseDate.substring(0..3)
        holder.title.text = movies[position].title
        var genreText = ""
        movies[position].genreIds.forEachIndexed { i, id ->
            if (i < 3) genreText += "${Genre.getGenre(id)?.name}, "
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

    fun setNewMovies(movies: List<CondensedMovie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    class CondensedMovieViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val title = view.find<TextView>(R.id.title)
        val genres: TextView? = view.find<TextView>(R.id.genres)
        val year: TextView? = view.find<TextView>(R.id.year)
        val poster = view.find<ImageView>(R.id.poster)
    }
}