package joebruckner.lastpick.presenters

import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.HistoryEvent
import joebruckner.lastpick.presenters.HistoryPresenter.HistoryView
import java.util.*

class HistoryPresenterImpl(val bus: Bus): HistoryPresenter {
    var view: HistoryView? = null

    override fun attachView(view: HistoryView) {
        this.view = view
        bus.register(this)
    }

    override fun detachView() {
        bus.unregister(this)
        this.view = null
    }

    override fun getHistory() {
        bus.post(HistoryEvent())
    }

    @Subscribe fun historyAvailable(movies: ArrayList<Movie>) {
        view?.showContent(movies)
    }
}