package joebruckner.lastpick.domain.impl

import joebruckner.lastpick.domain.BookmarkInteractor
import joebruckner.lastpick.source.bookmark.FirebaseBookmarkSource
import joebruckner.lastpick.source.bookmark.LocalBookmarkSource
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkInteractorImpl @Inject constructor(
        val networkBookmarkRepository: FirebaseBookmarkSource,
        val localBookmarkRepository: LocalBookmarkSource
): BookmarkInteractor {

    override fun getBookmarks(): Observable<List<Int>> {
        return Observable.concat(
                localBookmarkRepository.getBookmarkEntries(),
                networkBookmarkRepository.getBookmarkEntries()
        )
        .first()
        .doOnNext {
            it.forEach { localBookmarkRepository.saveBookmarkEntry(it) }
        }
    }

    override fun isMovieBookmarked(id: Int): Observable<Boolean> {
        return localBookmarkRepository.checkIfBookmarkExists(id)
    }

    override fun addBookmark(id: Int) {
        networkBookmarkRepository.saveBookmarkEntry(id)
        localBookmarkRepository.saveBookmarkEntry(id)
    }

    override fun removeBookmark(id: Int) {
        networkBookmarkRepository.deleteBookmarkEntry(id)
        localBookmarkRepository.deleteBookmarkEntry(id)
    }

    override fun syncBookmarks() {
        localBookmarkRepository.getBookmarkEntries().subscribe {
            it.forEach { id ->
                networkBookmarkRepository.saveBookmarkEntry(id)
            }
        }
        networkBookmarkRepository.getBookmarkEntries().subscribe {
            it.forEach { id ->
                localBookmarkRepository.saveBookmarkEntry(id)
            }
        }
    }
}