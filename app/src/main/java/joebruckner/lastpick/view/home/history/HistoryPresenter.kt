package joebruckner.lastpick.view.home.history

import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.domain.HistoryInteractor
import joebruckner.lastpick.domain.MovieInteractor
import joebruckner.lastpick.model.presentation.Movie
import joebruckner.lastpick.utilities.applySchedulers
import javax.inject.Inject

@ActivityScope
class HistoryPresenter @Inject constructor(
        val movieManager: MovieInteractor,
        val historyManager: HistoryInteractor,
        val navigator: FlowNavigator
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