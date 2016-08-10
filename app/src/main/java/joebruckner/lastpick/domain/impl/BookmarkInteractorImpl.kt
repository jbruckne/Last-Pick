package joebruckner.lastpick.domain.impl

import joebruckner.lastpick.domain.BookmarkInteractor
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.source.bookmark.LocalBookmarkDataSource
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkInteractorImpl @Inject constructor(
        val localBookmarkSource: LocalBookmarkDataSource
): BookmarkInteractor {

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