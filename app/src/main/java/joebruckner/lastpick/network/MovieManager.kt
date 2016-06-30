package joebruckner.lastpick.network

import joebruckner.lastpick.data.Filter
import joebruckner.lastpick.data.Movie
import rx.Observable
import java.util.*

class MovieManager(val serviceManager: ServiceManager, val historyManager: HistoryManager) {
    private val idCache = arrayListOf<Int>()
    private var currentFilter = Filter()
    private var scope = 1

    fun getNextMovie(filter: Filter): Observable<Movie> {
        // Check if cache is still valid
        if (!filter.equals(currentFilter))
            invalidateCache(filter)

        return getCachedIds(filter).flatMap { ids ->
            val id = ids[random(0..ids.size-1)]
            idCache.remove(id)
            serviceManager.getMovie(id).doOnNext { historyManager.addMovieToHistory(it) }
        }

    }

    fun getCachedIds(filter: Filter): Observable<List<Int>> {
        if (idCache.isNotEmpty()) return Observable.just(idCache)
        val page = random(1..scope)
        return serviceManager.fetchPage(page, filter)
                .doOnNext {
                    page -> scope = page.totalPages
                    idCache.addAll(page.getIds())
                }.flatMap { page -> Observable.just(page.getIds()) }
    }

    fun invalidateCache(filter: Filter) {
        idCache.clear()
        currentFilter = filter
        scope = 1
    }

    private fun random(range: IntRange): Int {
        if (range.last <= 1) return range.last
        val random = Random(System.currentTimeMillis())
        return random.nextInt(range.last - range.first) + range.first
    }
}