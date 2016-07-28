package joebruckner.lastpick.ui.movie

import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.utils.visibleIf
import joebruckner.lastpick.ui.movie.adapters.CreditsAdapter

class MovieInfoFragment() : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_movie_info

    val overview    by lazy { find<TextView>(R.id.overview) }
    val tagline      by lazy { find<TextView>(R.id.tagline) }
    val castList    by lazy { find<RecyclerView>(R.id.cast_list) }

    val adapter by lazy { CreditsAdapter(activity) }
    var movie: Movie? = null

    fun updateView() {
        if (view == null || activity == null) return
        overview.text = movie?.overview
        tagline.visibleIf(!movie?.tagline.isNullOrBlank())
        tagline.text = movie?.tagline
        adapter.credits = movie?.credits
        scrollToTop()
    }

    fun updateMovie(movie: Movie) {
        this.movie = movie
        updateView()
    }

    fun scrollToTop() {
        view?.let {
            it.find<NestedScrollView>(R.id.nested_scroll_view).scrollTo(0, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        castList.adapter = adapter
        updateView()
    }
}