package joebruckner.lastpick.source.collection

import com.google.gson.Gson
import joebruckner.lastpick.model.database.CollectionAction
import joebruckner.lastpick.model.database.ItemAction
import joebruckner.lastpick.source.DatabaseHelper
import rx.Observable
import java.util.*
import javax.inject.Inject

class LocalCollectionSource @Inject constructor(
        val dbHelper: DatabaseHelper,
        val gson: Gson = Gson()
): CollectionRepository {


    override fun getAll(): Observable<List<CollectionAction>> {
        return Observable.defer<List<CollectionAction>> {
            val reader = dbHelper.readableDatabase

            val cursor = reader.rawQuery("select * from ${CollectionEntry.tableCollections}", null)

            if (cursor.moveToFirst()) {
                val collections = mutableListOf<CollectionAction>()
                do {
                    collections.add(CollectionEntry.retrieveCollection(cursor))
                } while (cursor.moveToNext())
                Observable.just(collections)
            } else {
                Observable.error<List<CollectionAction>>(Throwable("No Collections Found"))
            }
        }
    }

    override fun getCollection(id: Int): Observable<List<ItemAction>> {
        return Observable.defer<List<ItemAction>> {
            val reader = dbHelper.readableDatabase

            val cursor = reader.rawQuery("select * from ${CollectionEntry.tableCollections} " +
                    "where ${ItemEntry.cid}=$id", null)

            if (cursor.moveToFirst()) {
                val items = mutableListOf<ItemAction>()
                do {
                    items.add(ItemEntry.retrieveItem(cursor))
                } while (cursor.moveToNext())
                Observable.just(items)
            } else {
                Observable.error<List<ItemAction>>(Throwable("Collection Not Found"))
            }
        }
    }

    override fun createCollection(name: String): Observable<CollectionAction> {
        return Observable.defer<CollectionAction> {
            Observable.just(createCollection(
                    UUID.randomUUID().mostSignificantBits.toInt(),
                    name
            ))
        }
    }

    private fun createCollection(id: Int, name: String): CollectionAction {
        val action = CollectionAction(1, id, name)
        val content = CollectionEntry.createCollectionRow(action)
        dbHelper.writableDatabase.insert(CollectionEntry.tableCollections, null, content)
        return action
    }

    override fun updateCollection(id: Int, name: String): Observable<CollectionAction> {
        return Observable.defer<CollectionAction> {
            deleteCollection(id)
            Observable.just(createCollection(id, name))
        }
    }

    override fun deleteCollection(id: Int) {
        val action = CollectionAction(0, id, "")
        val content = CollectionEntry.createCollectionRow(action)
        dbHelper.writableDatabase.insert(CollectionEntry.tableCollections, null, content)
    }

    override fun addItem(cid: Int, mid: Int) {
        val item = ItemEntry.createItemRow(ItemAction(1, mid, cid))
        dbHelper.writableDatabase.insert(ItemEntry.tableItems, null, item)
    }

    override fun removeItem(cid: Int, mid: Int) {
        val item = ItemEntry.createItemRow(ItemAction(0, mid, cid))
        dbHelper.writableDatabase.insert(ItemEntry.tableItems, null, item)
    }
}