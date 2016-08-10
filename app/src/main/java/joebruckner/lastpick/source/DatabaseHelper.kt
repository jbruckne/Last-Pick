package joebruckner.lastpick.source

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import joebruckner.lastpick.source.bookmark.BookmarkEntry
import joebruckner.lastpick.source.history.HistoryEntry
import joebruckner.lastpick.source.movie.MovieEntry

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

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        val NAME = "GuideboxMovie.db"
        val VERSION = 2
        val COLUMN_ID = "_id"
    }
}