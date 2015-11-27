package joebruckner.lastpick.ui.home

import android.support.v7.graphics.Palette
import android.util.Log
import android.view.View
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.presenters.MoviePresenter
import joebruckner.lastpick.presenters.MoviePresenterImpl
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.MovieViewHolder
import joebruckner.lastpick.ui.common.DetailActivity
import kotlinx.android.synthetic.fragment_movie.content
import kotlinx.android.synthetic.fragment_movie.error
import kotlinx.android.synthetic.fragment_movie.loading

class MovieFragment : BaseFragment(), MoviePresenter.MovieView {
    override val layoutId = R.layout.fragment_movie
    override var isLoading = true
    lateinit var presenter: MoviePresenter
    lateinit var holder: MovieViewHolder
    lateinit var detailParent: DetailActivity

    override fun showLoading() {
        isLoading = true
        detailParent.disableFab()
        detailParent.clearBackdrop()
        detailParent.clearPoster()
        detailParent.setTitle(" ")
        updateViews(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
        Log.d("Loading", "...")
    }

    override fun showContent(movie: Movie) {
        isLoading = false
        detailParent.enableFab()
        holder.movie = movie
        detailParent.setTitle(movie.title)
        detailParent.setBackdrop(movie.fullBackdropPath())
        detailParent.setPoster(movie.fullPosterPath())
        updateViews(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
        Log.d("Content", movie.toString())
    }

    override fun showError(errorMessage: String) {
        isLoading = false
        detailParent.enableFab()
        detailParent.clearBackdrop()
        detailParent.clearPoster()
        detailParent.setTitle(" ")
        error.text = errorMessage
        updateViews(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
        Log.e("Error", errorMessage)
    }

    private fun updateViews(contentState: Int, loadingState:Int, errorState: Int) {
        content.visibility = contentState
        loading.visibility = loadingState
        error.visibility   = errorState
    }

    override fun onStart() {
        super.onStart()
        detailParent = parent as DetailActivity
        holder = MovieViewHolder(view)
        val bus = parent.application.getSystemService(LastPickApp.BUS) as Bus
        presenter = MoviePresenterImpl(bus)
        presenter.attachActor(this)
        presenter.shuffleMovie()
    }

    override fun handleAction(action: Action) {
        when (action.name) {
            Action.SHUFFLE -> presenter.shuffleMovie()
            Action.UNDO -> presenter.undoShuffle()
        }
    }
}