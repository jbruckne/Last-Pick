package joebruckner.lastpick.presenters

import joebruckner.lastpick.data.Movie

interface MoviePresenter {
    fun attachActor(view: MovieView)
    fun detachActor()
    fun updateMovie()
    fun updateFilter(selected: BooleanArray, yearLte: String, yearGte: String)
    fun updateBookmark(movie: Movie, isAdding: Boolean)
    fun getSelectedGenres(): BooleanArray
    fun getLte(): String
    fun getGte(): String

    interface MovieView {
        fun showLoading()
        fun showContent(movie: Movie)
        fun showError(errorMessage: String)
        fun showBookmarkUpdate(isBookmarked: Boolean, notify: Boolean)
        fun showBookmarkError(isBookmarked: Boolean)
        fun getContent(): Movie?
        var isLoading: Boolean
    }
}
