package joebruckner.lastpick.view.specials

import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.EventLogger
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.domain.MovieInteractor
import joebruckner.lastpick.model.presentation.Showcase
import joebruckner.lastpick.model.tmdb.SlimMovie
import joebruckner.lastpick.utilities.applySchedulers
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

    override fun getSpecialList(type: Showcase) {
        view?.showLoading()
        movieInteractor
                .getSpecialList(type)
                .map { it.results }
                .compose(applySchedulers<List<SlimMovie>>())
                .subscribe ({
                    view?.showContent(it)
                }, {
                    logger.logError(it)
                    view?.showError("Oops, there was a problem loading the list.")
                })
    }

    override fun movieSelected(movie: SlimMovie) {
        navigator.showMovie(movie.id)
    }
}