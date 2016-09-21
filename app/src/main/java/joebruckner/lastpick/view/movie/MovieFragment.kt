package joebruckner.lastpick.view.movie

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.State
import joebruckner.lastpick.utilities.animateTint
import joebruckner.lastpick.utilities.consume
import joebruckner.lastpick.utilities.find
import joebruckner.lastpick.utilities.load
import joebruckner.lastpick.view.common.BaseFragment
import joebruckner.lastpick.widgets.FilterSheetDialogBuilder
import joebruckner.lastpick.widgets.PaletteTheme
import javax.inject.Inject

class MovieFragment() : BaseFragment(), MovieContract.View {
    // Properties
    override val layoutId = R.layout.fragment_movie_v2
    override var state = State.LOADING
    private var movie: Movie? = null
    private var restart = false
    private var showAllSummary = false

    // Objects
    @Inject lateinit var presenter: MovieContract.Presenter

    // Views
    val fab: FloatingActionButton? get() = activity?.find(R.id.fab)
    val content: NestedScrollView get() = find(R.id.movie_scroll_view)
    val loading: View get() = find(R.id.movie_loading)
    val title: TextView get() = find(R.id.movie_title)
    val info: TextView get() = find(R.id.movie_info)
    val tagline: TextView get() = find(R.id.movie_tagline)
    val summary: TextView get() = find(R.id.movie_overview_summary)
    val moreOverview: Button get() = find(R.id.movie_overview_more)
    val genres: TextView get() = find(R.id.movie_genres)
    val poster: ImageView get() = find(R.id.movie_poster)
    val backdrop: ImageView get() = find(R.id.movie_backdrop)

    // Fragment arguments
    val providedMovieId by lazy { arguments.getInt("movie_id", -1) }
    private var savedMovieJson: String? = null

    private var errorMessage: String = "Error"
    private var errorButtonMessage: String = "Try again"
    private var errorButtonListener: (() -> Unit) = {}

    private val duration = 200L

    override fun showMovie(movie: Movie) {
        this.movie = movie
        content.smoothScrollTo(0, 0)
        updateViewState(State.CONTENT)
        title.text = movie.title
        title.text = movie.title
        info.text = "${movie.releaseDate.slice(0..3)}" +
                " \u2022 ${movie.rating ?: "NR"}" +
                " \u2022 ${movie.runtime} min"
        genres.text = movie.genres.take(3).map { it.name }.joinToString(" \u2022 ")

        poster.load(context, movie.getFullPosterPath(), {
            PaletteTheme.Builder(it).generate { showColor(it.getAccentColor()) }
            poster.animate().alpha(1f).setDuration(duration).start()
        })
        backdrop.load(context, movie.getFullBackdropPath(), {
            backdrop.animate().alpha(1f).setDuration(duration).start()
        })

        showOverviewCard(movie)
    }

    private fun showOverviewCard(movie: Movie) {
        tagline.text = if (movie.tagline.isNullOrBlank()) "Overview" else movie.tagline
        updateSummary(movie)
        moreOverview.setOnClickListener {
            showAllSummary = !showAllSummary
            updateSummary(movie)
        }
    }

    private fun updateSummary(movie: Movie) {
        moreOverview.text = if (showAllSummary) "Less" else "More"
        if (showAllSummary) {
            summary.text = movie.overview
            return
        }
        if (movie.overview.length > 350) {
            moreOverview.visibility = View.VISIBLE
            summary.text = movie.overview.substring(0..300)
            summary.text = "${summary.text}..."
        } else {
            moreOverview.visibility = View.INVISIBLE
            summary.text = movie.overview
        }
    }

    private fun showColor(color: Int) {
        fab?.animateTint(color)
    }

    override fun showLoading() {
        updateViewState(State.LOADING)
    }

    override fun showError(errorMessage: String, errorButtonMessage: String, f: () -> Unit) {
        this.errorMessage = errorMessage
        this.errorButtonMessage = errorButtonMessage
        this.errorButtonListener = f
        updateViewState(State.ERROR)
    }

    private fun updateViewState(newState: State) {
        state = newState
        fab?.let {
            if(state.equals(State.CONTENT)) {
                poster.alpha = 0f
                backdrop.alpha = 0f
                loading.animate().setDuration(duration).alpha(0f).start()
            } else {
                loading.animate().setDuration(duration).alpha(1f).start()
            }
        }
    }

    override fun setBookmark(isBookmarked: Boolean) {
        // TODO
    }

    override fun showSnackbar(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

    override fun showImage(imageUrl: String) {
        val layout = activity
                .layoutInflater
                .inflate(R.layout.fragment_image, viewRoot, false) as ViewGroup
        viewRoot.addView(layout)
        val fullscreenImage = layout.find<ImageViewTouch>(R.id.fullscreen_image)
        fullscreenImage.load(context, imageUrl)
        fullscreenImage.setSingleTapListener {
            presenter.onImageDismissed()
        }
    }

    override fun removeImage() {
        val layout = viewRoot.find<View>(R.id.fullscreen_container)
        viewRoot.removeView(layout)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        restart = savedInstanceState != null
        val toolbar = view?.find<Toolbar>(R.id.movie_toolbar)
        toolbar?.inflateMenu(R.menu.menu_movie)
        return view
    }

    private fun restoreState() {
        state = State.valueOf(arguments.getString("state", State.LOADING.name))
        savedMovieJson = arguments.getString("movie")
        showAllSummary = arguments.getBoolean("showAllSummary", false)
    }

    override fun onStart() {
        super.onStart()
        Log.d(this.toString(), "onStart")

        presenter.attachView(this)

        if (!isFirstStart || restart) {
            restoreState()
            savedMovieJson?.let {
                if (state == State.CONTENT) showMovie(Gson().fromJson(it, Movie::class.java))
            }
            return
        }

        if (providedMovieId > 0) presenter.onMovieSelected(providedMovieId)
        else presenter.onRandomRequested()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        arguments.putString("state", state.name)
        arguments.putString("movie", Gson().toJson(movie))
        arguments.putBoolean("showAllSummary", showAllSummary)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        Log.d(this.toString(), "onStop")
        presenter.detachView()
        super.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_filter -> consume { showFilterSettings() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed(): Boolean {
        if (activity.findViewById(R.id.fullscreen_container) != null) {
            presenter.onImageDismissed()
            return true
        }
        return false
    }

    fun callForUpdate() {
        presenter.onRandomRequested()
    }

    fun showFilterSettings() {
        FilterSheetDialogBuilder(context, presenter.getFilter()) {
            presenter.onFilterDismissed(it)
        }
        .create()
        .show()
    }

    companion object {
        fun newInstance(movieId: Int? = null): MovieFragment {
            val fragment = MovieFragment()
            val args = Bundle()
            if (movieId != null) args.putInt("movie_id", movieId)
            fragment.arguments = args
            return fragment
        }
    }
}