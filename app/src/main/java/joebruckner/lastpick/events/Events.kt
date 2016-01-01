package joebruckner.lastpick.events

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Page

/*
 * Requests
 */

class RandomMovieRequest

class RecentHistoryRequest

class BookmarkedMoviesRequest

data class MovieRequest(val id: Int)

data class PageRequest(val page: Int = 1)

data class BookmarkUpdateRequest(val movie: Movie, val isAdding: Boolean)

/*
 * Events
 */

data class RecentHistoryEvent(val history: List<Movie>)

data class BookmarkedMoviesEvent(val bookmarked: List<Movie>)

data class MovieEvent(val movie: Movie)

data class PageEvent(val page: Page)

data class BookmarkUpdateEvent(val movie: Movie, val isSuccess: Boolean)

data class RequestErrorEvent(val message: String, val code: Int)