package joebruckner.lastpick.data

import com.google.gson.annotations.SerializedName

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
        fun getFullProfilePath() = Movie.BASE_URL + Movie.POSTER_SIZE + profilePath
}