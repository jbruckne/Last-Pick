package joebruckner.lastpick.presenters

import joebruckner.lastpick.data.Movie

interface MoviePresenter {
    fun attachActor(view: MovieView)
    fun detachActor()
    fun shuffleMovie()
	fun undoShuffle()

    interface MovieView {
        fun showLoading()
        fun showContent(movie: Movie)
        fun showError(errorMessage: String)
        fun isLoading(): Boolean
    }
}
