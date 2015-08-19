package joebruckner.lastpick.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieManager implements BulkMovieListener, SingleMovieListener {
	@Nullable private OnNewMovieListener listener;
	@NonNull private List<MovieDb> cachedResults;

	private static final String API_KEY = "9856c489f3e6e5b5b972c6373c440210";
	private static final String LANGUAGE = "en";
	private static final int PAGES = 3;
	private static final int SCOPE = 150;

	public MovieManager() {
		cachedResults = new ArrayList<>();
	}

	public void setOnNewMovieListener(@NonNull OnNewMovieListener listener) {
		this.listener = listener;
	}

	public void findNewMovie() {
		if (cachedResults.isEmpty())
			refillCache();
		else
			getMovie();
	}

	private void refillCache() {
		BulkMovieTask task = new BulkMovieTask(PAGES, SCOPE, API_KEY, LANGUAGE, this);
		task.execute();
	}

	private void getMovie() {
		Log.d(this.getClass().getSimpleName(), "Fetching Movie");
		int size = cachedResults.size();
		Random random = new Random(System.currentTimeMillis());
		int id = cachedResults.remove(random.nextInt(size)).getId();
		SingleMovieTask task = new SingleMovieTask(id, API_KEY, LANGUAGE, this);
		task.execute();
	}

	@Override public void onNewMovies(@NonNull List<MovieDb> movies) {
		this.cachedResults = movies;
		Log.d(this.getClass().getSimpleName(), "Cache Refilled");
		findNewMovie();
	}

	@Override public void onNewMovie(@NonNull MovieDb movie) {
		Log.d(this.getClass().getSimpleName(), "Fetched Movie");
		if (listener != null)
			listener.onNewMovie(movie);
	}

	@Override public void onError(@NonNull String errorMessage) {
		if (listener != null)
			listener.onError(errorMessage);
	}
}
