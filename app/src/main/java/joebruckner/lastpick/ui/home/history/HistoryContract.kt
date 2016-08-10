package joebruckner.lastpick.ui.home.history

import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.State

class HistoryContract {
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun getHistory()
    }
    interface View {
        fun showContent(movies: List<Movie>)
        fun showError(errorMessage: String)
        fun showLoading()
        var state: State
    }
}
