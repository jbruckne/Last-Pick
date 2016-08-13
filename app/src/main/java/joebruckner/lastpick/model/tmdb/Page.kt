package joebruckner.lastpick.model.tmdb

import com.google.gson.annotations.SerializedName

data class Page(
        val page: Int,
        val results: List<SlimMovie>,
        @SerializedName("total_results") val totalResults: Int,
        @SerializedName("total_pages") val totalPages: Int
) {

    fun getIds() = results.map { it.id }
}
