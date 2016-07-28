package joebruckner.lastpick.interactors.impl

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.interactors.BookmarkInteractor
import joebruckner.lastpick.sources.JsonFileManager
import joebruckner.lastpick.sources.bookmark.LocalBookmarkDataSource
import rx.Observable

class BookmarkInteractorImpl(
        jsonFileManager: JsonFileManager,
        val localBookmarkSource: LocalBookmarkDataSource
): BookmarkInteractor {

    init {
        val bookmarks = mutableSetOf<Movie>()
        val savedBookmarks = jsonFileManager.load<Array<Movie>>("bookmarks")
        bookmarks.addAll(savedBookmarks?.toList() ?: emptyList())
        bookmarks.forEach { localBookmarkSource.saveBookmarkEntry(it) }
    }

    override fun getBookmarks(): Observable<List<Movie>> {
        return localBookmarkSource.getBookmarkEntries()
    }

    override fun isMovieBookmarked(movie: Movie): Boolean {
        return localBookmarkSource.checkIfBookmarkExists(movie.id)
    }

    override fun addBookmark(movie: Movie) {
        localBookmarkSource.saveBookmarkEntry(movie)
    }

    override fun removeBookmark(movie: Movie) {
        localBookmarkSource.deleteBookmarkEntry(movie.id)
    }
}