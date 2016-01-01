package joebruckner.lastpick.data

import com.google.gson.JsonArray

data class Page(
        val page: Int,
        val results: JsonArray
) {

    public fun getIds():
            List<Int> = results.map { it.asJsonObject.get("id").asInt }
}
