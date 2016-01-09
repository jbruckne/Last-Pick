package joebruckner.lastpick.ui

import android.view.View
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Credits
import joebruckner.lastpick.data.Movie

class MovieViewHolder(view: View) {
    //val title: TextView
    val summary: TextView
    val cast: TextView
    val year: TextView
    val mpaa: TextView
    val vote: TextView
    val length: TextView
    var movie: Movie? = null
        set(m: Movie?) {
           field = m
           summary.text    = m?.overview
           cast.text       = formatCredits(m?.credits)
           year.text       = m?.releaseDate?.substring(0,4)
           mpaa.text       = m?.getSimpleMpaa()
           vote.text       = formatVote(m?.voteAverage ?: 0.0)
           length.text     = formatLength(m?.runtime ?: 0)
        }

    init {
        //title   = view.findViewById(R.id.title)     as TextView
        summary = view.findViewById(R.id.overview)  as TextView
        cast    = view.findViewById(R.id.cast)      as TextView
        year    = view.findViewById(R.id.year)      as TextView
        mpaa  = view.findViewById(R.id.mpaa)        as TextView
        vote = view.findViewById(R.id.popularity)   as TextView
        length  = view.findViewById(R.id.runtime)   as TextView
    }

    fun formatCredits(credits: Credits?): String {
        if (credits == null) return ""

        val text = StringBuilder()
        val director = credits.getDirector()
        if (director != null)
            text.appendln("Directed by ${director.name}")
        for (i in 0..4) {
            if (credits.cast.size > i)
                text.append(
                        "\n${credits.cast[i].name}" +
                        " as " +
                        "${credits.cast[i].firstCharacter()}"
                )
        }
        return text.toString()
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