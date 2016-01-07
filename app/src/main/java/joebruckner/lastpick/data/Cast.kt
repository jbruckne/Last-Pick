package joebruckner.lastpick.data

import com.google.gson.annotations.SerializedName

data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    @SerializedName("cast_id")
    val castId: Int,
    @SerializedName("credit_id")
    val creditId: String,
    val order: Int,
    @SerializedName("profile_path")
    val profilePath: String
) {
    fun firstCharacter(): String {
        val text = StringBuilder()
        text.append(character.substringBefore('/').substringBefore('('))
        if (text.length > 20) text.replace(20, text.length, "...")
        return text.toString()
    }
}