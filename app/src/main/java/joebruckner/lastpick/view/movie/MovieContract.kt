package joebruckner.lastpick.view.movie

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
        fun onRandomRequested()
        fun onMovieSelected(id: Int)
        fun onShareSelected()
        fun onAddToSelected()
        fun onBookmarkToggled()
        fun onSeenToggle()
        fun onTrailerSelected(video: Video)
        fun onImageSelected(imageUrl: String)
        fun onImageDismissed()
        fun onReviewSelected(source: ReviewSource)
        fun onSourceSelected(source: Source)
        fun onFilterDismissed(filter: Filter)
        fun getFilter(): Filter
    }

    interface View {
        var state: State
        fun showLoading()
        fun showMovie(movie: Movie)
        fun showError(errorMessage: String, errorButtonMessage: String, f: () -> Unit)
        fun setBookmark(isBookmarked: Boolean)
        fun showSnackbar(message: String)
        fun showImage(imageUrl: String)
        fun removeImage()
    }
}
