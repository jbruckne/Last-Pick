package joebruckner.lastpick.ui.movie.fragments

import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieContract
import joebruckner.lastpick.ui.movie.adapters.CreditsAdapter
import joebruckner.lastpick.ui.movie.adapters.SourceAdapter
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.visibleIf
import javax.inject.Inject

class MovieInfoFragment() : BaseFragment(), MovieContract.Subview {
    // Parameters
    override val layoutId: Int = R.layout.fragment_movie_info

    // Objects
    @Inject lateinit var presenter: MovieContract.Presenter
    val creditsAdapter by lazy { CreditsAdapter(activity) }
    val sourceAdapter by lazy { SourceAdapter(activity) }

    // Views
    val overview: TextView get() = find(R.id.overview)
    val tagline: TextView get() = find(R.id.tagline)
    val castList: RecyclerView get() = find(R.id.cast_list)
    val sourceList: RecyclerView get() = find(R.id.source_list)

    fun updateView() {
        if (view == null || activity == null) return
        presenter.getCurrentMovie()?.let {
            overview.text = it.overview
            tagline.visibleIf(!it.tagline.isNullOrBlank())
            tagline.text = it.tagline
            creditsAdapter.credits = it.credits
            sourceAdapter.setNewItems(it.sources)
        }
        scrollToTop()
    }

    override fun updateMovie(movie: Movie) {
        updateView()
    }

    override fun updateColor(color: Int) {

    }

    fun scrollToTop() {
        view?.let {
            it.find<NestedScrollView>(R.id.nested_scroll_view).scrollTo(0, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Info", "Started")
        sourceAdapter.listener = { presenter.viewSource(it) }
        castList.adapter = creditsAdapter
        sourceList.adapter = sourceAdapter
        presenter.addSubview(this)
    }

    override fun onStop() {
        presenter.removeSubview(this)
        super.onStop()
    }
}