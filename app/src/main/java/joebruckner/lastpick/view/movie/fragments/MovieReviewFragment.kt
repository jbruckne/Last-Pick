package joebruckner.lastpick.view.movie.fragments

import android.support.v4.widget.NestedScrollView
import android.widget.Button
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.presentation.Movie
import joebruckner.lastpick.model.presentation.ReviewSource
import joebruckner.lastpick.view.common.BaseFragment
import joebruckner.lastpick.view.movie.MovieContract
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.visibleIf
import javax.inject.Inject

class MovieReviewFragment : BaseFragment(), MovieContract.Subview {
    // Parameters
    override val layoutId = R.layout.fragment_movie_review

    // Objects
    @Inject lateinit var presenter: MovieContract.Presenter

    // Views
    val rating: TextView get() = find(R.id.rating)
    val rottenTomatoes: Button get() = find(R.id.rotten_tomatoes)
    val metacritic: Button get() = find(R.id.metacritic)
    val themoviedb: Button get() = find(R.id.themoviedb)

    override fun onStart() {
        super.onStart()
        rottenTomatoes.setOnClickListener {
            presenter.onReviewSourceClicked(ReviewSource.ROTTEN_TOMATOES)
        }
        metacritic.setOnClickListener {
            presenter.onReviewSourceClicked(ReviewSource.METACRITIC)
        }
        themoviedb.setOnClickListener {
            presenter.onReviewSourceClicked(ReviewSource.THEMOVIEDB)
        }
        presenter.addSubview(this)
    }

    override fun onStop() {
        presenter.removeSubview(this)
        super.onStop()
    }

    override fun updateMovie(movie: Movie) = updateView()

    override fun updateColor(color: Int) {
        rottenTomatoes.setTextColor(color)
        metacritic.setTextColor(color)
        themoviedb.setTextColor(color)
    }

    override fun scrollToTop() {
        find<NestedScrollView>(R.id.scroll_view).scrollTo(0, 0)
    }

    fun updateView() {
        if (view == null || activity == null) return
        rating.text = "${presenter.getMovie()?.voteAverage ?: '-' }"
        rottenTomatoes.visibleIf(presenter.getMovie()?.rottenTomatoesId != null)
        metacritic.visibleIf(presenter.getMovie()?.metacriticLink != null)

        scrollToTop()
    }
}
