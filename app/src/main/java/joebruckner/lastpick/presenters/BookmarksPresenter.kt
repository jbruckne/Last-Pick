package joebruckner.lastpick.presenters

import joebruckner.lastpick.data.Movie

interface BookmarksPresenter {
    fun attachView(view: BookmarksView)
    fun detachView()
    fun getBookmarks()

    interface BookmarksView {
        fun showContent(movies: List<Movie>)
        fun showError(errorMessage: String)
        var isLoading: Boolean
    }
}