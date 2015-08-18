package joebruckner.lastpick.managers;


import android.support.annotation.NonNull;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public interface BulkMovieListener {
	void onNewMovies(@NonNull List<MovieDb> movies);
	void onError(@NonNull String errorMessage);
}
