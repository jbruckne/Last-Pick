package joebruckner.lastpick.ui.movie

import joebruckner.lastpick.model.Filter
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.ReviewSource
import joebruckner.lastpick.model.State
import joebruckner.lastpick.model.guidebox.Source
import joebruckner.lastpick.model.tmdb.Video

class MovieContract {

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun addSubview(subview: Subview)
        fun removeSubview(subview: Subview)
        fun reloadMovie()
        fun getNextMovie()
        fun getMovieById(id: Int)
        fun shareMovie()
        fun watchVideo(video: Video)
        fun readReviews(source: ReviewSource)
        fun viewSource(source: Source)
        fun setMovie(movie: Movie)
        fun setColor(color: Int)
        fun updateFilter(filter: Filter)
        fun getFilter(): Filter
        fun updateBookmark()
        fun getBookmarkStatus(): Boolean
        fun getCurrentMovie(): Movie?
    }

    interface View {
        var state: State
        fun showLoading()
        fun showContent(movie: Movie)
        fun showError(errorMessage: String, errorButtonMessage: String, f: () -> Unit)
        fun setBookmark(isBookmarked: Boolean)
        fun showSnackbar(message: String)
    }

    interface Subview {
        fun updateMovie(movie: Movie)
        fun updateColor(color: Int)
    }
}
