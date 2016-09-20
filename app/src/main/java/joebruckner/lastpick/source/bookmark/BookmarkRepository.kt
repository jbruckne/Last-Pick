package joebruckner.lastpick.source.bookmark

import rx.Observable

interface BookmarkRepository {
    fun getBookmarkEntries(): Observable<List<Int>>
    fun saveBookmarkEntry(id: Int)
    fun deleteBookmarkEntry(id: Int)
    fun checkIfBookmarkExists(id: Int): Observable<Boolean>
}