package joebruckner.lastpick.domain.impl

import joebruckner.lastpick.domain.HistoryInteractor
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.source.history.LocalHistoryRepository
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryInteractorImpl @Inject constructor(
        val localHistorySource: LocalHistoryRepository
): HistoryInteractor {

    override fun getHistory(): Observable<List<Int>> {
        return localHistorySource.getHistoryEntries()
    }

    override fun addMovieToHistory(movie: Movie) {
        localHistorySource.saveHistoryEntry(movie.id)
    }
}