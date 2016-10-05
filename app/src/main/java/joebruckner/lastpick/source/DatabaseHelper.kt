package joebruckner.lastpick.source

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import joebruckner.lastpick.model.database.CollectionAction
import joebruckner.lastpick.model.database.ItemAction
import joebruckner.lastpick.source.bookmark.BookmarkEntry
import joebruckner.lastpick.source.collection.CollectionEntry
import joebruckner.lastpick.source.collection.ItemEntry
import joebruckner.lastpick.source.history.HistoryEntry
import joebruckner.lastpick.source.movie.MovieEntry

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MovieEntry.CREATE_TABLE_MOVIES)
        db.execSQL(HistoryEntry.CREATE_TABLE_HISTORY)
        db.execSQL(ItemEntry.createTableItems)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(ItemEntry.createTableItems)
        db.execSQL(CollectionEntry.createTableCollections)
        db.execSQL(BookmarkEntry.CREATE_TABLE_BOOKMARKS)

        val createBookmarks = CollectionAction(1, 0, "Bookmarks")
        val entry = CollectionEntry.createCollectionRow(createBookmarks)
        db.insert(CollectionEntry.tableCollections, null, entry)

        try {
            val cursor = db.rawQuery("select * from ${BookmarkEntry.TABLE_BOOKMARKS}", null)
            val index = cursor.getColumnIndex(BookmarkEntry.COLUMN_MOVIE_ID)
            if (cursor.moveToFirst()) {
                do {
                    val createItem = ItemAction(1, cursor.getInt(index), 0)
                    db.insert(ItemEntry.tableItems, null, ItemEntry.createItemRow(createItem))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        db.execSQL(BookmarkEntry.DELETE_TABLE_BOOKMARKS)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        val NAME = "GuideboxMovie.db"
        val VERSION = 4
        val COLUMN_ID = "_id"
    }
}