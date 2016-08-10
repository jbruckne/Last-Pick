package joebruckner.lastpick.domain.impl

import joebruckner.lastpick.domain.MovieInteractor
import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.ListType
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.tmdb.Page
import joebruckner.lastpick.source.movie.LocalMovieDataSource
import joebruckner.lastpick.source.movie.NetworkMovieDataSource
import joebruckner.lastpick.utils.addIfNotContained
import joebruckner.lastpick.utils.shuffle
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieInteractorImpl @Inject constructor(
        val networkMovieSource: NetworkMovieDataSource,
        val localMovieSource: LocalMovieDataSource
): MovieInteractor {
    private val usedPages = mutableListOf<Int>()
    private val usedMovies = mutableListOf<Int>()
    private val unusedPages = mutableListOf<Int>()
    private val unusedMovies = mutableListOf<Int>()

    private var filter: Filter = Filter()
    private var queryData: Page? = null
    private var last: Movie? = null

    var count = 0

    override fun getMovie(id: Int): Observable<Movie> {
        return Observable
                .concat(
                        localMovieSource.getMovie(id),
                        networkMovieSource.getMovie(id)
                )
                .first()
                .doOnNext {
                    last = it
                    localMovieSource.saveMovieEntry(it)
                }
    }

    override fun getMovieSuggestion(filter: Filter): Observable<Movie> {
        if (this.filter != filter) {
            this.filter = filter
            queryData = null
            usedPages.clear()
            usedMovies.clear()
            unusedPages.clear()
            unusedMovies.clear()
        }
        return getIdWorkingSet(filter)
                .map { it.minus(blacklist) }
                .flatMapIterable { it.shuffle() }
                .first()
                .doOnNext {
                    usedMovies.add(it)
                    unusedMovies.remove(it)
                    count++
                }
                .flatMap { getMovie(it) }
                .retry(2)
    }

    private fun getIdWorkingSet(filter: Filter): Observable<List<Int>> {
        if (unusedMovies.isNotEmpty()) return Observable.just(unusedMovies)
        return getPageWorkingSet(filter)
                .flatMapIterable { it.shuffle() }
                .first()
                .flatMap { networkMovieSource.getPage(it, filter) }
                .doOnNext {
                    usedPages.add(it.page)
                    unusedPages.remove(it.page)
                }
                .flatMapIterable { it.results }
                .map { it.id }
                .doOnNext { unusedMovies.addIfNotContained(it) }
                .toList()
    }

    private fun getPageWorkingSet(filter: Filter): Observable<List<Int>> {
        return when (true) {
            unusedPages.isNotEmpty() -> Observable.just(unusedPages)
            queryData == null -> networkMovieSource
                    .getPage(1000, filter)
                    .flatMapIterable { IntRange(1, it.totalPages).toList() }
                    .doOnNext { unusedPages.addIfNotContained(it) }
                    .toList()
            else -> Observable.error(Throwable(MovieInteractor.OUT_OF_SUGGESTIONS))
        }
    }

    override fun getLastMovie(): Observable<Movie> {
        if (last != null)
            return Observable.just(last)
        if (usedMovies.isNotEmpty())
            return getMovie(usedMovies.last())
        return Observable.error(Throwable(ERROR_RELOAD))
    }

    override fun getSpecialList(type: ListType): Observable<Page> {
        return networkMovieSource.getSpecialList(type)
    }

    override fun resetMovieSuggestions() {
        unusedPages.addAll(usedMovies)
        unusedMovies.addAll(usedMovies)
        usedPages.clear()
        usedMovies.clear()
    }

    companion object {
        val blacklist = listOf(170522, 64956, 262551)
        val ERROR_RELOAD = "Failed to reload movie"
    }
}