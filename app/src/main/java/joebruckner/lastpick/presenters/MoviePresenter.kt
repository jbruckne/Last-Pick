package joebruckner.lastpick.presenters

import joebruckner.lastpick.data.Movie

interface MoviePresenter {
    fun attachActor(view: MovieView)
    fun detachActor()
    fun reloadMovie()
    fun getNextMovie()
    fun getMovieById(id: Int)
    fun setMovie(movie: Movie)
    fun updateFilter(selected: BooleanArray, yearGte: String, yearLte: String)
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
        fun showMovieErrorButton(message: String, listener: () -> Unit)
        fun showBookmarkUpdate(isBookmarked: Boolean, notify: Boolean)
        fun showBookmarkError(isBookmarked: Boolean)
        var isLoading: Boolean
    }
}
