package joebruckner.lastpick.ui.movie

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import joebruckner.lastpick.*
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.network.BookmarkManager
import joebruckner.lastpick.network.MovieManager
import joebruckner.lastpick.presenters.MoviePresenter
import joebruckner.lastpick.presenters.MoviePresenterImpl
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.widgets.FilterSheetDialogBuilder
import joebruckner.lastpick.widgets.PaletteTheme
import joebruckner.lastpick.widgets.SimpleRequestListener

class MovieFragment() : BaseFragment(), MoviePresenter.MovieView {
    override val menuId = R.menu.menu_movie
    override val layoutId = R.layout.fragment_movie
    override var isLoading = true

    val providedMovieId: Int by lazy { arguments.getInt("movie", -1) }
    val isConfigChange: Boolean by lazy { arguments.getBoolean("isConfigChange", false) }

    lateinit var adapter: CastAdapter
    val presenter: MoviePresenter by lazy {
        MoviePresenterImpl(
            activity.application.getSystemService(LastPickApp.MOVIE_MANAGER) as MovieManager,
            activity.application.getSystemService(LastPickApp.BOOKMARKS_MANAGER) as BookmarkManager
        )
    }

    // Views
    val content         by lazy { find<View>(R.id.content) }
    val loading         by lazy { find<View>(R.id.loading) }
    val error           by lazy { find<View>(R.id.error) }
    val errorMessage    by lazy { find<TextView>(R.id.error_message) }
    val errorButton     by lazy { find<Button>(R.id.error_button) }
    val poster          by lazy { find<ImageView>(R.id.poster) }
    val backdrop        by lazy { activity.find<ImageView>(R.id.backdrop) }
    val title           by lazy { activity.find<TextView>(R.id.title) }
    val castList        by lazy { find<RecyclerView>(R.id.cast_list) }
    val trailerButton   by lazy { find<ImageView>(R.id.trailer_button) }
    val scrollView      by lazy { find<NestedScrollView>(R.id.nested_scroll_view) }
    val summary         by lazy { find<TextView>(R.id.overview) }
    val year            by lazy { find<TextView>(R.id.year) }
    val mpaa            by lazy { find<TextView>(R.id.mpaa) }
    val popularity      by lazy { find<TextView>(R.id.popularity) }
    val runtime         by lazy { find<TextView>(R.id.runtime) }
    val genres          by lazy { find<ViewGroup>(R.id.genres) }
    val phrase          by lazy { find<TextView>(R.id.phrase) }

    val ALPHA_CLEAR = 0f
    val ALPHA_HALF = 0.4f
    val ALPHA_FULL = 1f
    val OUT_DURATION: Long = 250
    val IN_DURATION: Long = 350

    override fun showLoading() {
        isLoading = true
        clearMovie()
        parent.disableFab()
        updateViews(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
        scrollView.smoothScrollTo(0, 0)
    }

    override fun showError(message: String) {
        isLoading = false
        clearMovie()
        parent.enableFab()
        errorMessage.text = message
        updateViews(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
        parent.appBar.setExpanded(false, true)
    }

    override fun showMovieErrorButton(message: String, listener: () -> Unit) {
        errorButton.text = message
        errorButton.setOnClickListener { listener() }
        errorButton.visibility = View.VISIBLE
    }

    override fun showContent(movie: Movie) {
        isLoading = false
        showMovie(movie)
        parent.enableFab()
        updateViews(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
        parent.appBar.setExpanded(true, false)
    }

    private fun clearMovie() {
        backdrop.animate().alpha(ALPHA_CLEAR).duration = OUT_DURATION
        poster.animate().alpha(ALPHA_CLEAR).duration = OUT_DURATION
        parent.title = ""
        title.text = ""
    }

    private fun showMovie(movie: Movie) {
        if (view == null) return

        // Set backdrop views
        parent.title = movie.title
        title.text = movie.title
        loadImage(movie.getFullBackdropPath(), backdrop, ALPHA_HALF) { theme ->
            parent.setPrimary(theme.getPrimaryColor())
            parent.setDark(theme.getPrimaryDarkColor())
        }

        // Set movie detail views
        loadImage(movie.getFullPosterPath(), poster, ALPHA_FULL) { theme ->
            parent.setAccent(theme.getAccentColor())
        }
        summary.text = movie.overview
        year.text = movie.releaseDate.substring(0, 4)
        mpaa.text = movie.getSimpleMpaa()
        popularity.text = movie.voteAverage.toString()
        runtime.text = movie.runtime.toRuntimeFormat()
        genres.removeAllViews()
        movie.genres.slice(0..Math.min(movie.genres.size-1, 2)).forEach { genre ->
            val card = genres.inflate(R.layout.card_filter)
            val name = card.find<TextView>(R.id.name)
            name.text = genre.name
            genres.addView(card)
        }
        adapter.cast = movie.credits.cast
        phrase.text = movie.tagline

        // Only show button if trailer exists
        trailerButton.visibility =
                if (movie.getYoutubeTrailer().isNullOrBlank()) View.GONE
                else View.VISIBLE

        // Show bookmarked icon
        val item = parent.menu?.findItem(R.id.action_bookmark) ?: return
        parent.supportInvalidateOptionsMenu()
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

    private fun updateViews(contentState: Int, loadingState:Int, errorState: Int) {
        content.visibility = contentState
        loading.visibility = loadingState
        error.visibility   = errorState
        errorButton.visibility = View.GONE
    }

    fun loadImage(imagePath: String, imageView: ImageView,
                  alpha: Float, listener: (PaletteTheme) -> Unit) {
        Glide.with(parent.applicationContext)
                .load(imagePath)
                ?.asBitmap()
                ?.listener(SimpleRequestListener { resource ->
                    PaletteTheme.Builder(resource).generateFrom { listener(it) }
                    imageView.animate().alpha(alpha).duration = IN_DURATION
                })
                ?.centerCrop()
                ?.into(imageView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        parent.appBar.addOnOffsetChangedListener { appBarLayout, i ->
            val p = Math.abs(i.toFloat() / appBarLayout.totalScrollRange.toFloat())
            parent.supportActionBar?.setDisplayShowTitleEnabled(p >= 1)
        }

        backdrop.alpha = ALPHA_HALF

        trailerButton.setOnClickListener {
            activity.viewUri(presenter.getCurrentMovie()!!.getFullTrailerPath())
        }

        // Set up cast list
        adapter = CastAdapter(context)
        castList.isNestedScrollingEnabled = false
        castList.adapter = adapter

        // Initialization of presenter
        presenter.attachActor(this)

        if (isConfigChange) presenter.reloadMovie()
        else if (providedMovieId > 0) presenter.getMovieById(providedMovieId)
        else  presenter.getNextMovie()
    }

    override fun onResume() {
        super.onStart()
        presenter.attachActor(this)
    }

    override fun onPause() {
        presenter.detachActor()
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
            val watched = menu.findItem(R.id.action_watched)
            watched?.setIcon(
                    if (presenter.getBookmarkStatus()) R.drawable.ic_visibility_off_24dp
                    else R.drawable.ic_visibility_24dp
            )
        } catch (e: Exception) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_filter -> consume { showFilterSettings() }
        R.id.action_bookmark -> consume { presenter.updateBookmark() }
        R.id.action_watched -> consume {  }
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