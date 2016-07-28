package joebruckner.lastpick.interactors.impl

import joebruckner.lastpick.data.Filter
import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Page
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.sources.movie.LocalMovieDataSource
import joebruckner.lastpick.sources.movie.NetworkMovieDataSource
import joebruckner.lastpick.utils.addIfNotContained
import joebruckner.lastpick.utils.shuffle
import rx.Observable

class MovieInteractorImpl(
        val networkMovieSource: NetworkMovieDataSource,
        val localMovieSource: LocalMovieDataSource
): MovieInteractor {
    private val usedPages = mutableListOf<Int>()
    private val usedMovies = mutableListOf<Int>()
    private val unusedPages = mutableListOf<Int>()
    private val unusedMovies = mutableListOf<Int>()

    private var filter: Filter = Filter()
    private var queryData: Page? = null

    override fun getMovie(id: Int): Observable<Movie> {
        return Observable
                .concat(
                        localMovieSource.getMovie(id),
                        networkMovieSource.getMovie(id)
                )
                .first()
                .doOnNext { localMovieSource.saveMovieEntry(it) }
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
                .flatMapIterable { it.shuffle() }
                .first()
                .flatMap { getMovie(it) }
                .doOnNext {
                    usedMovies.add(it.id)
                    unusedMovies.remove(it.id)
                }
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
                    .flatMapIterable { IntRange(0, it.totalPages).toList() }
                    .doOnNext { unusedPages.addIfNotContained(it) }
                    .toList()
            else -> Observable.error(Throwable(MovieInteractor.OUT_OF_SUGGESTIONS))
        }
    }

    override fun getLastMovie(): Observable<Movie> {
        return getMovie(usedMovies.last())
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
}