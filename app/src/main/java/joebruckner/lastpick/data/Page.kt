package joebruckner.lastpick.data

import com.google.gson.annotations.SerializedName

data class Page(
        val page: Int,
        val results: List<CondensedMovie>,
        @SerializedName("total_results") val totalResults: Int,
        @SerializedName("total_pages") val totalPages: Int
) {

    fun getIds() = results.map { it.id }
}
