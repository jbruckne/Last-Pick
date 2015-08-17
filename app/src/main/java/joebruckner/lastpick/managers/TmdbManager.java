package joebruckner.lastpick.managers;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class TmdbManager {

	public interface NewMovieListener {
		void newMovie(MovieDb movie);
		void error(String errorMessage);
	}

	public static void findNewMovie(NewMovieListener listener) {
		MovieTask movieTask = new MovieTask(listener);
		movieTask.execute();
	}

	private static class MovieTask extends AsyncTask<String, Integer, MovieDb> {
		private final String TAG = this.getClass().getSimpleName();
		private List<MovieDb> cachedResults;
		private NewMovieListener listener;
		private TmdbMovies movies;
		private Random random;

		private static final String API_KEY = "9856c489f3e6e5b5b972c6373c440210";
		private static final int MAX_PAGE = 50;

		public MovieTask(NewMovieListener listener) {
			this.cachedResults = new ArrayList<>();
			this.listener = listener;
			this.movies = new TmdbApi(API_KEY).getMovies();
			this.random = new Random(System.currentTimeMillis());
		}

		@Override protected MovieDb doInBackground(String... params) {
			if (cachedResults.isEmpty()) {
				Random random = new Random(System.currentTimeMillis());
				int page = random.nextInt(MAX_PAGE);
				Log.d(TAG, "PAGE: " + page);
				MovieResultsPage results = movies.getPopularMovieList("en", page);
				cachedResults = results.getResults();
			}

			int id = random.nextInt(cachedResults.size());
			Log.d(TAG, "ID: " + id);
			return cachedResults.remove(id);
		}

		@Override protected void onPostExecute(MovieDb movie) {
			Log.d(TAG, movie.toString());
			listener.newMovie(movie);
			super.onPostExecute(movie);
		}
	}
}
