package joebruckner.lastpick.interactors

import joebruckner.lastpick.data.Movie
import rx.Observable

interface BookmarkInteractor {
    fun getBookmarks(): Observable<List<Movie>>
    fun isMovieBookmarked(movie: Movie): Boolean
    fun addBookmark(movie: Movie)
    fun removeBookmark(movie: Movie)
}