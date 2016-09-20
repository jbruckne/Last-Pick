package joebruckner.lastpick.source.bookmark

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import rx.Observable
import javax.inject.Inject

class FirebaseBookmarkSource @Inject constructor(): BookmarkRepository {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun getBookmarkEntries(): Observable<List<Int>> {
        if (auth.currentUser == null)
            Observable.error<List<Int>>(Throwable(errorNotLoggedIn))

        return Observable.create { subscriber ->
            database.getReference("users")
                    .child(auth.currentUser!!.uid)
                    .child("bookmarks")
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            subscriber.onError(Throwable(error.message))
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val ids = arrayListOf<Int>()
                            snapshot.children.forEach { ids.add(it.key.toInt()) }
                            subscriber.onNext(ids)
                        }
                    })
        }
    }

    override fun saveBookmarkEntry(id: Int) {
        if (auth.currentUser == null) return

        database.getReference("users")
                .child(auth.currentUser!!.uid)
                .child("bookmarks")
                .child(id.toString())
                .setValue(true)
    }

    override fun deleteBookmarkEntry(id: Int) {
        if (auth.currentUser == null) return

        database.getReference("users")
                .child(auth.currentUser!!.uid)
                .child("bookmarks")
                .child(id.toString())
                .removeValue()
    }

    override fun checkIfBookmarkExists(id: Int): Observable<Boolean> {
        if (auth.currentUser == null)
            return Observable.error<Boolean>(Throwable(errorNotLoggedIn))

        return Observable.create { subscriber ->
            database.getReference("users")
                    .child(auth.currentUser!!.uid)
                    .child("bookmarks")
                    .child(id.toString())
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            subscriber.onNext(false)
                            subscriber.onCompleted()
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            subscriber.onNext(true)
                            subscriber.onCompleted()
                        }
                    })
        }
    }

    companion object {
        val errorNotLoggedIn = "User must be logged in to retrieve bookmarks from Firebase"
    }
}