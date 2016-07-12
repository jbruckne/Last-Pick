package joebruckner.lastpick.ui.home

import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.data.Movie

class LandingContract {

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadLists()
    }

    interface View {
        fun showLoading()
        fun showError(message: String)
        fun showContent(movies: List<Movie>, type: ListType)
    }
}