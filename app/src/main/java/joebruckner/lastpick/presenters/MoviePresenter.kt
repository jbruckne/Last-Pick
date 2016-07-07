package joebruckner.lastpick.presenters

import joebruckner.lastpick.data.Movie

interface MoviePresenter {
    fun attachActor(view: MovieView)
    fun detachActor()
    fun getNextMovie()
    fun setMovie(movie: Movie)
    fun updateFilter(selected: BooleanArray, yearLte: String, yearGte: String)
    fun updateBookmark()
    fun getSelectedGenres(): BooleanArray
    fun getBookmarkStatus(): Boolean
    fun getCurrentMovie(): Movie?
    fun getLte(): String
    fun getGte(): String

    interface MovieView {
        fun showLoading()
        fun showContent(movie: Movie)
        fun showError(message: String)
        fun showMovieRecycleButton()
        fun showBookmarkUpdate(isBookmarked: Boolean, notify: Boolean)
        fun showBookmarkError(isBookmarked: Boolean)
        var isLoading: Boolean
    }
}
