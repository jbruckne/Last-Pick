package joebruckner.lastpick.presenters

import android.util.Log
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.MovieEvent
import joebruckner.lastpick.events.MovieRequest
import joebruckner.lastpick.events.RequestError
import joebruckner.lastpick.network.MovieManager
import joebruckner.lastpick.presenters.MoviePresenter.MovieView

class MoviePresenterImpl(val bus: Bus) : MoviePresenter {

    var view: MovieView? = null

    override fun attachActor(view: MovieView) {
        if (this.view == null) bus.register(this)
        this.view = view
    }

    override fun detachActor() {
        bus.unregister(this)
        this.view = null
    }

    override fun shuffleMovie() {
        view?.showLoading()
        bus.post(MovieEvent())
    }

    override fun undoShuffle() {
        throw UnsupportedOperationException()
    }

    @Subscribe fun movieAvailable(movie: Movie) {
        if (view?.isLoading ?: false)
            view?.showContent(movie)
    }

    @Subscribe fun errorThrown(error: RequestError) {
        if (view?.isLoading ?: false)
            view?.showError(error.message)
    }
}