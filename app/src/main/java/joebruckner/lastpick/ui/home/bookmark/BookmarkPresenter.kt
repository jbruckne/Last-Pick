package joebruckner.lastpick.ui.home.bookmark

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.interactors.BookmarkInteractor
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.utils.applySchedulers

class BookmarkPresenter(
        val movieManager: MovieInteractor,
        val bookmarkManager: BookmarkInteractor
): BookmarkContract.Presenter {
    private var view: BookmarkContract.View? = null

    override fun attachView(view: BookmarkContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun getBookmarks() {
        view?.showLoading()
        bookmarkManager
                .getBookmarks()
                .compose(applySchedulers<List<Movie>>())
                .subscribe ({
                    view?.showContent(it.sortedBy {
                        it.title.first().toUpperCase() - 'A'
                    })
                }, { error ->
                    error.printStackTrace()
                    view?.showError("Oops, there was a problem loading your bookmarks.")
                })
    }
}