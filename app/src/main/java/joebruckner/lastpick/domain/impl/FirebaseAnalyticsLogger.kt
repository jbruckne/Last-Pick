package joebruckner.lastpick.domain.impl

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import joebruckner.lastpick.domain.EventLogger
import joebruckner.lastpick.model.presentation.Filter
import joebruckner.lastpick.model.presentation.Movie
import joebruckner.lastpick.model.presentation.Showcase
import joebruckner.lastpick.model.guidebox.Source
import joebruckner.lastpick.model.tmdb.SlimMovie
import joebruckner.lastpick.model.tmdb.Video
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsLogger @Inject constructor(
        val analytics: FirebaseAnalytics
): EventLogger {

    override fun logRandomViewed(filter: Filter, count: Int) {
        val params = Bundle()
        params.putString("genres", filter.genresToString())
        params.putBoolean("show_all", filter.showAll)
        params.putInt("year_gte", filter.yearGte.toInt())
        params.putInt("year_lte", filter.yearLte.toInt())
        params.putInt("count", count)
        params.putString("time", Date().toString())
        analytics.logEvent("random_movie", params)
    }

    override fun logFilterChange(filter: Filter) {
        val params = Bundle()
        params.putString("genres", filter.genresToString())
        params.putBoolean("show_all", filter.showAll)
        params.putInt("year_gte", filter.yearGte.toInt())
        params.putInt("year_lte", filter.yearLte.toInt())
        params.putString("time", Date().toString())
        analytics.logEvent("filter_changed", params)
    }

    override fun logBookmarkChange(movie: Movie, isBookmarked: Boolean) {
        val params = Bundle()
        params.putBoolean("is_bookmarked", isBookmarked)
        params.putInt("movie_id", movie.id)
        params.putString("movie_title", movie.title)
        params.putString("time", Date().toString())
        analytics.logEvent("bookmark_changed", params)
    }

    override fun logMovieLoaded(movie: Movie) {
        val params = Bundle()
        params.putInt("movie_id", movie.id)
        params.putString("movie_title", movie.title)
        params.putString("time", Date().toString())
        analytics.logEvent("new_movie", params)
    }

    override fun logMovieShared(movie: Movie) {
        val params = Bundle()
        params.putInt("movie_id", movie.id)
        params.putString("movie_title", movie.title)
        params.putString("time", Date().toString())
        analytics.logEvent("share_movie", params)
    }

    override fun logVideoViewed(movie: Movie, video: Video) {
        val params = Bundle()
        params.putInt("movie_id", movie.id)
        params.putString("movie_title", movie.title)
        params.putString("video", video.name)
        params.putString("time", Date().toString())
        analytics.logEvent("watch_video", params)
    }

    override fun logSourceViewed(movie: Movie, source: Source) {
        val params = Bundle()
        params.putInt("movie_id", movie.id)
        params.putString("movie_title", movie.title)
        params.putString("guidebox", source.source)
        params.putString("time", Date().toString())
        analytics.logEvent("view_source", params)
    }

    override fun logShowcaseViewed(movie: SlimMovie, type: Showcase) {
        val params = Bundle()
        params.putInt("movie_id", movie.id)
        params.putString("type", type.name)
        params.putString("time", Date().toString())
        analytics.logEvent("view_showcase", params)
    }

    override fun logSpecialListViewed(type: Showcase) {
        val params = Bundle()
        params.putString("type", type.name)
        params.putString("time", Date().toString())
        analytics.logEvent("view_special_list", params)
    }

    override fun logError(error: Throwable) {
        val params = Bundle()
        params.putString("error_message", error.message)
        params.putString("time", Date().toString())
        analytics.logEvent("new_movie", params)
        Log.e("Movie", "Failed to retrieve movie", error)
    }
}