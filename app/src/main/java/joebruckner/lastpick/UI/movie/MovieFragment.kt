package joebruckner.lastpick.ui.movie

import android.content.Intent
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
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.State
import joebruckner.lastpick.interactors.BookmarkInteractor
import joebruckner.lastpick.interactors.HistoryInteractor
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.adapters.MovieDetailsPagerAdapter
import joebruckner.lastpick.utils.consume
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.loadWithPalette
import joebruckner.lastpick.utils.visibleIf
import joebruckner.lastpick.widgets.FilterSheetDialogBuilder

class MovieFragment() : BaseFragment(), MovieContract.View {
    override val layoutId = R.layout.fragment_movie
    override var state = State.LOADING

    private var movie: Movie? = null
    private var errorMessage: String = "Error"
    private var errorButtonMessage: String = "Try again"
    private var errorButtonListener: (() -> Unit) = {}

    // Fragment arguments
    val providedMovieId by lazy { arguments.getInt("movie", -1) }
    val isConfigChange  by lazy { arguments.getBoolean("isConfigChange", false) }
    val isDiscoveryMode by lazy { arguments.getBoolean("isDiscoveryMode", true) }

    // TODO Move these to injection
    lateinit var pagerAdapter: MovieDetailsPagerAdapter
    val presenter: MovieContract.Presenter by lazy {
        MoviePresenter(
            activity.application.getSystemService(LastPickApp.MOVIE_MANAGER) as MovieInteractor,
            activity.application.getSystemService(LastPickApp.HISTORY_MANAGER) as HistoryInteractor,
            activity.application.getSystemService(LastPickApp.BOOKMARKS_MANAGER) as BookmarkInteractor
        )
    }

    // Views
    val content         by lazy { find<ViewPager>(R.id.view_pager) }
    val loading         by lazy { find<View>(R.id.loading) }
    val error           by lazy { find<View>(R.id.error) }
    val errorText       by lazy { find<TextView>(R.id.error_message) }
    val errorButton     by lazy { find<Button>(R.id.error_button) }
    val poster          by lazy { activity.find<ImageView>(R.id.poster) }
    val backdrop        by lazy { activity.find<ImageView>(R.id.backdrop) }
    val titleArea       by lazy { activity.find<View>(R.id.title_area) }
    val title           by lazy { activity.find<TextView>(R.id.title) }
    val details         by lazy { activity.find<TextView>(R.id.details) }
    val genres          by lazy { activity.find<TextView>(R.id.genres) }
    val rating          by lazy { activity.find<TextView>(R.id.rating) }

    // Animation values
    val ALPHA_CLEAR = 0f
    val ALPHA_HALF = 0.8f
    val ALPHA_FULL = 1f
    val OUT_DURATION: Long = 250
    val IN_DURATION: Long = 350

    override fun showLoading() = updateViewState(State.LOADING)

    override fun showError(errorMessage: String, errorButtonMessage: String, f: () -> Unit) {
        this.errorMessage = errorMessage
        this.errorButtonMessage = errorButtonMessage
        this.errorButtonListener = f
        updateViewState(State.ERROR)
    }

    override fun showContent(movie: Movie) {
        this.movie = movie
        updateViewState(State.CONTENT)
    }

    private fun updateViewState(newState: State) {
        state = newState

        when (state) {
            State.LOADING -> {
                clearMovie()
                parent.appBar?.setExpanded(false, true)
            }
            State.CONTENT -> {
                showMovie(movie)
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
        if (baseView == null || movie == null) return

        // Set screen title
        parent.title = movie.title
        title.text = movie.title

        // Load backdrop
        backdrop.loadWithPalette(
                parent.applicationContext,
                movie.getFullBackdropPath(),
                IN_DURATION, ALPHA_HALF
        ) { theme ->
            if (activity != null) {
                //parent.setPrimary(theme.getPrimaryColor())
                //titleArea.setBackgroundColor(theme.getPrimaryColor())

            }
        }

        // Load poster
        poster.loadWithPalette(
                parent.applicationContext,
                movie.getFullPosterPath(),
                IN_DURATION,
                ALPHA_FULL
        ) { theme ->
            if (activity != null) parent.setAccent(theme.getAccentColor())

        }

        // Set rating
        rating.text = movie.voteAverage.toString()

        // Set movie details
        details.text    = "${movie.releaseDate.substring(0..3)} " +
                "  ${movie.getSimpleMpaa()}  " +
                " ${movie.runtime} min"

        // Set genres
        var genreText = ""
        movie.genres.forEachIndexed { i, genre ->
            if (i < 3) genreText += "${genre.name}, "
        }
        if (genreText.isNotBlank()) genreText = genreText.dropLast(2)
        genres.text = genreText

        pagerAdapter.updateMovie(movie)
    }

    override fun showBookmarkUpdate(isBookmarked: Boolean, notify: Boolean) {
        parent.supportInvalidateOptionsMenu()
        if (!notify) return
        Snackbar.make(view!!,
                if (isBookmarked) "Bookmark added"
                else "Bookmark removed", Snackbar.LENGTH_SHORT).show()
    }

    override fun showBookmarkError(isBookmarked: Boolean) {
        Snackbar.make(view!!,
                if (!isBookmarked) "Failed to add bookmark"
                else "Failed to remove bookmark", Snackbar.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(logTag, "Starting")

        // Fix orientation change bug with scrollView overlap
        parent.appBar?.addOnOffsetChangedListener { appBarLayout, i ->
            val expansion = Math.abs(i.toFloat() / appBarLayout.totalScrollRange.toFloat())
            parent.supportActionBar?.setDisplayShowTitleEnabled(expansion >= 0.9)
            pagerAdapter.scrollToTop()
        }

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

        if (!isFirstStart) return

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
        } catch (e: Exception) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_filter -> consume { showFilterSettings() }
        R.id.action_bookmark -> consume { presenter.updateBookmark() }
        R.id.action_share -> consume { share() }
        else -> super.onOptionsItemSelected(item)
    }

    fun share() {
        val link = "http://www.themoviedb.org/movie/${presenter.getCurrentMovie()?.id}"
        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this movie!")
        intent.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(Intent.createChooser(intent, "Share with"))
    }

    fun callForUpdate() {
        presenter.getNextMovie()
    }

    fun showFilterSettings() {
        FilterSheetDialogBuilder(
                context,
                presenter.isShowingAll(),
                presenter.getSelectedGenres(),
                presenter.getGte(),
                presenter.getLte()
        ) { all, selected, gte, lte -> presenter.updateFilter(all, selected, gte, lte) }
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