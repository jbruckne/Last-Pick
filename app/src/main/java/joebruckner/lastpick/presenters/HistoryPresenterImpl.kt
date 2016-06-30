package joebruckner.lastpick.presenters

import joebruckner.lastpick.network.HistoryManager
import joebruckner.lastpick.presenters.HistoryPresenter.HistoryView

class HistoryPresenterImpl(val historyManager: HistoryManager): HistoryPresenter {
    private var view: HistoryView? = null

    override fun attachView(view: HistoryView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun getHistory() {
        view?.showContent(historyManager.getHistory().toList().asReversed())
    }
}