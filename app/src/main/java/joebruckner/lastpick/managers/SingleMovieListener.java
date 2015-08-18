package joebruckner.lastpick.managers;


import android.support.annotation.NonNull;

import info.movito.themoviedbapi.model.MovieDb;

public interface SingleMovieListener {
	void onNewMovie(@NonNull MovieDb movie);
	void onError(@NonNull String errorMessage);
}