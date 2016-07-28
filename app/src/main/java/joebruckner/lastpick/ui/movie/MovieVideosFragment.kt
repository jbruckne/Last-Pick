package joebruckner.lastpick.ui.movie


import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.adapters.TrailerAdapter

/**
 * A simple [Fragment] subclass.
 */
class MovieVideosFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_movie_videos

    val trailerList by lazy { find<RecyclerView>(R.id.trailer_list) }

    val adapter by lazy { TrailerAdapter(activity) }
    var movie: Movie? = null

    override fun onStart() {
        super.onStart()
        trailerList.adapter = adapter
        updateView()
    }

    fun updateMovie(movie: Movie) {
        this.movie = movie
        updateView()
    }

    fun updateView() {
        if (view == null || activity == null) return
        adapter.videos = movie?.videos?.results ?: emptyList()
    }
}
