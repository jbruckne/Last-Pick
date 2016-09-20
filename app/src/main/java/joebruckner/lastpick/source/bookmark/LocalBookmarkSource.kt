package joebruckner.lastpick.source.bookmark

import android.content.ContentValues
import android.util.Log
import joebruckner.lastpick.model.tmdb.TmdbMovie
import joebruckner.lastpick.source.DatabaseHelper
import joebruckner.lastpick.source.JsonFileManager
import rx.Observable
import javax.inject.Inject

class LocalBookmarkSource @Inject constructor(
        val dbHelper: DatabaseHelper,
        jsonFileManager: JsonFileManager
): BookmarkRepository {

    init {
        val legacyBookmarks = jsonFileManager.load<Array<TmdbMovie>>("bookmarks")
        legacyBookmarks?.forEach { saveBookmarkEntry(it.id) }
    }

    override fun getBookmarkEntries(): Observable<List<Int>> {
        return Observable.create<List<Int>> { subscriber ->
            val db = dbHelper.readableDatabase

            val cursor = db.rawQuery("SELECT * FROM ${BookmarkEntryV2.tableBookmarks}", null)
            val index = cursor.getColumnIndex(BookmarkEntryV2.columnMovieId)

            try {
                if (cursor.moveToFirst()) {
                    val movies = mutableSetOf<Int>()
                    do {
                        movies.add(cursor.getInt(index))
                    } while (cursor.moveToNext())
                    subscriber.onNext(movies.toList())
                } else {
                    subscriber.onNext(emptyList())
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

    override fun saveBookmarkEntry(id: Int) {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(BookmarkEntryV2.columnMovieId, id)
        db.insert(BookmarkEntryV2.tableBookmarks, null, contentValues)
    }

    override fun deleteBookmarkEntry(id: Int) {
        val db = dbHelper.writableDatabase

        db.delete(
                BookmarkEntryV2.tableBookmarks,
                BookmarkEntryV2.columnMovieId + "=" + id,
                null
        )
    }

    override fun checkIfBookmarkExists(id: Int): Observable<Boolean> {
        return Observable.create { subscriber ->
            val db = dbHelper.readableDatabase

            val cursor = db.query(
                    BookmarkEntryV2.tableBookmarks,
                    arrayOf(BookmarkEntryV2.columnMovieId),
                    BookmarkEntryV2.columnMovieId + "=?",
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
                subscriber.onNext(result)
                subscriber.onCompleted()
            }
        }
    }
}