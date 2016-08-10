package joebruckner.lastpick.source

import joebruckner.lastpick.model.guidebox.ExternalMovie
import joebruckner.lastpick.model.guidebox.GuideboxMovie
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface GuideboxService {

    @GET("search/movie/id/themoviedb/{id}")
    fun findMovieWithId(@Path("id") id: Int) : Observable<ExternalMovie>

    @GET("movie/{id}")
    fun getMovie(@Path("id") id: Int) : Observable<GuideboxMovie>
}