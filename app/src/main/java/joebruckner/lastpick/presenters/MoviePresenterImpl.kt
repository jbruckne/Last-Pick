package joebruckner.lastpick.presenters

import android.util.Log
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.Request
import joebruckner.lastpick.events.RequestError
import joebruckner.lastpick.presenters.MoviePresenter.MovieView

class MoviePresenterImpl(val bus: Bus) : MoviePresenter {

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
        Log.d("Movie Available", movie.toString())
        if (view?.isLoading ?: false)
            view?.showContent(movie)
    }

    @Subscribe fun errorThrown(error: RequestError) {
        if (view?.isLoading ?: false)
            view?.showError(error.message)
    }
}