package joebruckner.lastpick.source.collection

import joebruckner.lastpick.model.MovieCollection
import rx.Observable

interface CollectionRepository {
    fun getAllCollections(): Observable<List<MovieCollection>>
    fun getAllEntries(): Observable<List<Pair<Int, Int>>>
    fun getCollection(id: Int): Observable<MovieCollection>
    fun getMoviesInCollection(collectionId: Int): Observable<List<Int>>
    fun getCollectionsFromMovie(movieId: Int): Observable<List<MovieCollection>>
    fun addCollection(collection: MovieCollection)
    fun updateCollection(collectionId: Int, name: String)
    fun removeCollection(id: Int)
    fun addToCollection(collectionId: Int, movieId: Int)
    fun removeFromCollection(collectionId: Int, movieId: Int)
}