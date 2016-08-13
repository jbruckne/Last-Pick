package joebruckner.lastpick.ui.movie.fragments

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
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
    @Inject lateinit var creditsAdapter: CreditsAdapter
    @Inject lateinit var sourceAdapter: SourceAdapter

    // Views
    val overview: TextView get() = find(R.id.overview)
    val tagline: TextView get() = find(R.id.tagline)
    val castList: RecyclerView get() = find(R.id.cast_list)
    val sourceList: RecyclerView get() = find(R.id.source_list)

    fun updateView() {
        if (view == null || activity == null) return
        presenter.getMovie()?.let {
            overview.text = "   ${it.overview}"
            tagline.text = it.tagline
            tagline.visibleIf(!it.tagline.isNullOrBlank())
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

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        castList.adapter = creditsAdapter
        sourceList.adapter = sourceAdapter
        sourceAdapter.listener = { presenter.onSourceClicked(it) }
    }

    override fun onStart() {
        super.onStart()
        presenter.addSubview(this)
    }

    override fun onStop() {
        presenter.removeSubview(this)
        super.onStop()
    }
}