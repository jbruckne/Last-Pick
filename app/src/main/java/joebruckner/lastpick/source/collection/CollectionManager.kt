package joebruckner.lastpick.source.collection

import joebruckner.lastpick.model.presentation.MovieCollection
import rx.Observable

class CollectionManager(
        val local: CollectionRepository,
        val threshold: Int = 120
) {
    val collections = mutableSetOf<MovieCollection>()
    val items = mutableMapOf<Int, List<Int>>()
    var lastSync: Long = 0

    init {
        syncCollections()
    }

    fun getCollections(): Observable<List<MovieCollection>> {
        //if ((System.currentTimeMillis() - lastSync) / 10 > threshold)
            return Observable.just(collections.toList())
    }

    fun getCollection(id: Int): Observable<List<Int>> {
        return Observable.just(items[id] ?: emptyList())
    }

    fun createCollection(name: String): Observable<MovieCollection> {
        return Observable.create { subscriber ->
            local.createCollection(name).subscribe({
                val collection = MovieCollection(it.id, it.name)
                collections.add(collection)

                subscriber.onNext(collection)
            }, {
                subscriber.onError(Throwable("Failed to create collection"))
            })
        }
    }

    fun createCollection(name: String, movieId: Int): Observable<MovieCollection> {
        return Observable.create { subscriber ->
            local.createCollection(name).subscribe({
                val collection = MovieCollection(it.id, it.name)
                collections.add(collection)
                addMovie(it.id, movieId)

                subscriber.onNext(collection)
            }, {
               subscriber.onError(Throwable("Failed to create collection"))
            })
        }
    }

    fun addMovie(cid: Int, mid: Int) {
        items[cid] = items[cid]?.plus(mid) ?: listOf(mid)
        local.addItem(cid, mid)
    }

    fun removeMovie(cid: Int, mid: Int) {
        items[cid] = items[cid]?.minus(mid) ?: emptyList()
        local.removeItem(cid, mid)
    }

    fun syncCollections() {
        collections.add(MovieCollection(0, "Bookmarks"))
        lastSync = System.currentTimeMillis()
    }
}