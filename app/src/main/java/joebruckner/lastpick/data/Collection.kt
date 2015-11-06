package joebruckner.lastpick.data

import com.google.gson.annotations.SerializedName

data class Collection(
        val id: Int,
        val name: String,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("backdrop_path")
        val backdropPath: String
)