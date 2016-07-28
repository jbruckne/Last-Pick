package joebruckner.lastpick.sources

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Page
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface TmdbService {

    @GET("movie/{id}?append_to_response=credits,releases,videos")
    fun getMovie( @Path("id") id: Int, @Query("api_key") key: String): Observable<Movie>

    @GET("discover/movie?vote_average.gte=6.5" +
            "&vote_count.gte=100" +
            "&language=100")
    fun discoverMovies(
            @Query("api_key") key: String,
            @Query("page") page: Int,
            @Query("with_genres", encoded = true) genres: String?,
            @Query("primary_release_date.lte") lessThanYear: String?,
            @Query("primary_release_date.gte") greaterThanYear: String?
    ): Observable<Page>

    @GET("movie/top_rated")
    fun getTopRated(@Query("api_key") key: String): Observable<Page>

    @GET("movie/now_playing")
    fun getNowPlaying(@Query("api_key") key: String): Observable<Page>

    @GET("movie/upcoming")
    fun getUpcoming(@Query("api_key") key: String): Observable<Page>

    @GET("movie/popular")
    fun getPopular(@Query("api_key") key: String): Observable<Page>
}