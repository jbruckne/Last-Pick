package joebruckner.lastpick.sources

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import joebruckner.lastpick.sources.bookmark.BookmarkEntry
import joebruckner.lastpick.sources.history.HistoryEntry
import joebruckner.lastpick.sources.movie.MovieEntry

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MovieEntry.CREATE_TABLE_MOVIES)
        db.execSQL(HistoryEntry.CREATE_TABLE_HISTORY)
        db.execSQL(BookmarkEntry.CREATE_TABLE_BOOKMARKS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(MovieEntry.DELETE_TABLE_MOVIES)
        db.execSQL(HistoryEntry.DELETE_TABLE_HISTORY)
        db.execSQL(BookmarkEntry.DELETE_TABLE_BOOKMARKS)
        onCreate(db)
    }

    companion object {
        val NAME = "Movie.db"
        val VERSION = 7
        val COLUMN_ID = "_id"
    }
}