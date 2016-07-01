package joebruckner.lastpick.network

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("movie/{id}?append_to_response=credits,releases,videos")
    fun getMovie( @Path("id") id: Int, @Query("api_key") key: String): Call<Movie>

    @GET("discover/movie?vote_average.gte=6.5" +
            "&vote_count.gte=100" +
            "&language=100" +
            "&primary_release_date.lte=2015-12-31")
    fun discoverMovies(
            @Query("api_key") key: String,
            @Query("page") page: Int,
            @Query("with_genres", encoded = true) genres: String?,
            @Query("primary_release_date.lte") lessThanYear: String?,
            @Query("primary_release_date.gte") greaterThanYear: String?
    ): Call<Page>

}