package joebruckner.lastpick.ui.home.landing

import joebruckner.lastpick.data.CondensedMovie
import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.utils.applySchedulers
import rx.Observable

class LandingPresenter(val movieInteractor: MovieInteractor): LandingContract.Presenter {
    var view: LandingContract.View? = null

    override fun attachView(view: LandingContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadLists() {
        view?.showLoading()

        val popular = movieInteractor.getSpecialList(ListType.POPULAR)
        val now = movieInteractor.getSpecialList(ListType.NOW_PLAYING)
        val upcoming = movieInteractor.getSpecialList(ListType.UPCOMING)
        val top = movieInteractor.getSpecialList(ListType.TOP_RATED)

        Observable
                .merge(popular, now, upcoming, top)
                .map { it.results.first() }
                .compose(applySchedulers<CondensedMovie>())
                .toList()
                .subscribe ({
                    view?.showContent(it)
                }, { error ->
                    view?.showError(error.message ?: "Error")
                })
    }
}