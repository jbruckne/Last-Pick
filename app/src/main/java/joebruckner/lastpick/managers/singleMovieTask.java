package joebruckner.lastpick.managers;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class SingleMovieTask extends AsyncTask<String, Integer, MovieDb> {
	@NonNull private SingleMovieListener listener;
	@NonNull private String language;
	@NonNull private String key;
	private int id;

	public static final String ERROR_NOT_FOUND = "Error: Movie not found";

	public SingleMovieTask(int id, @NonNull String key, @NonNull String language,
	                      @NonNull SingleMovieListener listener) {
		this.listener = listener;
		this.language = language;
		this.key = key;
		this.id = id;
	}

	@Override protected MovieDb doInBackground(String... params) {
		try {
			TmdbMovies movies = new TmdbApi(key).getMovies();
			return movies.getMovie(id, language, TmdbMovies.MovieMethod.credits);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override protected void onPostExecute(@Nullable MovieDb movie) {
		if (movie == null)
			listener.onError(ERROR_NOT_FOUND);
		else
			listener.onNewMovie(movie);
		super.onPostExecute(movie);
	}
}
