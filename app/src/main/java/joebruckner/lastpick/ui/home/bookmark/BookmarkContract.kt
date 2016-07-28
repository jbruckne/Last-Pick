package joebruckner.lastpick.ui.home.bookmark

import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.State

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
