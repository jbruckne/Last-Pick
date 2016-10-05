package joebruckner.lastpick.domain

import joebruckner.lastpick.model.presentation.Movie
import rx.Observable

interface HistoryInteractor {
    fun getHistory(): Observable<List<Int>>
    fun addMovieToHistory(movie: Movie)
}