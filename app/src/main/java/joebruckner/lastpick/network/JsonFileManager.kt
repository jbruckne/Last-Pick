package joebruckner.lastpick.network

import android.content.Context
import com.google.gson.Gson

class JsonFileManager(val context: Context, val gson: Gson = Gson()) {

    inline fun <reified T: Any> load(fileName: String): T? {
        try {
            val input = context.openFileInput(fileName)
            val json = input.reader().readText()
            return gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun save(fileName: String, content: Any) {
        val json = gson.toJson(content)
        val output = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        output.write(json.toString().toByteArray())
    }
}