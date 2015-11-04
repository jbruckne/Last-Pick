package joebruckner.lastpick.models

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class MovieSet(
        @JvmField var page: Int,
        @JvmField var results: JsonArray
) {}
