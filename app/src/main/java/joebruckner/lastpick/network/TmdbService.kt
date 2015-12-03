package joebruckner.lastpick.network

import joebruckner.lastpick.BuildConfig
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.MovieSet
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query


public interface TmdbService {

    @GET("movie/top_rated?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTopRatedMovies(@Query("page") page: Int): Call<MovieSet>

    @GET("movie/{id}?api_key=${BuildConfig.TMDB_API_KEY}&append_to_response=credits,releases")
    fun getMovie(@Path("id") id: Int): Call<Movie>

    @GET("discover/movie?api_key=${BuildConfig.TMDB_API_KEY}&primary_release_date.lte=1960-12-31")
    fun discoverOldMovies(): Call<MovieSet>

}