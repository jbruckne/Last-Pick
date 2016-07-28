package joebruckner.lastpick.sources.bookmark

import joebruckner.lastpick.data.Movie
import rx.Observable

interface BookmarkDataSource {
    fun getBookmarkEntries(): Observable<List<Movie>>
    fun saveBookmarkEntry(movie: Movie)
    fun deleteBookmarkEntry(id: Int)
    fun checkIfBookmarkExists(id: Int): Boolean
}