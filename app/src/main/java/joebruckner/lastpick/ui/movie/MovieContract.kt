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
        fun onRandomClicked()
        fun onMovieClicked(id: Int)
        fun onShareClicked()
        fun onTrailerClicked(video: Video)
        fun onImageClicked(imageUrl: String)
        fun onImageDismissed()
        fun onReviewSourceClicked(source: ReviewSource)
        fun onSourceClicked(source: Source)
        fun onTopClicked()
        fun setColor(color: Int)
        fun getColor(): Int
        fun onFilterDismissed(filter: Filter)
        fun getFilter(): Filter
        fun onBookmarkToggled()
        fun getBookmarkStatus(): Boolean
        fun getMovie(): Movie?
    }

    interface View {
        var state: State
        fun showLoading()
        fun showContent(movie: Movie)
        fun showError(errorMessage: String, errorButtonMessage: String, f: () -> Unit)
        fun setBookmark(isBookmarked: Boolean)
        fun showSnackbar(message: String)
        fun showImage(imageUrl: String)
        fun removeImage()
    }

    interface Subview {
        fun updateMovie(movie: Movie)
        fun updateColor(color: Int)
        fun scrollToTop()
    }
}
