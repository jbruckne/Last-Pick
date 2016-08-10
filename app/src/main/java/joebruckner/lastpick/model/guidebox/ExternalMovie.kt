package joebruckner.lastpick.model.guidebox

import com.google.gson.annotations.SerializedName

data class ExternalMovie(
    val id: Int,
    val themoviedb: Int,
    @SerializedName("rottentomatoes")
    val rottenTomatoes: Int,
    val imdb: String,
    val freebase: String,
    val metacritic: String
)