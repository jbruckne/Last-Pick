package joebruckner.lastpick.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.presenters.MoviePresenter
import joebruckner.lastpick.presenters.MoviePresenterImpl

class NewMovieFragment : BaseFragment(), MoviePresenter.MovieView {
    override var isLoading = true;
    lateinit var presenter: MoviePresenter

    override fun showLoading() {
        isLoading = true;
        Log.d("Loading", "...")
    }

    override fun showContent(movie: Movie) {
        isLoading = false;
        Log.d("Content", movie.toString())
    }

    override fun showError(errorMessage: String) {
        isLoading = false;
        Log.e("Error", errorMessage)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_movie, container, false)
    }

    override fun onStart() {
        super.onStart()
        val bus: Bus = activity.application.getSystemService(LastPickApp.BUS) as Bus
        presenter = MoviePresenterImpl(bus)
        presenter.attachActor(this)
    }

    override fun handleAction(action: Action) {
        when (action.name) {
            Action.SHUFFLE -> presenter.shuffleMovie()
            Action.UNDO -> presenter.undoShuffle()
        }
    }
}
