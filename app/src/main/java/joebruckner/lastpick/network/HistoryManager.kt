package joebruckner.lastpick.network

import android.util.Log
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.HistoryEvent

class HistoryManager(val bus: Bus, val amount: Int) {
    val history = arrayListOf<Movie>()

    init {
        bus.register(this)
    }

    @Subscribe fun movieAvailable(movie: Movie) {
        history.add(0, movie)
    }

    @Subscribe fun historyRequested(event: HistoryEvent) {
        bus.post(history)
    }
}