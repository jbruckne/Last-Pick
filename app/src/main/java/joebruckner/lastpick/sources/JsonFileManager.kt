package joebruckner.lastpick.sources

import android.content.Context
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream

class JsonFileManager(val context: Context, val gson: Gson = Gson()) {

    inline fun <reified T: Any> load(fileName: String): T? {
        val input = l(fileName) ?: return null
        val json = input.reader().readText()
        return gson.fromJson(json, T::class.java)
    }

    fun l(f: String): FileInputStream? {
        val file = File(context.filesDir, f)
        if (!file.exists()) {
            context.openFileOutput(f, Context.MODE_PRIVATE).close()
        }
        try {
            val input = FileInputStream(file)
            return input
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