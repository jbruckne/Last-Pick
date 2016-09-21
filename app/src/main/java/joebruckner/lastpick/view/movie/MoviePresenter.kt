package joebruckner.lastpick.view.movie

import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.*
import joebruckner.lastpick.domain.impl.MovieInteractorImpl
import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.ReviewSource
import joebruckner.lastpick.model.State
import joebruckner.lastpick.model.guidebox.Source
import joebruckner.lastpick.model.tmdb.Video
import joebruckner.lastpick.utilities.applySchedulers
import joebruckner.lastpick.view.movie.MovieContract.View
import javax.inject.Inject

@ActivityScope
class MoviePresenter @Inject constructor(
        val movieInteractor: MovieInteractor,
        val historyInteractor: HistoryInteractor,
        val bookmarkInteractor: BookmarkInteractor,
        val navigator: FlowNavigator,
        val logger: EventLogger
) : MovieContract.Presenter {
    private var view: View? = null

    private var movie: Movie? = null
    private var id: Int? = null
    private var filter = Filter()
    private var image: String? = null
    private fun viewIsLoading(): Boolean = view?.state == State.LOADING

    override fun attachView(view: View) {
        this.view = view
        if (movie != null) view.showMovie(movie!!)
        image?.let { view.showImage(it) }
    }

    override fun detachView() {
        this.view = null
    }

    fun reloadMovie() {
        if (movie != null) {
            showMovie()
            return
        }
        movieInteractor
                .getLastMovie()
                .compose(applySchedulers<Movie>())
                .subscribe ({ movie ->
                    setMovie(movie)
                }, { error ->
                    showError(error, true)
                })
    }

    override fun onRandomRequested() {
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
        logger.logRandomViewed(filter, (movieInteractor as MovieInteractorImpl).count)
    }

    override fun onMovieSelected(id: Int) {
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

    override fun onShareSelected() {
        navigator.share(Movie.theMovieDatabaseUrl + movie?.id, "Check out this movie!")
        movie?.let { logger.logMovieShared(it) }
    }

    override fun onAddToSelected() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun onBookmarkToggled() {
        movie?.let {
            bookmarkInteractor
                    .isMovieBookmarked(it.id)
                    .compose(applySchedulers<Boolean>())
                    .subscribe { isBookmarked ->
                        if (isBookmarked) {
                            bookmarkInteractor.removeBookmark(it.id)
                            view?.showSnackbar("Bookmark removed")
                        } else {
                            bookmarkInteractor.addBookmark(it.id)
                            view?.showSnackbar("Bookmark added")
                        }
                        view?.setBookmark(isBookmarked)
                        logger.logBookmarkChange(it, !isBookmarked)
                    }
        }
    }

    override fun onSeenToggle() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun onSourceSelected(source: Source) {
        navigator.view(source.link)
        movie?.let { logger.logSourceViewed(it, source) }
    }

    override fun onReviewSelected(source: ReviewSource) {
        when (source) {
            ReviewSource.ROTTEN_TOMATOES -> {
                movie?.rottenTomatoesId?.let {
                    navigator.view(Movie.rottenTomatoesUrl + it)
                }
            }
            ReviewSource.METACRITIC -> {
                movie?.metacriticLink?.let {
                    navigator.view(it)
                }
            }
            ReviewSource.THEMOVIEDB -> {
                movie?.let {
                    navigator.view(Movie.theMovieDatabaseUrl + it.id)
                }
            }
        }
    }

    override fun onTrailerSelected(video: Video) {
        navigator.view(video.getTrailerUrl())
        movie?.let { logger.logVideoViewed(it, video) }
    }

    override fun onImageSelected(imageUrl: String) {
        this.image = imageUrl
        view?.showImage(imageUrl)
    }

    override fun onImageDismissed() {
        this.image = null
        view?.removeImage()
    }

    fun setMovie(movie: Movie) {
        logger.logMovieLoaded(movie)
        this.movie = movie
        showMovie()
    }

    private fun showMovie() {
        if (viewIsLoading()) {
            movie?.let {
                view?.showMovie(it)
                bookmarkInteractor
                        .isMovieBookmarked(it.id)
                        .compose(applySchedulers<Boolean>())
                        .subscribe { view?.setBookmark(it) }
            }
        }
    }

    private fun showError(error: Throwable, reload: Boolean = false) {
        logger.logError(error)
        if (!viewIsLoading()) return
        when (error.message) {
            MovieInteractor.OUT_OF_SUGGESTIONS -> {
                view?.showError("Oops! We've run out of movies to suggest.", "Reshow movies") {
                    movieInteractor.resetMovieSuggestions()
                    onRandomRequested()
                }
            }
            else -> {
                view?.showError("Oops! Ran into some connection issues", "Try again") {
                    if (reload == true) reloadMovie()
                    else if (id != null) onMovieSelected(id!!)
                    else onRandomRequested()
                }
            }
        }
    }

    override fun onFilterDismissed(filter: Filter) {
        this.filter = filter
        logger.logFilterChange(filter)
    }

    override fun getFilter(): Filter = filter
}