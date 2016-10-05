package joebruckner.lastpick.source.collection

import android.content.ContentValues
import android.database.Cursor
import joebruckner.lastpick.model.database.ItemAction

object ItemEntry {
    val action = "Action"
    val mid = "Mid"
    val cid = "Cid"
    val time = "Timestamp"
    val tableItems = "Items"
    val createTableItems = "CREATE TABLE IF NOT EXISTS $tableItems (" +
            "$cid INTEGER PRIMARY KEY, " +
            "$mid INTEGER, " +
            "$action INTEGER, " +
            "$time INTEGER, " +
            ");"
    val deleteTableItems = "DROP TABLE IF EXISTS $tableItems"

    fun createItemRow(item: ItemAction): ContentValues {
        val values = ContentValues()
        values.put(cid, item.collectionId)
        values.put(mid, item.movieId)
        values.put(action, item.action)
        values.put(time, item.timestamp)
        return values
    }

    fun retrieveItem(cursor: Cursor): ItemAction {
        return ItemAction(
                cursor.getInt(cursor.getColumnIndex(action)),
                cursor.getInt(cursor.getColumnIndex(mid)),
                cursor.getInt(cursor.getColumnIndex(cid)),
                cursor.getLong(cursor.getColumnIndex(time))
        )
    }
}