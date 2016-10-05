package joebruckner.lastpick.source.collection

import joebruckner.lastpick.model.database.CollectionAction
import joebruckner.lastpick.model.database.ItemAction
import rx.Observable

interface CollectionRepository {
    fun getAll(): Observable<List<CollectionAction>>
    fun createCollection(name: String): Observable<CollectionAction>
    fun updateCollection(id: Int, name: String): Observable<CollectionAction>
    fun deleteCollection(id: Int)
    fun getCollection(id: Int): Observable<List<ItemAction>>
    fun addItem(cid: Int, mid: Int)
    fun removeItem(cid: Int, mid: Int)
}