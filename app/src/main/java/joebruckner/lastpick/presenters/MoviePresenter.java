package joebruckner.lastpick.presenters;

import info.movito.themoviedbapi.model.MovieDb;
import joebruckner.lastpick.actors.Actor;

public interface MoviePresenter {
	void attachActor(Actor<MovieDb> actor);
	void detachActor();
	void start();
}
