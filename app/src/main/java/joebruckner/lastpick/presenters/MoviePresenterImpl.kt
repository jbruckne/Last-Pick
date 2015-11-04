package joebruckner.lastpick.presenters

import android.util.Log

import info.movito.themoviedbapi.model.MovieDb
import joebruckner.lastpick.actors.Actor
import joebruckner.lastpick.managers.MovieManager
import joebruckner.lastpick.managers.OnNewMovieListener
import joebruckner.lastpick.models.Movie

class MoviePresenterImpl : MoviePresenter, OnNewMovieListener {
	var actor: Actor<Movie>? = null
    val manager: MovieManager

    init {
        manager = MovieManager()
        manager.listener = this
    }

    override fun attachActor(actor: Actor<Movie>) {
        this.actor = actor
    }

    override fun detachActor() {
        this.actor = null
    }

    override fun shuffleMovie() {
        actor?.showLoading()
        manager.findNewMovie()
    }

	override fun undoShuffle() {
		throw UnsupportedOperationException()
	}

    override fun onNewMovie(movie: MovieDb) {
        actor?.showContent(Movie(movie))
    }

    override fun onError(errorMessage: String) {
        actor?.showError(errorMessage)
    }
}