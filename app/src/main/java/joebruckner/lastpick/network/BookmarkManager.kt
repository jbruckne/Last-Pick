package joebruckner.lastpick.network

import android.util.Log
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.BookmarkUpdateEvent
import joebruckner.lastpick.events.BookmarkUpdateRequest
import joebruckner.lastpick.events.BookmarkedMoviesEvent
import joebruckner.lastpick.events.BookmarkedMoviesRequest

class BookmarkManager(val bus: Bus) {
    val bookmarks = arrayListOf<Movie>()

    init {
        bus.register(this)
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
    }

}