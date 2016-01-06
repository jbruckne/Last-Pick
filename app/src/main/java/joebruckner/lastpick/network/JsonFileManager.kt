package joebruckner.lastpick.network

import android.content.Context
import android.util.Log
import com.google.gson.Gson

class JsonFileManager(val context: Context, val gson: Gson = Gson()) {

    inline fun <reified T: Any> load(fileName: String): T? {
        try {
            val input = context.openFileInput(fileName)
            val json = input.reader().readText()
            Log.e("Json", json)
            return gson.fromJson(json, javaClass<T>())
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