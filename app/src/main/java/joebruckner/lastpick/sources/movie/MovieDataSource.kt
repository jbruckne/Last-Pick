package joebruckner.lastpick.sources.movie

import joebruckner.lastpick.data.Filter
import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Page
import rx.Observable

interface MovieDataSource {
    fun getMovie(id: Int): Observable<Movie>
    fun getPage(page: Int, filter: Filter): Observable<Page>
    fun getSpecialList(type: ListType): Observable<Page>
    fun saveMovieEntry(movie: Movie): Unit
}