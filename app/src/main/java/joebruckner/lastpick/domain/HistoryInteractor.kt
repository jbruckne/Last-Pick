package joebruckner.lastpick.domain

import joebruckner.lastpick.model.Movie
import rx.Observable

interface HistoryInteractor {
    fun getHistory(): Observable<List<Int>>
    fun addMovieToHistory(movie: Movie)
}