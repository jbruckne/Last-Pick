package joebruckner.lastpick.ui.movie

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import joebruckner.lastpick.*
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.State
import joebruckner.lastpick.network.BookmarkManager
import joebruckner.lastpick.network.MovieManager
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.widgets.FilterSheetDialogBuilder

class MovieFragment() : BaseFragment(), MovieContract.View {
    override val layoutId = R.layout.fragment_movie
    override var state = State.LOADING

    private var inDiscoveryMode = false
    private var movie: Movie? = null
    private var errorMessage: String? = null
    private var errorButtonMessage: String? = null
    private var errorButtonListener: (() -> Unit)? = null

    // Fragment arguments
    val providedMovieId by lazy { arguments.getInt("movie", -1) }
    val isConfigChange  by lazy { arguments.getBoolean("isConfigChange", false) }

    lateinit var adapter: CastAdapter
    val presenter: MovieContract.Presenter by lazy {
        MoviePresenter(
            activity.application.getSystemService(LastPickApp.MOVIE_MANAGER) as MovieManager,
            activity.application.getSystemService(LastPickApp.BOOKMARKS_MANAGER) as BookmarkManager
        )
    }

    // Views
    val content         by lazy { find<ViewPager>(R.id.content) }
    val loading         by lazy { find<View>(R.id.loading) }
    val error           by lazy { find<View>(R.id.error) }
    val errorText       by lazy { find<TextView>(R.id.error_message) }
    val errorButton     by lazy { find<Button>(R.id.error_button) }
    val poster          by lazy { activity.find<ImageView>(R.id.poster) }
    val backdrop        by lazy { activity.find<ImageView>(R.id.backdrop) }
    val titleArea       by lazy { activity.find<View>(R.id.title_area) }
    val title           by lazy { activity.find<TextView>(R.id.title) }
    val castList        by lazy { find<RecyclerView>(R.id.cast_list) }
    val scrollView      by lazy { find<NestedScrollView>(R.id.nested_scroll_view) }
    val summary         by lazy { find<TextView>(R.id.overview) }
    val details         by lazy { activity.find<TextView>(R.id.details) }
    val genres          by lazy { activity.find<TextView>(R.id.genres) }
    val phrase          by lazy { find<TextView>(R.id.phrase) }

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
        updateViewState(State.LOADING)
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
                if (inDiscoveryMode) parent.enableFab()
                parent.appBar?.setExpanded(true, true)
            }
            State.ERROR -> {
                clearMovie()
                errorText.text = errorMessage
                errorButton.text = errorButtonMessage
                parent.appBar?.setExpanded(false, true)
            }
        }

        // Set visibility of views
        titleArea.visibleIf(state == State.CONTENT)
        content.visibleIf(state == State.CONTENT)
        loading.visibleIf(state == State.LOADING)
        error.visibleIf(state == State.ERROR)

        // Reset scrollView location
        //scrollView.scrollTo(0, 0)

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
                parent.setPrimary(theme.getPrimaryColor())
                titleArea.setBackgroundColor(theme.getPrimaryColor())

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

        // Set movie details
        summary.text    = "${movie.overview}"
        details.text    = "${movie.releaseDate.substring(0..3)}   " +
                "${movie.getSimpleMpaa()}   " +
                "${movie.runtime} min"
        phrase.text     = movie.tagline

        // Set genres
        var genreText = ""
        movie.genres.forEachIndexed { i, genre ->
            if (i < 3) genreText += "${genre.name}, "
        }
        if (genreText.isNotBlank()) genreText = genreText.dropLast(2)
        genres.text = genreText

        // Update actors list
        adapter.cast = movie.credits.cast
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

        // Fix orientation change bug with scrollView overlap
        parent.appBar?.addOnOffsetChangedListener { appBarLayout, i ->
            val expansion = Math.abs(i.toFloat() / appBarLayout.totalScrollRange.toFloat())
            parent.supportActionBar?.setDisplayShowTitleEnabled(expansion >= 0.9)
            //scrollView.scrollTo(0, 0)
        }

        // Set up tabs
        parent.tabLayout?.let { layout ->
            layout.addTab(layout.newTab().setText("Info"), 0)
            layout.addTab(layout.newTab().setText("Videos"), 1)
            val pagerAdapter = MovieDetailsPagerAdapter(fragmentManager)
            content.adapter = pagerAdapter
            content.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(layout))
            layout.setOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) { }
                override fun onTabUnselected(tab: TabLayout.Tab) { }
                override fun onTabSelected(tab: TabLayout.Tab) {
                    content.setCurrentItem(tab.position, true)
                }
            })
        }

        // Set up cast list
        adapter = CastAdapter(context)
        castList.adapter = adapter

        // Initialization of presenter
        presenter.attachView(this)

        if (!isFirstStart) return

        // Load correct type of movie
        if (isConfigChange) presenter.reloadMovie()
        else if (providedMovieId > 0) presenter.getMovieById(providedMovieId)
        else {
            inDiscoveryMode = true
            presenter.getNextMovie()
        }
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
                presenter.getSelectedGenres(),
                presenter.getGte(),
                presenter.getLte()
        ) { selected, gte, lte -> presenter.updateFilter(selected, gte, lte) }
        .create()
        .show()
    }

    companion object {
        fun newInstance(isConfigChange: Boolean, movieId: Int? = null): MovieFragment {
            val fragment = MovieFragment()
            val args = Bundle()
            args.putBoolean("isConfigChange", isConfigChange)
            if (movieId != null) args.putInt("movie", movieId)
            fragment.arguments = args
            return fragment
        }
    }
}