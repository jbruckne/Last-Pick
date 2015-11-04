package joebruckner.lastpick.managers

import com.google.gson.JsonElement
import com.squareup.okhttp.ResponseBody
import joebruckner.lastpick.models.Movie
import joebruckner.lastpick.models.MovieSet
import org.json.JSONObject
import retrofit.Call
import retrofit.Response
import retrofit.http.GET
import retrofit.http.Header
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable



public interface TmdbService {

    companion object {
        const val api_key = "9856c489f3e6e5b5b972c6373c440210"
    }

    @GET("movie/top_rated?api_key=$api_key")
    fun getTopRatedMovies(@Query("page") page: Int): Call<MovieSet>

    @GET("movie/{id}?api_key=$api_key")
    fun getMovie(@Path("id") id: Int): Call<Movie>

}