package joebruckner.lastpick.models;

import android.support.annotation.NonNull;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

/**
 * Wrapper class for MovieDb
 */
public class Movie {
	@NonNull private MovieDb movie;

	private static final String BASE_URL = "http://image.tmdb.org/t/p/";
	private static final String BACKDROP_SIZE = "w1280";
	private static final String POSTER_SIZE = "w342";

	public Movie(@NonNull MovieDb movie) {
		this.movie = movie;
	}

	@NonNull public MovieDb getRawMovie() {
		return movie;
	}

	@NonNull public String getTitle() {
		return movie.getTitle();
	}

	@NonNull public String getSummary() {
		return movie.getOverview();
	}

	@NonNull public List<PersonCast> getCast() {
		return movie.getCast();
	}

	@NonNull public String getPosterPath() {
		return BASE_URL + POSTER_SIZE + movie.getPosterPath();
	}

	@NonNull public String getBackdropPath() {
		return BASE_URL + BACKDROP_SIZE + movie.getBackdropPath();
	}

	@NonNull public String getReleaseDate() {
		return movie.getReleaseDate();
	}

	public int getRuntime() {
		return movie.getRuntime();
	}

	public float getRating() {
		return movie.getUserRating();
	}
}
