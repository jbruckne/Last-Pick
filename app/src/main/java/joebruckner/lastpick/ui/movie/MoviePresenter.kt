package joebruckner.lastpick.ui.movie

import joebruckner.lastpick.data.Filter
import joebruckner.lastpick.data.Genre
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.State
import joebruckner.lastpick.network.BookmarkManager
import joebruckner.lastpick.network.MovieManager
import joebruckner.lastpick.ui.movie.MovieContract.View
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MoviePresenter(
        val moviesManager: MovieManager,
        val bookmarksManager: BookmarkManager
) : MovieContract.Presenter {
    private var view: View? = null
    private var movie: Movie? = null
    private var id: Int? = null
    private var filter = Filter()

    private fun checkViewIsLoading(): Boolean = view?.state == State.LOADING

    override fun attachView(view: View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun reloadMovie() {
        moviesManager.getCachedMovie()?.let { setMovie(it) }
        ?: if (checkViewIsLoading())
            view?.showError("Oops! Ran into some connection issues", "Try again") {
                reloadMovie()
            }
    }

    override fun getNextMovie() {
        this.id = null
        view?.showLoading()
        moviesManager.getNextMovie(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ movie ->
                    setMovie(movie)
                }, { error ->
                    showError(error)
                })
    }

    override fun getMovieById(id: Int) {
        this.id = id
        view?.showLoading()
        moviesManager.getMovie(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ movie ->
                    setMovie(movie)
                }, { error ->
                    showError(error)
                })
    }

    override fun setMovie(movie: Movie) {
        this.movie = movie
        showMovie()
    }

    private fun showMovie() {
        if (checkViewIsLoading()) {
            view?.showContent(movie!!)
            updateBookmarkView(movie!!, false)
        }
    }

    private fun showError(error: Throwable) {
        error.printStackTrace()
        if (!checkViewIsLoading()) return
        when (error.message) {
            MovieManager.OUT_OF_MOVIES -> {
                view?.showError("Oops! We've run out of movies to suggest.", "Reshow movies") {
                    moviesManager.reuseMovies()
                    getNextMovie()
                }
            }
            else -> {
                view?.showError("Oops! Ran into some connection issues", "Try again") {
                    id?.let { getMovieById(it) } ?: getNextMovie()
                }
            }
        }
    }

    override fun updateBookmark() {
        if (movie == null) return
        val observable =
                if (!bookmarksManager.isBookmarked(movie!!))
                    bookmarksManager.addBookmark(movie!!)
                else
                    bookmarksManager.removeBookmark(movie!!)
        observable.subscribe { updateBookmarkView(movie!!, true) }
    }

    override fun updateFilter(selected: BooleanArray, yearGte: String, yearLte: String) {
        filter = Filter(
                Genre.Companion.getAll().filterIndexed { i, genre -> selected[i] },
                yearGte,
                yearLte
        )
    }

    override fun getSelectedGenres(): BooleanArray {
        return Genre.getAll()
                .map { filter.genres.contains(it) }
                .toBooleanArray()
    }

    override fun getBookmarkStatus() = bookmarksManager.isBookmarked(movie!!)

    override fun getCurrentMovie() = movie

    override fun getLte() = filter.yearLte

    override fun getGte() = filter.yearGte

    fun updateRatingFilter() {
        //TODO
    }

    fun updateKeywordFilter() {
        //TODO
    }

    fun updateBookmarkView(movie: Movie, notify: Boolean) {
        if (this.movie?.id != movie.id) return
        view?.showBookmarkUpdate(bookmarksManager.isBookmarked(movie), notify)
    }
}