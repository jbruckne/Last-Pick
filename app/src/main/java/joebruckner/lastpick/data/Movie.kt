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
        val revenue: Long,
        val status: String,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int,
        val videos: Video.ListWrapper?,
        val releases: Release.ListWrapper
) {

    override fun equals(other: Any?): Boolean {
        if (other !is Movie) return false
        return this.id == other.id
    }

    fun getFullTrailerPath(): String = BASE_TRAILER_URL + getYoutubeTrailer()

    fun getFullPosterPath(): String = BASE_URL + POSTER_SIZE + posterPath

    fun getFullBackdropPath(): String = BASE_URL + BACKDROP_SIZE + backdropPath

    fun getSimpleMpaa(): String {
        releases.countries.forEach { release ->
            if (release.country == "US") return release.certification
        }
        return releases.countries[0].certification
    }

    fun getYoutubeTrailer(): String? {
        videos?.results?.forEach { video ->
            if (video.site == "YouTube") return video.key
        }
        return null
    }

    companion object {
        val BASE_TRAILER_URL = "https://www.youtube.com/watch?v="
        val BASE_URL = "http://image.tmdb.org/t/p/"
        val BACKDROP_SIZE = "w1280"
        val POSTER_SIZE = "w342"
    }
}
