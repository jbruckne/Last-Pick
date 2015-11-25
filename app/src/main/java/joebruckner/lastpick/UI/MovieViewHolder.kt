package joebruckner.lastpick.ui

import android.view.View
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie

class MovieViewHolder(view: View) {
    //val title: TextView
    val summary: TextView
    val cast: TextView
    val year: TextView
    //val rating: TextView
    val vote: TextView
    val length: TextView
    var movie: Movie? = null
        set(m: Movie?) {
           //title.text = "Hello"
           summary.text    = m?.overview
           cast.text       = m?.credits?.toString()
           year.text       = m?.releaseDate?.substring(0,4)
           //rating.text     = m?.voteAverage.toString()
           vote.text       = formatVote(m?.voteAverage ?: 0.0)
           length.text     = formatLength(m?.runtime ?: 0)
        }

    init {
        //title   = view.findViewById(R.id.title)     as TextView
        summary = view.findViewById(R.id.overview)  as TextView
        cast    = view.findViewById(R.id.cast)      as TextView
        year    = view.findViewById(R.id.year)      as TextView
        //rating  = view.findViewById(R.id.rating)    as TextView
        vote = view.findViewById(R.id.popularity) as TextView
        length  = view.findViewById(R.id.runtime)   as TextView
    }

    fun formatLength(runtime: Int): String {
        val hours = runtime / 60
        val minutes = runtime % 60
        val text = StringBuilder()
        text.append(hours)
        text.append(":")
        if (minutes < 10) text.append("0")
        text.append(minutes)
        return text.toString()
    }

    fun formatVote(vote: Double): String {
        return vote.toString()
    }
}