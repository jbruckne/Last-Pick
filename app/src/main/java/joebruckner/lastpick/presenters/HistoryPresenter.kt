package joebruckner.lastpick.presenters

import joebruckner.lastpick.data.Movie

interface HistoryPresenter {
    fun attachView(view: HistoryView)
    fun detachView()
    fun getHistory()

    interface HistoryView {
        fun showContent(movies: List<Movie>)
        fun showError(errorMessage: String)
        var isLoading: Boolean
    }
}