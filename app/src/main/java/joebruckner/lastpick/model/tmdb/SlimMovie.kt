package joebruckner.lastpick.model.tmdb

import com.google.gson.annotations.SerializedName
import joebruckner.lastpick.model.Movie

class SlimMovie(
        val id: Int,
        val title: String,
        val overview: String,
        @SerializedName("release_date")
        val releaseDate: String,
        @SerializedName("genre_ids")
        val genreIds: List<Int>,
        val popularity: Double,
        val adult: Boolean,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("backdrop_path")
        val backdropPath: String,
        @SerializedName("original_language")
        val originalLanguage: String,
        @SerializedName("original_title")
        val originalTitle: String,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int
) {
    fun getFullPosterPath(): String = Movie.imageUrl + posterPath
    fun getFullBackdropPath(): String = Movie.imageUrl + backdropPath
}