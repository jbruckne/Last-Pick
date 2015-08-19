package joebruckner.lastpick.presenters;

import joebruckner.lastpick.actors.Actor;
import joebruckner.lastpick.models.Movie;

public interface MoviePresenter {
	void attachActor(Actor<Movie> actor);
	void detachActor();
	void start();
}
