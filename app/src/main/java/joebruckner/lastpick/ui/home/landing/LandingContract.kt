package joebruckner.lastpick.ui.home.landing

import joebruckner.lastpick.model.Showcase
import joebruckner.lastpick.model.State
import joebruckner.lastpick.model.tmdb.SlimMovie

class LandingContract {

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadShowcases()
    }

    interface View {
        var state: State
        fun showLoading()
        fun showError(message: String, type: Showcase)
        fun showContent(movie: SlimMovie, type: Showcase)
    }
}