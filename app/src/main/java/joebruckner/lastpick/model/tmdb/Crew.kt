package joebruckner.lastpick.model.tmdb

import com.google.gson.annotations.SerializedName
import joebruckner.lastpick.model.Movie

data class Crew(
        val id: Int,
        val name: String,
        val job: String,
        val department: String,
        @SerializedName("credit_id")
        val creditId: String,
        @SerializedName("profile_path")
        val profilePath: String
) {
        fun getFullProfilePath() = Movie.imageUrl + profilePath
}