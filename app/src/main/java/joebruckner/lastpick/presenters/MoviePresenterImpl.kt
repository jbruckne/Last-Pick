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
    private var movie: Movie? = null
    private var id: Int? = null
    private var filter = Filter()

    override fun attachActor(view: MovieView) {
        this.view = view
    }

    override fun detachActor() {
        this.view = null
    }

    override fun reloadMovie() {
        moviesManager.getCachedMovie()?.let { setMovie(it) }
        ?: if (view?.isLoading ?: false) view?.showError("Failed to reload movie")
    }

    override fun getNextMovie() {
        this.id = null
        view?.showLoading()
        moviesManager.getNextMovie(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ m ->
                    setMovie(m)
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
                .subscribe ({ m ->
                    setMovie(m)
                }, { error ->
                    showError(error)
                })
    }

    override fun setMovie(movie: Movie) {
        this.movie = movie
        showMovie()
    }

    private fun showMovie() {
        if (view?.isLoading ?: false) {
            view?.showContent(movie!!)
            updateBookmarkView(movie!!, false)
        }
    }

    private fun showError(error: Throwable) {
        error.printStackTrace()
        if (!(view?.isLoading ?: false)) return
        when (error.message) {
            MovieManager.OUT_OF_MOVIES -> {
                view?.showError("Oops! We've run out of movies to suggest.")
                view?.showMovieErrorButton("Reshow movies") {
                    moviesManager.reuseMovies()
                    getNextMovie()
                }
            }
            else -> {
                view?.showError("Oops! We were not able to get the last movie.")
                view?.showMovieErrorButton("Retry") {
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