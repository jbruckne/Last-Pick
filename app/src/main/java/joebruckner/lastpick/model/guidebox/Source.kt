package joebruckner.lastpick.model.guidebox

import com.google.gson.annotations.SerializedName

data class Source(
    val source: String,
    @SerializedName("display_name")
    val displayName: String,
    val link: String
) {

    data class OtherSourcesWrapper(
            @SerializedName("movie_theater")
            val movieTheater: List<Source> = emptyList()
    )
}