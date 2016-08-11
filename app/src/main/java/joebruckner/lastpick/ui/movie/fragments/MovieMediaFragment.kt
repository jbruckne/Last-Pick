package joebruckner.lastpick.ui.movie.fragments

import android.support.v7.widget.RecyclerView
import android.widget.Button
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieContract
import joebruckner.lastpick.ui.movie.adapters.ImageAdapter
import joebruckner.lastpick.ui.movie.adapters.TrailerAdapter
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.fullPath
import joebruckner.lastpick.utils.visibleIf
import javax.inject.Inject

class MovieMediaFragment : BaseFragment(), MovieContract.Subview {
    // Parameters
    override val layoutId: Int = R.layout.fragment_movie_media

    // Objects
    @Inject lateinit var presenter: MovieContract.Presenter
    @Inject lateinit var trailerAdapter: TrailerAdapter
    @Inject lateinit var imageAdapter: ImageAdapter

    // Views
    val trailerList: RecyclerView get() = find(R.id.trailer_list)
    val imageList: RecyclerView get() = find(R.id.image_list)
    val viewMoreTrailers: Button get() = find(R.id.view_more_trailers)
    val viewMoreImages: Button get() = find(R.id.view_more_images)

    override fun onStart() {
        super.onStart()
        trailerAdapter.listener = { video ->
            presenter.onTrailerClicked(video)
        }
        trailerList.adapter = trailerAdapter
        imageAdapter.listener = { image, view ->
            presenter.onImageClicked(image.fullPath())
        }
        imageList.adapter = imageAdapter
        presenter.addSubview(this)
    }

    override fun onStop() {
        presenter.removeSubview(this)
        super.onStop()
    }

    override fun updateMovie(movie: Movie) {
        updateView()
    }

    override fun updateColor(color: Int) {
        viewMoreTrailers.setTextColor(color)
        viewMoreImages.setTextColor(color)
    }

    fun updateView() {
        if (view == null || activity == null) return
        presenter.getMovie()?.let {
            trailerAdapter.videos = it.videos
            imageAdapter.setNewItems(it.posters, it.backdrops)
        }
        viewMoreImages.visibleIf(
                presenter.getMovie()?.posters?.isNotEmpty() ?: false ||
                presenter.getMovie()?.backdrops?.isNotEmpty() ?: false
        )
        viewMoreTrailers.visibleIf(
                presenter.getMovie()?.videos?.isNotEmpty() ?: false
        )
    }
}