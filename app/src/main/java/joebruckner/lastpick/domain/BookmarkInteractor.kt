package joebruckner.lastpick.domain

import rx.Observable

interface BookmarkInteractor {
    fun getBookmarks(): Observable<List<Int>>
    fun isMovieBookmarked(id: Int): Observable<Boolean>
    fun addBookmark(id: Int)
    fun removeBookmark(id: Int)
    fun syncBookmarks()
}