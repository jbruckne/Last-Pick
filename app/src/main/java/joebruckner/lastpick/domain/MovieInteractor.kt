package joebruckner.lastpick.domain

import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.Showcase
import joebruckner.lastpick.model.tmdb.Page
import rx.Observable

interface MovieInteractor {
    fun getMovie(id: Int): Observable<Movie>
    fun getMovieSuggestion(filter: Filter): Observable<Movie>
    fun getLastMovie(): Observable<Movie>
    fun getSpecialList(type: Showcase): Observable<Page>
    fun resetMovieSuggestions()

    companion object {
        val OUT_OF_SUGGESTIONS = "No more movies to suggest with the current filter."
    }
}