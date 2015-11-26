package joebruckner.lastpick.ui

import android.util.Log
import android.view.View
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.presenters.MoviePresenter
import joebruckner.lastpick.presenters.MoviePresenterImpl
import kotlinx.android.synthetic.fragment_new_movie.content
import kotlinx.android.synthetic.fragment_new_movie.error
import kotlinx.android.synthetic.fragment_new_movie.loading

class NewMovieFragment : BaseFragment(), MoviePresenter.MovieView {
    override val layoutId = R.layout.fragment_new_movie
    override var isLoading = true
    lateinit var presenter: MoviePresenter
    lateinit var holder: MovieViewHolder

    override fun showLoading() {
        isLoading = true
        parent.disableFab()
        parent.clearBackdrop()
        parent.clearPoster()
        parent.setTitle(" ")
        updateViews(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
        Log.d("Loading", "...")
    }

    override fun showContent(movie: Movie) {
        isLoading = false
        parent.enableFab()
        holder.movie = movie
        parent.setTitle(movie.title)
        parent.setBackdrop(movie.fullBackdropPath())
        parent.setPoster(movie.fullPosterPath())
        updateViews(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
        Log.d("Content", movie.toString())
    }

    override fun showError(errorMessage: String) {
        isLoading = false
        parent.enableFab()
        parent.clearBackdrop()
        parent.clearPoster()
        parent.setTitle(" ")
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
        holder = MovieViewHolder(view)
        val bus: Bus = parent.application.getSystemService(LastPickApp.BUS) as Bus
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