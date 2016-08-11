package joebruckner.lastpick.ui.movie

import android.graphics.Color
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.*
import joebruckner.lastpick.domain.impl.MovieInteractorImpl
import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.ReviewSource
import joebruckner.lastpick.model.State
import joebruckner.lastpick.model.guidebox.Source
import joebruckner.lastpick.model.tmdb.Video
import joebruckner.lastpick.ui.movie.MovieContract.Subview
import joebruckner.lastpick.ui.movie.MovieContract.View
import joebruckner.lastpick.utils.applySchedulers
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
    private val subviews: MutableList<Subview> = mutableListOf()
    private var movie: Movie? = null
    private var color: Int? = null
    private var id: Int? = null
    private var filter = Filter()
    private var image: String? = null

    private fun viewIsLoading(): Boolean = view?.state == State.LOADING

    override fun attachView(view: View) {
        this.view = view
        image?.let { view.showImage(it) }
    }

    override fun detachView() {
        this.view = null
    }

    override fun addSubview(subview: Subview) {
        subviews.add(subview)
        movie?.let { subview.updateMovie(it) }
        color?.let { subview.updateColor(it) }
    }

    override fun removeSubview(subview: Subview) {
        subviews.remove(subview)
    }

    override fun reloadMovie() {
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

    override fun onRandomClicked() {
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

    override fun onMovieClicked(id: Int) {
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

    override fun onShareClicked() {
        navigator.share(Movie.theMovieDatabaseUrl + movie?.id, "Check out this movie!")
        movie?.let { logger.logMovieShared(it) }
    }

    override fun onSourceClicked(source: Source) {
        navigator.view(source.link)
        movie?.let { logger.logSourceViewed(it, source) }
    }

    override fun onReviewSourceClicked(source: ReviewSource) {
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

    override fun onTrailerClicked(video: Video) {
        navigator.view(video.getTrailerUrl())
        movie?.let { logger.logVideoViewed(it, video) }
    }

    override fun onImageClicked(imageUrl: String) {
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

    override fun setColor(color: Int) {
        this.color = color
        subviews.forEach { it.updateColor(color) }
    }

    override fun getColor() = color ?: Color.WHITE

    private fun showMovie() {
        if (viewIsLoading()) {
            movie?.let {
                view?.showContent(it)
                view?.setBookmark(bookmarkInteractor.isMovieBookmarked(it))
                subviews.forEach { sub -> sub.updateMovie(it) }
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
                    onRandomClicked()
                }
            }
            else -> {
                view?.showError("Oops! Ran into some connection issues", "Try again") {
                    if (reload == true) reloadMovie()
                    else if (id != null) onMovieClicked(id!!)
                    else onRandomClicked()
                }
            }
        }
    }

    override fun onBookmarkToggled() {
        movie?.let {
            if (getBookmarkStatus()) {
                bookmarkInteractor.removeBookmark(it)
                view?.setBookmark(false)
                view?.showSnackbar("Bookmark removed")
            } else {
                bookmarkInteractor.addBookmark(it)
                view?.setBookmark(true)
                view?.showSnackbar("Bookmark added")
            }
            logger.logBookmarkChange(it, !getBookmarkStatus())
        }
    }

    override fun onFilterDismissed(filter: Filter) {
        this.filter = filter
        logger.logFilterChange(filter)
    }

    override fun getFilter(): Filter = filter

    override fun getBookmarkStatus() = bookmarkInteractor.isMovieBookmarked(movie!!)

    override fun getMovie() = movie
}