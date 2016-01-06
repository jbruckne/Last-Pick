package joebruckner.lastpick.data

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class Page(
        val page: Int,
        val results: JsonArray,
        @SerializedName("total_results") val totalResults: Int,
        @SerializedName("total_pages") val totalPages: Int
) {

    public fun getIds():
            List<Int> = results.map { it.asJsonObject.get("id").asInt }
}
