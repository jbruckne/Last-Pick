package joebruckner.lastpick.view.home.landing

import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.EventLogger
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.domain.MovieInteractor
import joebruckner.lastpick.model.presentation.Showcase
import joebruckner.lastpick.model.tmdb.SlimMovie
import joebruckner.lastpick.utilities.applySchedulers
import javax.inject.Inject

@ActivityScope
class LandingPresenter @Inject constructor(
        val movieInteractor: MovieInteractor,
        val navigator: FlowNavigator,
        val logger: EventLogger
): LandingContract.Presenter {
    var view: LandingContract.View? = null

    override fun attachView(view: LandingContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadShowcases() {
        view?.showLoading()
        val ids = mutableSetOf<Int>()
        Showcase.values().forEach { getShowcase(it, ids) }
    }

    override fun onMovieClicked(movie: SlimMovie, type: Showcase) {
        navigator.showMovie(movie.id)
        logger.logShowcaseViewed(movie, type)
    }

    override fun onShowcaseClicked(type: Showcase) {
        navigator.showSpecial(type)
        logger.logSpecialListViewed(type)
    }

    private fun getShowcase(type: Showcase, ids: MutableSet<Int>) {
        movieInteractor
                .getSpecialList(type)
                .flatMapIterable { it.results }
                .filter { !ids.contains(it.id) }
                .first()
                .doOnNext { ids.add(it.id) }
                .map { Pair(it, type) }
                .retry(2)
                .compose(applySchedulers<Pair<SlimMovie, Showcase>>())
                .subscribe ({
                    view?.showContent(it.first, it.second)
                }, {
                    view?.showError("Failed to load movie", type)
                })
    }
}