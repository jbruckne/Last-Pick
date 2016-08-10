package joebruckner.lastpick.ui.specials

import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.EventLogger
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.domain.MovieInteractor
import joebruckner.lastpick.model.ListType
import joebruckner.lastpick.model.tmdb.CondensedMovie
import joebruckner.lastpick.utils.applySchedulers
import javax.inject.Inject

@ActivityScope
class SpecialsPresenter @Inject constructor(
        val movieInteractor: MovieInteractor,
        val navigator: FlowNavigator,
        val logger: EventLogger
): SpecialsContract.Presenter {
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
                    logger.logError(it)
                    view?.showError("Oops, there was a problem loading the list.")
                })
    }

    override fun movieSelected(movie: CondensedMovie) {
        navigator.showMovie(movie.id)
    }
}