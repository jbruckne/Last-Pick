package joebruckner.lastpick.source.history

import rx.Observable

interface HistoryRepository {
    fun getHistoryEntries(): Observable<List<Int>>
    fun saveHistoryEntry(id: Int)
}