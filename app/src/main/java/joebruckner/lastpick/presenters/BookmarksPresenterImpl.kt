package joebruckner.lastpick.presenters

import joebruckner.lastpick.network.BookmarkManager
import joebruckner.lastpick.presenters.BookmarksPresenter.BookmarksView

class BookmarksPresenterImpl(val bookmarkManager: BookmarkManager): BookmarksPresenter {
    private var view: BookmarksView? = null

    override fun attachView(view: BookmarksView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun getBookmarks() {
        view?.showContent(bookmarkManager.getBookmarks().sortedBy {
            it.title.first().toUpperCase() - 'A'
        })
    }
}