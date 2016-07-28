package joebruckner.lastpick.ui.movie

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.State

class MovieContract {

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun reloadMovie()
        fun getNextMovie()
        fun getMovieById(id: Int)
        fun setMovie(movie: Movie)
        fun updateFilter(showAll: Boolean, selected: BooleanArray, yearGte: String, yearLte: String)
        fun isShowingAll(): Boolean
        fun updateBookmark()
        fun getSelectedGenres(): BooleanArray
        fun getBookmarkStatus(): Boolean
        fun getCurrentMovie(): Movie?
        fun getLte(): String
        fun getGte(): String
    }

    interface View {
        var state: State
        fun showLoading()
        fun showContent(movie: Movie)
        fun showError(errorMessage: String, errorButtonMessage: String, f: () -> Unit)
        fun showBookmarkUpdate(isBookmarked: Boolean, notify: Boolean) {}
        fun showBookmarkError(isBookmarked: Boolean) {}
    }
}
