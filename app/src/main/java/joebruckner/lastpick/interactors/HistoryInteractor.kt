package joebruckner.lastpick.interactors

import joebruckner.lastpick.data.Movie
import rx.Observable

interface HistoryInteractor {
    fun getHistory(): Observable<List<Int>>
    fun addMovieToHistory(movie: Movie)
}