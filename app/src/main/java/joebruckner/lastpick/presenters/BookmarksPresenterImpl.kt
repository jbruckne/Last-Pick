package joebruckner.lastpick.presenters

import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.events.BookmarkedMoviesEvent
import joebruckner.lastpick.events.BookmarkedMoviesRequest
import joebruckner.lastpick.presenters.BookmarksPresenter.BookmarksView

class BookmarksPresenterImpl(val bus: Bus): BookmarksPresenter {
    var view: BookmarksView? = null

    override fun attachView(view: BookmarksView) {
        this.view = view
        bus.register(this)
    }

    override fun detachView() {
        bus.unregister(this)
        this.view = null
    }

    override fun getBookmarks() {
        bus.post(BookmarkedMoviesRequest())
    }

    @Subscribe fun newBookmarkedMovies(event: BookmarkedMoviesEvent) {
        view?.showContent(event.bookmarked)
    }
}