package joebruckner.lastpick.ui.movie.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.inflate
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class MovieAdapter @Inject constructor(
        @Named("Activity") val context: Context
): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    var movies = listOf<Movie>()
    var onItemClickListener: ((Int) -> Unit)? = null

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder? {
        val view = parent.inflate(R.layout.card_movie_list)
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
            onItemClickListener?.invoke(position)
        }
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
