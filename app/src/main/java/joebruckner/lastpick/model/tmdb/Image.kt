package joebruckner.lastpick.model.tmdb

import com.google.gson.annotations.SerializedName

data class Image(
        @SerializedName("aspect_ratio")
        val aspectRatio: Double,
        @SerializedName("file_path")
        val filePath: String,
        val height: Int,
        val width: Int

) {

    data class ListWrapper(
            val backdrops: List<Image>,
            val posters: List<Image>
    )
}