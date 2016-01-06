package joebruckner.lastpick.network

import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.MovieEvent
import joebruckner.lastpick.data.RecentHistoryEvent
import joebruckner.lastpick.data.RecentHistoryRequest

class HistoryManager(val bus: Bus, val amount: Int) {
    val history = arrayListOf<Movie>()

    init {
        bus.register(this)
    }

    @Subscribe fun movieAvailable(event: MovieEvent) {
        if (!history.contains(event.movie)) history.add(0, event.movie)
    }

    @Subscribe fun historyRequested(request: RecentHistoryRequest) {
        bus.post(RecentHistoryEvent(history))
    }
}