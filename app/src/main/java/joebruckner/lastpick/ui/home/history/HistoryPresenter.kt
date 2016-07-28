package joebruckner.lastpick.ui.home.history

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.interactors.HistoryInteractor
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.utils.applySchedulers

class HistoryPresenter(
        val movieManager: MovieInteractor,
        val historyManager: HistoryInteractor
): HistoryContract.Presenter {
    private var view: HistoryContract.View? = null

    override fun attachView(view: HistoryContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun getHistory() {
        view?.showLoading()
        historyManager
                .getHistory()
                .flatMapIterable { it }
                .flatMap { movieManager.getMovie(it) }
                .toList()
                .compose(applySchedulers<List<Movie>>())
                .subscribe ({
                    view?.showContent(it)
                }, { error ->
                    error.printStackTrace()
                    view?.showError("Oops, there was a problem loading your history.")
                })
    }
}