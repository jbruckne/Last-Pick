package joebruckner.lastpick.presenters

import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.events.RecentHistoryEvent
import joebruckner.lastpick.events.RecentHistoryRequest
import joebruckner.lastpick.presenters.HistoryPresenter.HistoryView

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
        bus.post(RecentHistoryRequest())
    }

    @Subscribe fun newRecentHistory(event: RecentHistoryEvent) {
        view?.showContent(event.history)
    }
}