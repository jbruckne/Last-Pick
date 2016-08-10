package joebruckner.lastpick.ui.home.landing

import joebruckner.lastpick.model.tmdb.CondensedMovie
import joebruckner.lastpick.model.State

class LandingContract {

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadLists()
    }

    interface View {
        var state: State
        fun showLoading()
        fun showError(message: String)
        fun showContent(movies: List<CondensedMovie>)
    }
}