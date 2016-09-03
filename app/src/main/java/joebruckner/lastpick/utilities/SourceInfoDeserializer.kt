package joebruckner.lastpick.utils

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import joebruckner.lastpick.model.guidebox.GuideboxMovie
import java.lang.reflect.Type

class SourceInfoDeserializer: JsonDeserializer<GuideboxMovie> {

    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): GuideboxMovie {
        val element = json.asJsonObject["other_sources"]
        when (true) {
            element != null && element.isJsonArray -> {
                json.asJsonObject.add("other_sources", null)
                return context.deserialize(json, GuideboxMovie::class.java)
            }
            else -> {
                return Gson().fromJson(json, GuideboxMovie::class.java)
            }
        }
    }
}