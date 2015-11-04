package joebruckner.lastpick.managers

import android.util.Log
import com.google.gson.JsonArray

import java.io.IOException

import info.movito.themoviedbapi.model.MovieDb
import joebruckner.lastpick.models.Movie
import joebruckner.lastpick.models.MovieSet
import org.json.JSONArray
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit

class MovieManager(var listener: OnNewMovieListener? = null) {
    val service = Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbService::class.java)

    fun findNewMovie() {
        //updateMovieSet()
        getMovieById(13)
    }

    private fun updateMovieSet(page: Int) {
        service.getTopRatedMovies(page).enqueue(object: Callback<MovieSet> {

            public override fun onResponse(response: Response<MovieSet>, retrofit: Retrofit) {
                if (response.isSuccess && response.code() == 200)
                    Log.d("IDs", getResultIds(response.body().results).toString())
                else
                    listener?.onError("Failed to fetch page $page of Top Rated")
            }

            public override fun onFailure(t: Throwable) {
                t.printStackTrace()
            }

        });
    }

    private fun getMovieById(id: Int) {
        service.getMovie(id).enqueue(object: Callback<Movie> {
            override fun onResponse(response: Response<Movie>, retrofit: Retrofit) {
                if (response.isSuccess && response.code() == 200)
                    Log.d("Movie", response.body().toString())
                else
                    listener?.onError("Failed to fetch Movie with Id $id")
            }

            override fun onFailure(t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    private fun getResultIds(results: JsonArray):
            List<Int> = results.map { it.asJsonObject.get("id").asInt }

    companion object {
        private val LANGUAGE = "en"
        private val PAGES = 3
        private val SCOPE = 150
    }
}
