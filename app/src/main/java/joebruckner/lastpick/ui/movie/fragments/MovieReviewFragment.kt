package joebruckner.lastpick.ui.movie.fragments

import android.widget.Button
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.ReviewSource
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieContract
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
            presenter.readReviews(ReviewSource.ROTTEN_TOMATOES)
        }
        metacritic.setOnClickListener {
            presenter.readReviews(ReviewSource.METACRITIC)
        }
        themoviedb.setOnClickListener {
            presenter.readReviews(ReviewSource.THEMOVIEDB)
        }
        presenter.addSubview(this)
    }

    override fun onStop() {
        presenter.removeSubview(this)
        super.onStop()
    }

    override fun updateMovie(movie: Movie) = updateView()

    override fun updateColor(color: Int) {

    }

    fun updateView() {
        if (view == null || activity == null) return
        rating.text = "${presenter.getCurrentMovie()?.voteAverage ?: '-' }/10"
        rottenTomatoes.visibleIf(presenter.getCurrentMovie()?.rottenTomatoesId != null)
        metacritic.visibleIf(presenter.getCurrentMovie()?.metacriticLink != null)
    }
}
