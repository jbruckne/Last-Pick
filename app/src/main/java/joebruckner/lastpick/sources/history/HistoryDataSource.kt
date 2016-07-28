package joebruckner.lastpick.sources.history

import rx.Observable

interface HistoryDataSource {
    fun getHistoryEntries(): Observable<List<Int>>
    fun saveHistoryEntry(id: Int)
}