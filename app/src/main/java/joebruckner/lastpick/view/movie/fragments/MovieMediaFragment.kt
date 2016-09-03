package joebruckner.lastpick.ui.movie.fragments

import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.Button
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieContract
import joebruckner.lastpick.ui.movie.adapters.ImageAdapter
import joebruckner.lastpick.ui.movie.adapters.TrailerAdapter
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.fullPath
import joebruckner.lastpick.utils.shuffle
import joebruckner.lastpick.utils.visibleIf
import javax.inject.Inject

class MovieMediaFragment : BaseFragment(), MovieContract.Subview {
    // Parameters
    override val layoutId: Int = R.layout.fragment_movie_media

    // Objects
    @Inject lateinit var presenter: MovieContract.Presenter
    @Inject lateinit var trailerAdapter: TrailerAdapter
    @Inject lateinit var imageAdapter: ImageAdapter
    @Inject lateinit var imageLayoutManager: StaggeredGridLayoutManager

    // Views
    val trailerList: RecyclerView get() = find(R.id.trailer_list)
    val imageList: RecyclerView get() = find(R.id.image_list)
    val viewMoreTrailers: Button get() = find(R.id.view_more_trailers)
    val viewMoreImages: Button get() = find(R.id.view_more_images)
    val trailersTitle: TextView get() = find(R.id.trailers_title)
    val imagesTitle: TextView get() = find(R.id.images_title)

    override fun onStart() {
        super.onStart()
        trailerAdapter.listener = { video ->
            presenter.onTrailerClicked(video)
        }
        trailerList.adapter = trailerAdapter
        viewMoreTrailers.setOnClickListener {
            trailerAdapter.showAll = !trailerAdapter.showAll
            viewMoreTrailers.text = if (trailerAdapter.showAll) "View Less" else "View More"
        }
        imageAdapter.listener = { image, view ->
            presenter.onImageClicked(image.fullPath())
        }
        imageList.adapter = imageAdapter
        imageList.layoutManager = imageLayoutManager
        viewMoreImages.setOnClickListener {
            imageAdapter.showAll = !imageAdapter.showAll
            imageLayoutManager.spanCount =  if (imageAdapter.itemCount <= 0) 0
                                            else imageAdapter.itemCount / 3 + 1
            viewMoreImages.text = if (imageAdapter.showAll) "View Less" else "View More"
        }
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

    override fun scrollToTop() {
        find<NestedScrollView>(R.id.scroll_view).scrollTo(0, 0)
    }

    fun updateView() {
        if (view == null || activity == null) return
        presenter.getMovie()?.let {
            trailerAdapter.videos = it.videos
            imageAdapter.setNewItems(it.posters.shuffle(), it.backdrops.shuffle())
        }

        val numberOfTrailers = presenter.getMovie()?.videos?.size ?: 0
        viewMoreTrailers.visibleIf(numberOfTrailers > 3)
        trailersTitle.visibleIf(numberOfTrailers > 0)

        val numberOfPosters = presenter.getMovie()?.posters?.size ?: 0
        val numberOfBackdrops = presenter.getMovie()?.posters?.size ?: 0
        viewMoreImages.visibleIf(numberOfPosters + numberOfBackdrops > 5)
        imagesTitle.visibleIf(numberOfPosters + numberOfBackdrops > 0)

        scrollToTop()
    }
}