package joebruckner.lastpick.source.bookmark

import joebruckner.lastpick.model.Movie
import rx.Observable

interface BookmarkDataSource {
    fun getBookmarkEntries(): Observable<List<Movie>>
    fun saveBookmarkEntry(movie: Movie)
    fun deleteBookmarkEntry(id: Int)
    fun checkIfBookmarkExists(id: Int): Boolean
}