package joebruckner.lastpick.presenters

import joebruckner.lastpick.data.Movie

interface MoviePresenter {
    fun attachActor(view: MovieView)
    fun detachActor()
    fun updateMovie()
    fun updateGenreFilter(selected: BooleanArray)
    fun updateBookmark(movie: Movie, isAdding: Boolean)
    fun getSelectedGenres(): BooleanArray

    interface MovieView {
        fun showLoading()
        fun showContent(movie: Movie)
        fun showError(errorMessage: String)
        fun showBookmarkUpdate(isBookmarked: Boolean)
        fun showBookmarkError(isBookmarked: Boolean)
        fun getContent(): Movie?
        var isLoading: Boolean
    }
}
