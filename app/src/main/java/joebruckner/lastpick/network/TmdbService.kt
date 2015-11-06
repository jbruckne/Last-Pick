package joebruckner.lastpick.network

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.MovieSet
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query


public interface TmdbService {

    companion object {
        const val api_key = ""
    }

    @GET("movie/top_rated?api_key=$api_key")
    fun getTopRatedMovies(@Query("page") page: Int): Call<MovieSet>

    @GET("movie/{id}?api_key=$api_key&append_to_response=credits")
    fun getMovie(@Path("id") id: Int): Call<Movie>

}