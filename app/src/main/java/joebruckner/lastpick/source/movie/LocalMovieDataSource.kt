package joebruckner.lastpick.source.movie

import android.content.ContentValues
import android.util.Log
import com.google.gson.Gson
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.Showcase
import joebruckner.lastpick.model.tmdb.Page
import joebruckner.lastpick.source.DatabaseHelper
import rx.Observable
import javax.inject.Inject

class LocalMovieDataSource @Inject constructor(
        val dbHelper: DatabaseHelper
) : MovieDataSource {

    override fun getMovie(id: Int): Observable<Movie> {
        return Observable.create<Movie> { subscriber ->
            val db = dbHelper.readableDatabase

            val cursor = db.query(
                    MovieEntry.TABLE_MOVIES,
                    arrayOf(MovieEntry.COLUMN_VALUE),
                    MovieEntry.COLUMN_MOVIE_ID + "=?",
                    arrayOf(id.toString()),
                    null,
                    null,
                    null
            )
            try {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MovieEntry.COLUMN_VALUE)
                    val json = cursor.getString(index)
                    subscriber.onNext(Gson().fromJson(json, Movie::class.java))
                }
            } catch(e: Exception) {
                Log.e("Database", "Error while trying to get movies from database.")
            } finally {
                if (cursor != null && !cursor.isClosed)
                    cursor.close()
                db.close()
                subscriber.onCompleted()
            }
        }
    }

    override fun getPage(page: Int, filter: Filter): Observable<Page> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getSpecialList(type: Showcase): Observable<Page> {
        throw UnsupportedOperationException("not implemented")
    }

    fun trimTable(finalAmount: Int) {
        val db = dbHelper.writableDatabase

        db.execSQL(
                "DELETE FROM ${MovieEntry.TABLE_MOVIES} " +
                        "WHERE _id NOT IN (SELECT _id FROM ${MovieEntry.TABLE_MOVIES} " +
                        "ORDER BY _id DESC LIMIT $finalAmount)"
        )
    }

    override fun saveMovieEntry(movie: Movie) {
        val db = dbHelper.writableDatabase
        trimTable(100)
        val values = ContentValues()
        values.put(MovieEntry.COLUMN_MOVIE_ID, movie.id)
        values.put(MovieEntry.COLUMN_VALUE, Gson().toJson(movie))

        db.insert(MovieEntry.TABLE_MOVIES, null, values)
    }
}