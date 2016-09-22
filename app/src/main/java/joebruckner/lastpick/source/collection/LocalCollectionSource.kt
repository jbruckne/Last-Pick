package joebruckner.lastpick.source.collection

import joebruckner.lastpick.model.MovieCollection
import rx.Observable
import javax.inject.Inject

class LocalCollectionSource @Inject constructor(): CollectionRepository {
    // collectionId, collection
    val collections = mutableMapOf<Int, MovieCollection>()
    // collectionId, movieId
    val entries = mutableSetOf<Pair<Int, Int>>()

    override fun getAllCollections(): Observable<List<MovieCollection>> {
        return Observable.defer {
            Observable.just(collections.values.toList())
        }
    }

    override fun getAllEntries(): Observable<List<Pair<Int, Int>>> {
        return Observable.defer {
            Observable.just(entries.toList())
        }
    }

    override fun getCollection(id: Int): Observable<MovieCollection> {
        return Observable.defer<MovieCollection> {
            if (collections.containsKey(id)) Observable.just(collections[id])
            else Observable.error<MovieCollection>(Throwable("Collection does not exist"))
        }
    }

    override fun getMoviesInCollection(collectionId: Int): Observable<List<Int>> {
        return Observable.defer<List<Int>> {
            val movies = arrayListOf<Int>()
            entries.forEach { if (it.first == collectionId) movies.add(it.second) }
            Observable.just(movies)
        }
    }

    override fun getCollectionsFromMovie(movieId: Int): Observable<List<MovieCollection>> {
        return Observable.defer<List<MovieCollection>> {
            val set = mutableSetOf<MovieCollection>()
            entries.forEach {
                if (it.second == movieId) collections[it.first]?.let { set.add(it) }
            }
            Observable.just(set.toList())
        }
    }

    override fun addCollection(collection: MovieCollection) {
        collections.put(collection.id, collection)
    }

    override fun updateCollection(collectionId: Int, name: String) {
        collections.put(collectionId, MovieCollection(collectionId, name))
    }

    override fun removeCollection(id: Int) {
        collections.remove(id)
    }

    override fun addToCollection(collectionId: Int, movieId: Int) {
        entries.add(Pair(collectionId, movieId))
    }

    override fun removeFromCollection(collectionId: Int, movieId: Int) {
        entries.remove(Pair(collectionId, movieId))
    }
}