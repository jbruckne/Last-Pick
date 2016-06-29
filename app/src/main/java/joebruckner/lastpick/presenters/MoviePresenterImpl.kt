package joebruckner.lastpick.presenters

import joebruckner.lastpick.data.Filter
import joebruckner.lastpick.data.Genre
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.network.BookmarkManager
import joebruckner.lastpick.network.MovieManager
import joebruckner.lastpick.presenters.MoviePresenter.MovieView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MoviePresenterImpl(
        val moviesManager: MovieManager,
        val bookmarksManager: BookmarkManager
) : MoviePresenter {
    private var view: MovieView? = null
    private var filter = Filter()

    override fun attachActor(view: MovieView) {
        this.view = view
    }

    override fun detachActor() {
        this.view = null
    }

    override fun updateMovie() {
        view?.showLoading()
        moviesManager.getNextMovie(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ movie ->
                    if (view?.isLoading ?: false) {
                        view?.showContent(movie)
                        updateBookmarkView(movie, false)
                    }
                }, { error ->
                    if (view?.isLoading ?: false) view?.showError(error.toString())
                })
    }

    override fun updateBookmark(movie: Movie, isAdding: Boolean) {
        val observable =
                if (isAdding) bookmarksManager.addBookmark(movie)
                else bookmarksManager.removeBookmark(movie)
        observable.subscribe { updateBookmarkView(movie, true) }
    }

    override fun updateFilter(selected: BooleanArray, yearLte: String, yearGte: String) {
        filter = Filter(
                Genre.getAll().filterIndexed { i, genre -> selected[i] },
                yearGte,
                yearLte
        )
    }

    override fun getSelectedGenres(): BooleanArray {
        return Genre.getAll()
                .map { filter.genres.contains(it) }
                .toBooleanArray()
    }

    override fun getLte() = filter.yearLte

    override fun getGte() = filter.yearGte

    fun updateRatingFilter() {
        //TODO
    }

    fun updateKeywordFilter() {
        //TODO
    }

    fun updateBookmarkView(movie: Movie, notify: Boolean) {
        bookmarksManager.getBookmarks().subscribe { bookmarks ->
            if (view?.getContent()?.id != movie.id) return@subscribe
            view?.showBookmarkUpdate(bookmarks.contains(movie), notify)
        }
    }
}