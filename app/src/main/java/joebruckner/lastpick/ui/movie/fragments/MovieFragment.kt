package joebruckner.lastpick.ui.movie.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v4.widget.ContentLoadingProgressBar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.State
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieContract
import joebruckner.lastpick.ui.movie.adapters.MovieDetailsPagerAdapter
import joebruckner.lastpick.utils.*
import joebruckner.lastpick.widgets.FilterSheetDialogBuilder
import javax.inject.Inject

class MovieFragment() : BaseFragment(),
        MovieContract.View, AppBarLayout.OnOffsetChangedListener, ViewPager.OnPageChangeListener {
    // Properties
    override val layoutId = R.layout.fragment_movie
    override var state = State.LOADING

    // Objects
    lateinit var pagerAdapter: MovieDetailsPagerAdapter
    @Inject lateinit var presenter: MovieContract.Presenter

    // Views
    val viewPager: ViewPager get() = find(R.id.view_pager)
    val loading: ContentLoadingProgressBar get() = find(R.id.loading)
    val error: View         get() = find(R.id.error)
    val errorText: TextView get() = find(R.id.error_message)
    val errorButton: Button get() = find(R.id.error_button)
    val poster          by lazy { activity.find<ImageView>(R.id.poster) }
    val backdrop        by lazy { activity.find<ImageView>(R.id.backdrop) }
    val titleArea       by lazy { activity.find<View>(R.id.title_area) }
    val title           by lazy { activity.find<TextView>(R.id.title) }
    val details         by lazy { activity.find<TextView>(R.id.details) }
    val genres          by lazy { activity.find<TextView>(R.id.genres) }

    // Fragment arguments
    val providedMovieId by lazy { arguments.getInt("movie", -1) }
    val isConfigChange  by lazy { arguments.getBoolean("isConfigChange", false) }
    val isDiscoveryMode by lazy { arguments.getBoolean("isDiscoveryMode", true) }

    // Animation values
    val ALPHA_CLEAR = 0f
    val ALPHA_HALF = 0.8f
    val ALPHA_FULL = 1f
    val OUT_DURATION: Long = 250
    val IN_DURATION: Long = 350

    private var errorMessage: String = "Error"
    private var errorButtonMessage: String = "Try again"
    private var errorButtonListener: (() -> Unit) = {}

    override fun showContent(movie: Movie) {
        updateViewState(State.CONTENT)
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

        when (state) {
            State.LOADING -> {
                clearMovie()
                parent.appBar?.setExpanded(false, true)
            }
            State.CONTENT -> {
                showMovie(presenter.getMovie())
                if (isDiscoveryMode) parent.enableFab()
                if (!isConfigChange) parent.appBar?.setExpanded(true, true)
                parent.appBar?.addOnOffsetChangedListener(this)
            }
            State.ERROR -> {
                clearMovie()
                errorText.text = errorMessage
                errorButton.text = errorButtonMessage
                errorButton.setOnClickListener { errorButtonListener.invoke() }
                parent.appBar?.setExpanded(false, true)
            }
        }

        // Set visibility of views
        titleArea.visibleIf(state == State.CONTENT)
        viewPager.visibleIf(state == State.CONTENT)
        loading.visibleIf(state == State.LOADING)
        error.visibleIf(state == State.ERROR)

        // Update menu items
        parent.supportInvalidateOptionsMenu()
    }

    private fun clearMovie() {
        backdrop.animate().alpha(ALPHA_CLEAR).duration = OUT_DURATION
        poster.animate().alpha(ALPHA_CLEAR).duration = OUT_DURATION
        parent.disableFab()
        parent.title = ""
    }

    private fun showMovie(movie: Movie?) {
        if (view == null || movie == null) return

        // Set screen title
        parent.title = movie.title
        title.text = movie.title

        // Load backdrop
        backdrop.loadWithPalette(
                parent.applicationContext,
                movie.getFullBackdropPath(),
                IN_DURATION, ALPHA_HALF
        ) {}
        backdrop.setOnClickListener {
            presenter.onImageClicked(movie.getFullBackdropPath())
        }

        // Load poster
        poster.loadWithPalette(
                parent.applicationContext,
                movie.getFullPosterPath(),
                IN_DURATION,
                ALPHA_FULL
        ) { theme ->
            if (activity != null) parent.setAccent(theme.getAccentColor())
            loading
                    .indeterminateDrawable
                    .setColorFilter(theme.getAccentColor(), PorterDuff.Mode.MULTIPLY)
            presenter.setColor(theme.getAccentColor())
        }
        poster.setOnClickListener {
            presenter.onImageClicked(movie.getFullPosterPath())
        }

        // Set movie details
        val builder = StringBuilder()
        builder.append(movie.releaseDate.substring(0..3))
        movie.rating?.let { builder.append(" $it ") }
        if (movie.runtime > 0) builder.append(" ${movie.runtime} min")
        details.text = builder.toString()

        // Set genres
        var genreText = ""
        movie.genres.forEachIndexed { i, genre ->
            if (i < 3) genreText += "${genre.name}, "
        }
        if (genreText.isNotBlank()) genreText = genreText.dropLast(2)
        genres.text = genreText
    }

    override fun setBookmark(isBookmarked: Boolean) {
        parent.supportInvalidateOptionsMenu()
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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = MovieDetailsPagerAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.removeOnPageChangeListener(this)
        viewPager.addOnPageChangeListener(this)
        parent.tabLayout?.let { layout ->
            viewPager.currentItem = layout.selectedTabPosition
            layout.setupWithViewPager(viewPager)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val expansion = Math.abs(offset.toFloat() / appBarLayout.totalScrollRange.toFloat())
        if (expansion <= 0.2) {
            presenter.onTopClicked()
            appBarLayout.removeOnOffsetChangedListener(this)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "Starting")

        // Fix orientation change bug with scrollView overlap
        parent.appBar?.addOnOffsetChangedListener { appBarLayout, i ->
            val expansion = Math.abs(i.toFloat() / appBarLayout.totalScrollRange.toFloat())
            parent.supportActionBar?.setDisplayShowTitleEnabled(expansion >= 0.9)
        }

        if (!isFirstStart) return

        // Initialization of presenter
        presenter.attachView(this)

        // Load correct type of movie
        if (isConfigChange) presenter.reloadMovie()
        else if (providedMovieId > 0) presenter.onMovieClicked(providedMovieId)
        else presenter.onRandomClicked()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        presenter.detachView()
        super.onPause()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        try {
            val bookmark = menu.findItem(R.id.action_bookmark)
            bookmark?.setIcon(
                    if (presenter.getBookmarkStatus()) R.drawable.ic_bookmark_24dp
                    else R.drawable.ic_bookmark_outline_24dp
            )
        } catch (e: Exception) {}
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_filter -> consume { showFilterSettings() }
        R.id.action_bookmark -> consume { presenter.onBookmarkToggled() }
        R.id.action_share -> consume { presenter.onShareClicked() }
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
        presenter.onRandomClicked()
    }

    fun showFilterSettings() {
        FilterSheetDialogBuilder(context, presenter.getFilter()) {
            presenter.onFilterDismissed(it)
        }
        .create()
        .show()
    }

    override fun onPageScrollStateChanged(state: Int) {
        //Log.d(logTag, "State Changed: $state")
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //Log.d(logTag, "Scrolled: $position, $positionOffset, $positionOffsetPixels")
    }

    override fun onPageSelected(position: Int) {
        //Log.d(logTag, "Selected: $position")
    }

    companion object {
        fun newInstance(isDiscoveryMode: Boolean, isConfigChange: Boolean, movieId: Int? = null): MovieFragment {
            val fragment = MovieFragment()
            val args = Bundle()
            args.putBoolean("isDiscoveryMode", isDiscoveryMode)
            args.putBoolean("isConfigChange", isConfigChange)
            if (movieId != null) args.putInt("movie", movieId)
            fragment.arguments = args
            return fragment
        }
    }
}