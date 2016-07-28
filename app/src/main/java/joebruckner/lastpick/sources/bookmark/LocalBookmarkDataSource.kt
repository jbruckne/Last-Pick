package joebruckner.lastpick.sources.bookmark

import android.content.ContentValues
import android.util.Log
import com.google.gson.Gson
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.sources.DatabaseHelper
import rx.Observable

class LocalBookmarkDataSource(val dbHelper: DatabaseHelper): BookmarkDataSource {

    override fun getBookmarkEntries(): Observable<List<Movie>> {
        return Observable.create<List<Movie>> { subscriber ->
            val db = dbHelper.readableDatabase

            val cursor = db.rawQuery("SELECT * FROM ${BookmarkEntry.TABLE_BOOKMARKS}", null)

            val index = cursor.getColumnIndex(BookmarkEntry.COLUMN_MOVIE)

            try {
                if (cursor.moveToFirst()) {
                    val movies = mutableListOf<Movie>()
                    do {
                        movies.add(Gson().fromJson(cursor.getString(index), Movie::class.java))
                    } while (cursor.moveToNext())
                    subscriber.onNext(movies)
                }
            } catch(e: Exception) {
                Log.e("Database", "Error while trying to get bookmarks from database.")
            } finally {
                if (cursor != null && !cursor.isClosed)
                    cursor.close()
                db.close()
                subscriber.onCompleted()
            }
        }
    }

    override fun saveBookmarkEntry(movie: Movie) {
        if (checkIfBookmarkExists(movie.id)) return
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(BookmarkEntry.COLUMN_MOVIE_ID, movie.id)
        contentValues.put(BookmarkEntry.COLUMN_MOVIE, Gson().toJson(movie))

        db.insert(BookmarkEntry.TABLE_BOOKMARKS, null, contentValues)
    }

    override fun deleteBookmarkEntry(id: Int) {
        val db = dbHelper.writableDatabase

        db.delete(
                BookmarkEntry.TABLE_BOOKMARKS,
                BookmarkEntry.COLUMN_MOVIE_ID + "=" + id,
                null
        )
    }

    override fun checkIfBookmarkExists(id: Int): Boolean {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
                BookmarkEntry.TABLE_BOOKMARKS,
                arrayOf(BookmarkEntry.COLUMN_MOVIE_ID),
                BookmarkEntry.COLUMN_MOVIE_ID + "=?",
                arrayOf(id.toString()),
                null,
                null,
                null
        )

        var result = false
        try {
            if (cursor.moveToFirst()) result = true
        } catch(e: Exception) {
            result = false
        } finally {
            if (cursor != null && !cursor.isClosed)
                cursor.close()
            db.close()
            return result
        }
    }
}