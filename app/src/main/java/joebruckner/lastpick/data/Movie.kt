package joebruckner.lastpick.data

import com.google.gson.annotations.SerializedName

data class Movie(
        val id: Int,
        val title: String,
        val overview: String,
        val tagline: String,
        @SerializedName("release_date")
        val releaseDate: String,
        val runtime: Int,
        val genres: List<Genre>,
        val popularity: Double,
        @SerializedName("belongs_to_collection")
        val belongsToCollection: Collection?,
        val credits: Credits,
        val adult: Boolean,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("backdrop_path")
        val backdropPath: String,
        val budget: Int,
        val homepage: String,
        @SerializedName("imdb_id")
        val imdbId: String,
        @SerializedName("original_language")
        val originalLanguage: String,
        @SerializedName("original_title")
        val originalTitle: String,
        val revenue: Int,
        val status: String,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int,
        val videos: Video.ListWrapper
) {


    companion object {
        private val BASE_URL = "http://image.tmdb.org/t/p/"
        private val BACKDROP_SIZE = "w1280"
        private val POSTER_SIZE = "w342"
    }
}
