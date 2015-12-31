package joebruckner.lastpick.presenters

import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.MovieEvent
import joebruckner.lastpick.events.RequestError
import joebruckner.lastpick.presenters.MoviePresenter.MovieView

class MoviePresenterImpl(val bus: Bus) : MoviePresenter {

    var view: MovieView? = null
    var registered = false

    override fun attachActor(view: MovieView) {
        this.view = view
        if (!registered) bus.register(this)
        registered = true
    }

    override fun detachActor() {
        bus.unregister(this)
        this.view = null
        registered = false
    }

    override fun updateMovie() {
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