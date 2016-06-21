package joebruckner.lastpick.network

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.*
import java.util.*

class MovieManager(val bus: Bus, val serviceManager: ServiceManager) {
    val idCache = arrayListOf<Int>()
    val cacheType = arrayListOf<Genre>()
    var scope = 0

    init {
        bus.register(this)
    }

    @Subscribe fun onMovieRequest(request: MovieRequest) = getMovie(request.genres)

    fun getMovie(genres: List<Genre>?) {
        AsyncTask.execute {
            // Check if cache is still valid
            if (scope == 0)
                invalidateCache(null)
            if (genres != null && !genres.equals(cacheType))
                invalidateCache(genres)
            if (genres == null && cacheType.isNotEmpty())
                invalidateCache(null)

            // Refill cache if needed, then get a random movie
            var movie: Movie? = null
            while (movie == null) {
                if (idCache.isEmpty()) {
                    if (scope == 0) break
                    val page = serviceManager.fetchPage(random(1..scope), genres)
                    if (page == null || page.results.size() == 0) break
                    else idCache.addAll(page.getIds())
                }
                movie = serviceManager.fetchMovie(idCache.removeAt(random(0..idCache.size-1)))
            }

            Handler(Looper.getMainLooper()).post {
                if (movie == null) bus.post(ErrorEvent("Couldn't find any movies", 400))
                else bus.post(MovieEvent(movie as Movie))
            }
        }
    }

    fun invalidateCache(genres: List<Genre>?) {
        idCache.clear()
        cacheType.clear()
        val initialPage = serviceManager.fetchPage(1, genres)
        scope = initialPage?.totalPages ?: 0
        cacheType.addAll(genres ?: emptyList())
    }

    private fun random(range: IntRange): Int {
        if (range.last <= 1) return range.last
        val random = Random(System.currentTimeMillis())
        return random.nextInt(range.last - range.first) + range.first
    }
}