package joebruckner.lastpick.data

/*
 * Requests
 */

data class MovieRequest(val genres: List<Genre>?)

class RecentHistoryRequest

class BookmarkedMoviesRequest

data class BookmarkUpdateRequest(val movie: Movie, val isAdding: Boolean)

/*
 * Events
 */

data class RecentHistoryEvent(val history: List<Movie>)

data class BookmarkedMoviesEvent(val bookmarked: List<Movie>)

data class MovieEvent(val movie: Movie)

data class BookmarkUpdateEvent(val movie: Movie, val isSuccess: Boolean)

data class ErrorEvent(val message: String, val code: Int)