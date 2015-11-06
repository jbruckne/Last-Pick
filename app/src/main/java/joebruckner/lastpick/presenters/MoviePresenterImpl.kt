package joebruckner.lastpick.presenters

import android.util.Log
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Request
import joebruckner.lastpick.data.Error
import joebruckner.lastpick.presenters.MoviePresenter.MovieView

class MoviePresenterImpl(val bus: Bus): MoviePresenter {

	var view: MovieView? = null

    override fun attachActor(view: MovieView) {
        this.view = view
        bus.register(this)
    }

    override fun detachActor() {
        this.view = null
        bus.unregister(this)
    }

    override fun shuffleMovie() {
        view?.showLoading()
        bus.post(Request())
    }

	override fun undoShuffle() {
		throw UnsupportedOperationException()
	}

    @Subscribe fun movieAvailable(movie: Movie) {
        if (view?.isLoading() ?: false)
            view?.showContent(movie)
        else // TODO cache

        Log.d("Movie", movie.toString())
    }

    @Subscribe fun errorThrown(error: Error) {
        if (view?.isLoading() ?: false)
            view?.showError(error.message)

        Log.e("Error", error.toString())
    }
}