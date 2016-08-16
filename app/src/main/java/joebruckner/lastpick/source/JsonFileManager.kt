package joebruckner.lastpick.source

import android.content.Context
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import javax.inject.Inject
import javax.inject.Named

class JsonFileManager @Inject constructor(
        @Named("Application") val context: Context,
        val gson: Gson = Gson()
) {

    inline fun <reified T: Any> load(fileName: String): T? {
        val json = l(fileName) ?: return null
        return gson.fromJson(json, T::class.java)
    }

    fun l(f: String): String? {
        val file = File(context.filesDir, f)
        if (!file.exists()) {
            context.openFileOutput(f, Context.MODE_PRIVATE).close()
        }
        try {
            val input = FileReader(file).readText()
            return input
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun save(fileName: String, content: Any?) {
        val json = gson.toJson(content)
        val output = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        output.write(json.toString().toByteArray())
    }
}