package joebruckner.lastpick.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import info.movito.themoviedbapi.model.MovieDb;
import joebruckner.lastpick.actors.Actor;
import joebruckner.lastpick.managers.MovieManager;
import joebruckner.lastpick.managers.OnNewMovieListener;
import joebruckner.lastpick.models.Movie;

public class MoviePresenterImpl implements MoviePresenter, OnNewMovieListener {
	@Nullable Actor<Movie> actor;
	@NonNull MovieManager manager;

	public MoviePresenterImpl() {
		manager = new MovieManager();
		manager.setOnNewMovieListener(this);
	}

	@Override public void attachActor(Actor<Movie> actor) {
		this.actor = actor;
	}

	@Override public void detachActor() {
		this.actor = null;
	}

	@Override public void start() {
		if (actor != null)
			actor.showLoading();
		manager.findNewMovie();
	}

	@Override public void onNewMovie(MovieDb movie) {
		Log.d(this.getClass().getSimpleName(), "New Movie Received");
		if (actor != null) actor.showContent(new Movie(movie));
	}

	@Override public void onError(String errorMessage) {
		if (actor != null) actor.showError(errorMessage);
	}
}