package joebruckner.lastpick.source.collection

import android.content.ContentValues
import android.database.Cursor
import joebruckner.lastpick.model.database.CollectionAction

object CollectionEntry {
    val action = "Action"
    val cid = "Cid"
    val name = "Name"
    val time = "Timestamp"
    val tableCollections = "Items"
    val createTableCollections = "CREATE TABLE IF NOT EXISTS $tableCollections (" +
            "$cid INTEGER PRIMARY KEY, " +
            "$name TEXT, " +
            "$action INTEGER, " +
            "$time INTEGER, " +
            ");"
    val deleteTableCollections = "DROP TABLE IF EXISTS $cid"

    fun createCollectionRow(collection: CollectionAction): ContentValues {
        val values = ContentValues()
        values.put(action, collection.action)
        values.put(cid, collection.id)
        values.put(name, collection.name)
        values.put(time, collection.timestamp)
        return values
    }

    fun retrieveCollection(cursor: Cursor): CollectionAction {
        return CollectionAction(
                cursor.getInt(cursor.getColumnIndex(action)),
                cursor.getInt(cursor.getColumnIndex(cid)),
                cursor.getString(cursor.getColumnIndex(name)),
                cursor.getLong(cursor.getColumnIndex(time))
        )
    }
}