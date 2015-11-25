package joebruckner.lastpick.data

import com.google.gson.annotations.SerializedName

data class Release(
        val certification: String,
        @SerializedName("iso_3166_1")
        val country: String,
        @SerializedName("release_date")
        val releaseDate: String
) {

    data class ListWrapper(
            val countries: List<Release>
    )
}