package joebruckner.lastpick.view.home.bookmark

import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.State

class BookmarkContract {
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun getBookmarks()
    }
    interface View {
        fun showContent(movies: List<Movie>)
        fun showError(errorMessage: String)
        fun showLoading()
        var state: State
    }
}
