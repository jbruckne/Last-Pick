package joebruckner.lastpick.presenters

import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.*
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

    override fun updateMovie(filter: List<Genre>?) {
        view?.showLoading()
        bus.post(MovieRequest(filter))
    }

    override fun updateBookmark(movie: Movie, isAdding: Boolean) {
        bus.post(BookmarkUpdateRequest(movie, isAdding))
    }

    @Subscribe fun bookmarkChanged(event: BookmarkUpdateEvent) {
        val id = view?.getContent()?.id
        if (id != event.movie.id) return
        if (event.isSuccess) view?.showBookmarkUpdate(event.movie.isBookmarked)
        else view?.showBookmarkError(event.movie.isBookmarked)
    }

    @Subscribe fun onNewMovie(event: MovieEvent) {
        if (view?.isLoading ?: false)
            view?.showContent(event.movie)
    }

    @Subscribe fun errorThrown(event: ErrorEvent) {
        if (view?.isLoading ?: false)
            view?.showError(event.message)
    }
}