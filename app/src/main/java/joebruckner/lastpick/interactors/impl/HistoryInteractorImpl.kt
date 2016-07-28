package joebruckner.lastpick.interactors.impl

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.interactors.HistoryInteractor
import joebruckner.lastpick.sources.history.LocalHistoryDataSource
import rx.Observable

class HistoryInteractorImpl(
        val localHistorySource: LocalHistoryDataSource
): HistoryInteractor {

    override fun getHistory(): Observable<List<Int>> {
        return localHistorySource.getHistoryEntries()
    }

    override fun addMovieToHistory(movie: Movie) {
        localHistorySource.saveHistoryEntry(movie.id)
    }
}