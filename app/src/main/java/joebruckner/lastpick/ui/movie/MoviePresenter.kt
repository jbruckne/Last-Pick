package joebruckner.lastpick.ui.movie

import joebruckner.lastpick.data.Filter
import joebruckner.lastpick.data.Genre
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.State
import joebruckner.lastpick.interactors.BookmarkInteractor
import joebruckner.lastpick.interactors.HistoryInteractor
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.ui.movie.MovieContract.View
import joebruckner.lastpick.utils.applySchedulers

class MoviePresenter(
        val movieInteractor: MovieInteractor,
        val historyInteractor: HistoryInteractor,
        val bookmarkInteractor: BookmarkInteractor
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
        movieInteractor
                .getLastMovie()
                .compose(applySchedulers<Movie>())
                .subscribe ({ movie ->
                    setMovie(movie)
                }, { error ->
                    showError(error, true)
                })
    }

    override fun getNextMovie() {
        this.id = null
        view?.showLoading()
        movieInteractor
                .getMovieSuggestion(filter)
                .doOnNext { historyInteractor.addMovieToHistory(it) }
                .compose(applySchedulers<Movie>())
                .subscribe ({ movie ->
                    setMovie(movie)
                }, { error ->
                    showError(error)
                })
    }

    override fun getMovieById(id: Int) {
        this.id = id
        view?.showLoading()
        movieInteractor
                .getMovie(id)
                .doOnNext { historyInteractor.addMovieToHistory(it) }
                .compose(applySchedulers<Movie>())
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

    private fun showError(error: Throwable, reload: Boolean = false) {
        error.printStackTrace()
        if (!checkViewIsLoading()) return
        when (error.message) {
            MovieInteractor.OUT_OF_SUGGESTIONS -> {
                view?.showError("Oops! We've run out of movies to suggest.", "Reshow movies") {
                    movieInteractor.resetMovieSuggestions()
                    getNextMovie()
                }
            }
            else -> {
                view?.showError("Oops! Ran into some connection issues", "Try again") {
                    if (reload == true) reloadMovie()
                    else if (id != null) getMovieById(id!!)
                    else getNextMovie()
                }
            }
        }
    }

    override fun updateBookmark() {
        if (movie == null) return
        if (!bookmarkInteractor.isMovieBookmarked(movie!!))
            bookmarkInteractor.addBookmark(movie!!)
        else
            bookmarkInteractor.removeBookmark(movie!!)
        updateBookmarkView(movie!!, true)
    }

    override fun updateFilter(showAll: Boolean, selected: BooleanArray,
                              yearGte: String, yearLte: String) {

        val selection = Genre.getAll().filterIndexed { i, genre -> selected[i] }
        filter = Filter(
                showAll,
                selection,
                yearGte,
                yearLte
        )
    }

    override fun isShowingAll(): Boolean = filter.showAll

    override fun getSelectedGenres(): BooleanArray {
        return Genre.getAll()
                .map { filter.genres.contains(it) }
                .toBooleanArray()
    }

    override fun getBookmarkStatus() = bookmarkInteractor.isMovieBookmarked(movie!!)

    override fun getCurrentMovie() = movie

    override fun getLte() = if (filter.yearLte.toFloat() > 2020) "2020" else filter.yearLte

    override fun getGte() = filter.yearGte

    fun updateBookmarkView(movie: Movie, notify: Boolean) {
        if (this.movie?.id != movie.id) return
        view?.showBookmarkUpdate(bookmarkInteractor.isMovieBookmarked(movie), notify)
    }
}