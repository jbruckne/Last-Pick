package joebruckner.lastpick.presenters

import joebruckner.lastpick.data.Movie

interface MoviePresenter {
    fun attachActor(view: MovieView)
    fun detachActor()
    fun updateMovie()
    fun undoShuffle()

    interface MovieView {
        fun showLoading()
        fun showContent(movie: Movie)
        fun showError(errorMessage: String)
        var isLoading: Boolean
    }
}
