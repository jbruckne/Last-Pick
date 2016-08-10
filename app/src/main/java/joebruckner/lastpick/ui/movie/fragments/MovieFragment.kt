package joebruckner.lastpick.ui.movie.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.State
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieContract
import joebruckner.lastpick.ui.movie.adapters.MovieDetailsPagerAdapter
import joebruckner.lastpick.utils.consume
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.loadWithPalette
import joebruckner.lastpick.utils.visibleIf
import joebruckner.lastpick.widgets.FilterSheetDialogBuilder
import javax.inject.Inject

class MovieFragment() : BaseFragment(), MovieContract.View {
    // Overridden properties
    override val layoutId = R.layout.fragment_movie
    override var state = State.LOADING

    // Injected objects
    lateinit var pagerAdapter: MovieDetailsPagerAdapter
    @Inject lateinit var presenter: MovieContract.Presenter

    // Views
    val content: ViewPager  get() = find(R.id.view_pager)
    val loading: View       get() = find(R.id.loading)
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
                showMovie(presenter.getCurrentMovie())
                if (isDiscoveryMode) parent.enableFab()
                parent.appBar?.setExpanded(true, true)
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
        content.visibleIf(state == State.CONTENT)
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

        // Load poster
        poster.loadWithPalette(
                parent.applicationContext,
                movie.getFullPosterPath(),
                IN_DURATION,
                ALPHA_FULL
        ) { theme ->
            if (activity != null) parent.setAccent(theme.getAccentColor())
            presenter.setColor(theme.getAccentColor())
        }

        // Set movie details
        details.text    = "${movie.releaseDate.substring(0..3)} " +
                "  ${movie.rating}  " +
                " ${movie.runtime} min"

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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "Starting")

        // Fix orientation change bug with scrollView overlap
        parent.appBar?.addOnOffsetChangedListener { appBarLayout, i ->
            val expansion = Math.abs(i.toFloat() / appBarLayout.totalScrollRange.toFloat())
            parent.supportActionBar?.setDisplayShowTitleEnabled(expansion >= 0.9)
            childFragmentManager.fragments?.forEach {
                if (it is MovieInfoFragment) it.scrollToTop()
            }
        }

        if (!isFirstStart) return

        // Set up tabs
        parent.tabLayout?.let { layout ->
            pagerAdapter = MovieDetailsPagerAdapter(childFragmentManager)
            content.adapter = pagerAdapter
            layout.setupWithViewPager(content)
            content.clearOnPageChangeListeners()
            content.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(layout))
            layout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) { }
                override fun onTabUnselected(tab: TabLayout.Tab) { }
                override fun onTabSelected(tab: TabLayout.Tab) {
                    content.setCurrentItem(tab.position, true)
                }
            })
        }

        // Initialization of presenter
        presenter.attachView(this)

        // Load correct type of movie
        if (isConfigChange) presenter.reloadMovie()
        else if (providedMovieId > 0) presenter.getMovieById(providedMovieId)
        else presenter.getNextMovie()
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
        R.id.action_bookmark -> consume { presenter.updateBookmark() }
        R.id.action_share -> consume { presenter.shareMovie() }
        else -> super.onOptionsItemSelected(item)
    }

    fun callForUpdate() {
        presenter.getNextMovie()
    }

    fun showFilterSettings() {
        FilterSheetDialogBuilder(context, presenter.getFilter()) {
            presenter.updateFilter(it)
        }
        .create()
        .show()
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