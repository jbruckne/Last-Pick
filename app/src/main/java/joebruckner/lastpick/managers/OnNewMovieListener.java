package joebruckner.lastpick.managers;

import info.movito.themoviedbapi.model.MovieDb;

public interface OnNewMovieListener {
	void onNewMovie(MovieDb movie);
	void onError(String errorMessage);
}
