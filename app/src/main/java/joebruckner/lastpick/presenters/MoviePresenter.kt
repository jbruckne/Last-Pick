package joebruckner.lastpick.presenters

import joebruckner.lastpick.actors.Actor
import joebruckner.lastpick.models.Movie

interface MoviePresenter {
    fun attachActor(actor: Actor<Movie>)
    fun detachActor()
    fun shuffleMovie()
	fun undoShuffle()
}
