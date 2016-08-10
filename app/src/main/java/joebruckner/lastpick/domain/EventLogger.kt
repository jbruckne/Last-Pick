package joebruckner.lastpick.domain

import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.guidebox.Source
import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.tmdb.Video

interface EventLogger {
    fun logRandomViewed(filter: Filter, count: Int)
    fun logFilterChange(filter: Filter)
    fun logBookmarkChange(movie: Movie, isBookmarked: Boolean)
    fun logMovieLoaded(movie: Movie)
    fun logMovieShared(movie: Movie)
    fun logVideoViewed(movie: Movie, video: Video)
    fun logSourceViewed(movie: Movie, source: Source)
    fun logError(error: Throwable)
}