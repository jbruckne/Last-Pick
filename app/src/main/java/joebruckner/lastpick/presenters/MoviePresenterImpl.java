package joebruckner.lastpick.presenters;

import android.support.annotation.Nullable;

import info.movito.themoviedbapi.model.MovieDb;
import joebruckner.lastpick.actors.Actor;
import joebruckner.lastpick.managers.TmdbManager;

public class MoviePresenterImpl implements MoviePresenter, TmdbManager.NewMovieListener {
	@Nullable Actor<MovieDb> actor;

	@Override public void attachActor(Actor<MovieDb> actor) {
		this.actor = actor;
	}

	@Override public void detachActor() {
		this.actor = null;
	}

	@Override public void start() {
		if (actor != null)
			actor.showLoading();
		TmdbManager.findNewMovie(this);
	}


	@Override public void newMovie(MovieDb movie) {
		if (actor != null)
			actor.showContent(movie);
	}

	@Override public void error(String errorMessage) {
		if (actor != null)
			actor.showError(errorMessage);
	}
}