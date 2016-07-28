package joebruckner.lastpick.ui.specials

import joebruckner.lastpick.data.CondensedMovie
import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.utils.applySchedulers

class SpecialsPresenter(val movieInteractor: MovieInteractor): SpecialsContract.Presenter {
    var view: SpecialsContract.View? = null

    override fun attachView(view: SpecialsContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun getSpecialList(type: ListType) {
        view?.showLoading()
        movieInteractor
                .getSpecialList(type)
                .map { it.results }
                .compose(applySchedulers<List<CondensedMovie>>())
                .subscribe ({
                    view?.showContent(it)
                }, {
                    view?.showError("Oops, there was a problem loading the list.")
                })
    }
}