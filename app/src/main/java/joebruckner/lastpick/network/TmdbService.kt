package joebruckner.lastpick.network

import com.google.gson.JsonObject
import joebruckner.lastpick.BuildConfig
import joebruckner.lastpick.data.Genre
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Page
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query

public interface TmdbService {

    @GET("movie/top_rated?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTopRatedMovies(@Query("page") page: Int): Call<Page>

    @GET("movie/{id}?api_key=${BuildConfig.TMDB_API_KEY}" +
            "&append_to_response=credits,releases,videos")
    fun getMovie(@Path("id") id: Int): Call<Movie>

    @GET("discover/movie?api_key=${BuildConfig.TMDB_API_KEY}" +
            "&vote_average.gte=6.5" +
            "&vote_count.gte=100" +
            "&language=100" +
            "&primary_release_date.lte=2015-12-31")
    fun discoverMovies(@Query("page") page: Int,
                       @Query("with_genres", encoded = true) genres: String?): Call<Page>

}