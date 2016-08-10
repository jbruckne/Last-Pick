package joebruckner.lastpick.source.movie

import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.ListType
import joebruckner.lastpick.model.tmdb.Page
import rx.Observable

interface MovieDataSource {
    fun getMovie(id: Int): Observable<Movie>
    fun getPage(page: Int, filter: Filter): Observable<Page>
    fun getSpecialList(type: ListType): Observable<Page>
    fun saveMovieEntry(movie: Movie): Unit
}