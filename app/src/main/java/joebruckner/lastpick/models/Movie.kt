package joebruckner.lastpick.models

import com.google.common.math.DoubleMath
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.people.PersonCast

/**
 * Wrapper class for MovieDb
 */
data class Movie(
        @JvmField var adult: Boolean,
        @JvmField var backdrop_path: String,
        @JvmField var belongs_to_collection: JsonObject?,
        @JvmField var budget: Int,
        @JvmField var genres: JsonArray,
        @JvmField var homepage: String,
        @JvmField var id: Int,
        @JvmField var imdb_id: String,
        @JvmField var original_language: String,
        @JvmField var original_title: String,
        @JvmField var overview: String,
        @JvmField var popularity: Double,
        @JvmField var poster_path: String,
        @JvmField var production_companies: JsonArray,
        @JvmField var production_countries: JsonArray,
        @JvmField var release_date: String,
        @JvmField var revenue: Int,
        @JvmField var runtime: Int,
        @JvmField var spoken_languages: JsonArray,
        @JvmField var status: String,
        @JvmField var tagline: String,
        @JvmField var title: String,
        @JvmField var video: Boolean,
        @JvmField var vote_average: Double,
        @JvmField var vote_count: Int
) {

    companion object {
        private val BASE_URL = "http://image.tmdb.org/t/p/"
        private val BACKDROP_SIZE = "w1280"
        private val POSTER_SIZE = "w342"
    }
}
