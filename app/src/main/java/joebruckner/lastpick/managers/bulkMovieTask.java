package joebruckner.lastpick.managers;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class BulkMovieTask extends AsyncTask<String, Integer, List<MovieDb>> {
	@NonNull private BulkMovieListener listener;
	@NonNull private String language;
	@NonNull private String key;
	private int scope;
	private int pages;

	public static final String ERROR_NOT_FOUND = "Error: No movies found";

	public BulkMovieTask(int pages, int scope, @NonNull String key, @NonNull String language,
	                     @NonNull BulkMovieListener listener) {
		this.key = key;
		this.scope = scope;
		this.pages = pages;
		this.language = language;
		this.listener = listener;
	}

	@Override protected List<MovieDb> doInBackground(String... params) {
		try {
			Random random = new Random(System.currentTimeMillis());
			TmdbMovies movies = new TmdbApi(key).getMovies();
			List<MovieDb> results = new ArrayList<>();

			for (int i = 0; i < pages; i++) {
				int page = random.nextInt(scope);
				Log.d(this.getClass().getSimpleName(), "PAGE: " + page);
				results.addAll(movies.getPopularMovieList(language, page).getResults());
			}

			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override protected void onPostExecute(@Nullable List<MovieDb> results) {
		if (results == null)
			listener.onError(ERROR_NOT_FOUND);
		else
			listener.onNewMovies(results);
		super.onPostExecute(results);
	}
}