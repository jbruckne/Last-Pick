package joebruckner.lastpick.interactors

import joebruckner.lastpick.data.Filter
import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.Page
import rx.Observable

interface MovieInteractor {
    fun getMovie(id: Int): Observable<Movie>
    fun getMovieSuggestion(filter: Filter): Observable<Movie>
    fun getLastMovie(): Observable<Movie>
    fun getSpecialList(type: ListType): Observable<Page>
    fun resetMovieSuggestions()

    companion object {
        val OUT_OF_SUGGESTIONS = "No more movies to suggest with the current filter."
    }
}