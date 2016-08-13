package joebruckner.lastpick.source.history

import android.content.ContentValues
import android.util.Log
import joebruckner.lastpick.source.DatabaseHelper
import rx.Observable
import javax.inject.Inject

class LocalHistoryRepository @Inject constructor(
        val dbHelper: DatabaseHelper
): HistoryRepository {

    override fun getHistoryEntries(): Observable<List<Int>> {
        return Observable.create<List<Int>> { subscriber ->
            val db = dbHelper.readableDatabase

            val cursor = db.rawQuery("SELECT * FROM ${HistoryEntry.TABLE_HISTORY}", null)

            val index = cursor.getColumnIndex(HistoryEntry.COLUMN_MOVIE_ID)

            try {
                if (cursor.moveToFirst()) {
                    val history = mutableListOf<Int>()
                    do {
                        history.add(cursor.getInt(index))
                    } while (cursor.moveToNext())
                    subscriber.onNext(history.reversed())
                } else {
                    subscriber.onNext(emptyList())
                }
            } catch(e: Exception) {
                Log.e("Database", "Error while trying to get history from database.")
            } finally {
                if (cursor != null && !cursor.isClosed)
                    cursor.close()
                db.close()
                subscriber.onCompleted()
            }
        }
    }

    fun trimTable(finalAmount: Int) {
        val db = dbHelper.writableDatabase

        db.execSQL(
                "DELETE FROM ${HistoryEntry.TABLE_HISTORY} " +
                "WHERE _id NOT IN (SELECT _id FROM ${HistoryEntry.TABLE_HISTORY} " +
                "ORDER BY _id DESC LIMIT $finalAmount)"
        )
    }

    override fun saveHistoryEntry(id: Int) {
        deleteHistoryEntry(id)
        trimTable(25)
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(HistoryEntry.COLUMN_MOVIE_ID, id)

        db.insert(HistoryEntry.TABLE_HISTORY, null, contentValues)
    }

    fun getSizeOfTable(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${HistoryEntry.TABLE_HISTORY}", null)
        val count = cursor.count
        cursor.close()
        db.close()
        return count
    }

    fun deleteHistoryEntry(id: Int) {
        val db = dbHelper.writableDatabase

        db.delete(
                HistoryEntry.TABLE_HISTORY,
                HistoryEntry.COLUMN_MOVIE_ID + "=" + id,
                null
        )
    }
}