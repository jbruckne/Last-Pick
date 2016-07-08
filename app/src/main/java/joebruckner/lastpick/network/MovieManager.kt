package joebruckner.lastpick.network

import joebruckner.lastpick.data.Filter
import joebruckner.lastpick.data.Movie
import rx.Observable
import java.util.*

class MovieManager(val serviceManager: ServiceManager, val historyManager: HistoryManager) {
    private val unused = arrayListOf<Int>()
    private var currentFilter = Filter()
    private var random = Random()
    private var totalPages = MAX_PAGES
    private var totalResults = MAX_RESULTS
    private var resultCounter = 0

    fun getMovie(id: Int): Observable<Movie> {
        return serviceManager.getMovie(id)
    }

    fun getNextMovie(filter: Filter): Observable<Movie> {
        // Check if cache is still valid
        if (!filter.equals(currentFilter))
            invalidateCache(filter)

        random = Random(System.currentTimeMillis())

        return doInitialRequest(filter)
                .flatMap { getRandomPage(filter) }
                .flatMap { getRandomMovie(it) }
    }

    fun reuseMovies() = invalidateCache(currentFilter)

    private fun getRandomMovie(ids: List<Int>): Observable<Movie> {
        if (ids.isEmpty()) return Observable.error(Throwable(OUT_OF_MOVIES))
        val id = ids[random.nextInt(ids.size)]
        unused.remove(id)
        return serviceManager.getMovie(id)
                .doOnNext {
                    historyManager.addMovieToHistory(it)
                    resultCounter++
                }
    }

    private fun getRandomPage(filter: Filter): Observable<List<Int>> {
        return if (unused.isNotEmpty()) Observable.just(unused)
            else if (resultCounter >= totalResults) Observable.error(Throwable(OUT_OF_MOVIES))
            else serviceManager.getPage(random.nextInt(totalPages) + 1, filter)
                    .flatMap { page -> Observable.just(page.getIds()) }
                    .doOnNext { ids -> unused.addAll(ids) }
    }

    private fun doInitialRequest(filter: Filter): Observable<Nothing?> {
        return if (resultCounter > 0) Observable.just(null)
            else serviceManager.getPage(1, filter)
                .flatMap { page ->
                    totalPages = page.totalPages
                    totalResults = page.totalResults
                    Observable.just(null)
                }
    }

    private fun invalidateCache(filter: Filter) {
        unused.clear()
        currentFilter = filter
        resultCounter = 0
        totalResults = MAX_RESULTS
        totalPages = MAX_PAGES
    }

    companion object {
        val OUT_OF_MOVIES = "End of movies"
        val MAX_PAGES = 70
        val MAX_RESULTS = 1440
    }
}