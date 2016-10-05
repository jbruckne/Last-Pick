package joebruckner.lastpick.domain

import joebruckner.lastpick.model.presentation.Filter
import joebruckner.lastpick.model.presentation.Movie
import joebruckner.lastpick.model.presentation.Showcase
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