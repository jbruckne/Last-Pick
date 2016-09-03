package joebruckner.lastpick.view.movie.fragments

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.view.common.BaseFragment
import joebruckner.lastpick.view.movie.MovieContract
import joebruckner.lastpick.view.movie.adapters.CreditsAdapter
import joebruckner.lastpick.view.movie.adapters.SourceAdapter
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.visibleIf
import javax.inject.Inject

class MovieInfoFragment() : BaseFragment(), MovieContract.Subview {
    // Parameters
    override val layoutId: Int = R.layout.fragment_movie_info
    var showFullSummary = false

    // Objects
    @Inject lateinit var presenter: MovieContract.Presenter
    @Inject lateinit var creditsAdapter: CreditsAdapter
    @Inject lateinit var sourceAdapter: SourceAdapter

    // Views
    val overview: TextView get() = find(R.id.overview)
    val tagline: TextView get() = find(R.id.tagline)
    val castList: RecyclerView get() = find(R.id.cast_list)
    val sourceList: RecyclerView get() = find(R.id.source_list)
    val sourceTitle: TextView get() = find(R.id.source_title)
    val viewMoreSources: Button get() = find(R.id.view_more_sources)
    val viewMoreSummary: Button get() = find(R.id.view_more_summary)
    val scrollView: NestedScrollView get() = find(R.id.scroll_view)

    fun updateView() {
        if (view == null || activity == null) return
        presenter.getMovie()?.let {
            overview.text = "   ${it.overview.substring(0..Math.min(200, it.overview.length-1))}"
            tagline.text = it.tagline
            tagline.visibleIf(!it.tagline.isNullOrBlank())
            creditsAdapter.credits = it.credits
            sourceAdapter.setNewItems(it.sources)
            sourceTitle.visibleIf(it.sources.size > 0)
            viewMoreSources.visibleIf(it.sources.size > 4)
        }
    }

    override fun updateMovie(movie: Movie) {
        updateView()
    }

    override fun updateColor(color: Int) {
        viewMoreSources.setTextColor(color)
        viewMoreSummary.setTextColor(color)
    }

    override fun scrollToTop() {
        scrollView.scrollTo(0, 0)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        castList.adapter = creditsAdapter
        sourceList.adapter = sourceAdapter
        sourceAdapter.listener = { presenter.onSourceClicked(it) }
        viewMoreSources.setOnClickListener {
          sourceAdapter.showAll = !sourceAdapter.showAll
          viewMoreSources.text = if (sourceAdapter.showAll) "View Less" else "View More"
        }
        viewMoreSummary.setOnClickListener {
            showFullSummary = !showFullSummary
            val full = presenter.getMovie()?.overview ?: ""
            val text = if (showFullSummary || full.length <= 200) full else full.substring(0..200)
            overview.text = "   $text"
            viewMoreSummary.text = if (showFullSummary) "View Less" else "View More"
        }
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