package joebruckner.lastpick.source

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import joebruckner.lastpick.source.bookmark.BookmarkEntry
import joebruckner.lastpick.source.bookmark.BookmarkEntryV2
import joebruckner.lastpick.source.history.HistoryEntry
import joebruckner.lastpick.source.movie.MovieEntry

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MovieEntry.CREATE_TABLE_MOVIES)
        db.execSQL(HistoryEntry.CREATE_TABLE_HISTORY)
        db.execSQL(BookmarkEntryV2.createTableBookmarks)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val cursor = db.rawQuery("select * from ${BookmarkEntry.TABLE_BOOKMARKS}", null)
        val index = cursor.getColumnIndex(BookmarkEntry.COLUMN_MOVIE_ID)

        val ids = arrayListOf<Int>()
        try {
            if (cursor.moveToFirst()) {
                do {
                    ids.add(cursor.getInt(index))
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
        }

        ids.forEach {
            val content = ContentValues()
            content.put(BookmarkEntryV2.columnMovieId, it)
            db.insert(BookmarkEntryV2.tableBookmarks, null, content)
        }
        db.execSQL(BookmarkEntry.DELETE_TABLE_BOOKMARKS)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        val NAME = "GuideboxMovie.db"
        val VERSION = 3
        val COLUMN_ID = "_id"
    }
}