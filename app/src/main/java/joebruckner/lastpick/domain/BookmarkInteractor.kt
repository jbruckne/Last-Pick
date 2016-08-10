package joebruckner.lastpick.domain

import joebruckner.lastpick.model.Movie
import rx.Observable

interface BookmarkInteractor {
    fun getBookmarks(): Observable<List<Movie>>
    fun isMovieBookmarked(movie: Movie): Boolean
    fun addBookmark(movie: Movie)
    fun removeBookmark(movie: Movie)
}