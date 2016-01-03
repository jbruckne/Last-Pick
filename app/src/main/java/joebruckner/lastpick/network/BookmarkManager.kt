package joebruckner.lastpick.network

import android.util.Log
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.BookmarkUpdateEvent
import joebruckner.lastpick.events.BookmarkUpdateRequest
import joebruckner.lastpick.events.BookmarkedMoviesEvent
import joebruckner.lastpick.events.BookmarkedMoviesRequest

class BookmarkManager(val bus: Bus, val jsonFileManager: JsonFileManager) {
    val bookmarks = arrayListOf<Movie>()
    val BOOKMARK_FILE = "bookmarks"

    init {
        bus.register(this)
    }

    fun loadSavedBookmarks() {
        val savedBookmarks = jsonFileManager.load<Array<Movie>>(BOOKMARK_FILE)
        Log.d("BookmarkManager",
                if (savedBookmarks != null) savedBookmarks.toString()
                else "Bookmarks not found"
        )
        bookmarks.addAll(savedBookmarks?.toList() ?: emptyList())
    }

    @Subscribe fun bookmarkedMoviesRequested(request: BookmarkedMoviesRequest) {
        bus.post(BookmarkedMoviesEvent(bookmarks.toList()))
    }

    @Subscribe fun bookmarkUpdateRequested(request: BookmarkUpdateRequest) {
        request.movie.isBookmarked = request.isAdding
        if (request.isAdding && !bookmarks.contains(request.movie))
            bookmarks.add(request.movie)
        else if (!request.isAdding)
            bookmarks.remove(request.movie)
        bus.post(BookmarkUpdateEvent(request.movie, true))
        jsonFileManager.save(BOOKMARK_FILE, bookmarks)
    }

}