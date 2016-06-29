package joebruckner.lastpick.network

import joebruckner.lastpick.BuildConfig
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("movie/{id}?api_key=${BuildConfig.TMDB_API_KEY}" +
            "&append_to_response=credits,releases,videos")
    fun getMovie(@Path("id") id: Int): Call<Movie>

    @GET("discover/movie?api_key=${BuildConfig.TMDB_API_KEY}" +
            "&vote_average.gte=6.5" +
            "&vote_count.gte=100" +
            "&language=100" +
            "&primary_release_date.lte=2015-12-31")
    fun discoverMovies(@Query("page") page: Int,
                       @Query("with_genres", encoded = true) genres: String?,
                       @Query("primary_release_date.lte") lessThanYear: String?,
                       @Query("primary_release_date.gte") greaterThanYear: String?): Call<Page>

}