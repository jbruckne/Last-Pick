package joebruckner.lastpick.ui.home

import joebruckner.lastpick.network.MovieManager

class LandingPresenter(val movieManager: MovieManager): LandingContract.Presenter {
    var view: LandingContract.View? = null

    override fun attachView(view: LandingContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadLists() {

    }
}