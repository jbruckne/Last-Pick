package joebruckner.lastpick.ui.movie

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.graphics.Palette
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.appyvet.rangebar.RangeBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.network.BookmarkManager
import joebruckner.lastpick.network.MovieManager
import joebruckner.lastpick.presenters.MoviePresenter
import joebruckner.lastpick.presenters.MoviePresenterImpl
import joebruckner.lastpick.ui.MovieViewHolder
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.home.GenreAdapter
import joebruckner.lastpick.widgets.ExpandedBottomSheetDialog
import joebruckner.lastpick.widgets.ImageBlur
import joebruckner.lastpick.widgets.PaletteMagic

class MovieFragment() : BaseFragment(), MoviePresenter.MovieView {
    override val menuId = R.menu.menu_movie
    override val layoutId = R.layout.fragment_movie
    override var isLoading = true

    lateinit var holder: MovieViewHolder
    lateinit var adapter: CastAdapter
    lateinit var presenter: MoviePresenter
    lateinit var blur: BitmapTransformation

    // Views
    lateinit var poster: ImageView
    lateinit var backdrop: ImageView
    lateinit var backdropTitle: TextView
    lateinit var castList: RecyclerView

    val ALPHA_CLEAR = 0f
    val ALPHA_HALF = 0.4f
    val OUT_DURATION: Long = 250
    val IN_DURATION: Long = 350

    override fun showLoading() {
        isLoading = true
        clearMovie()
        parent.disableFab()
        updateViews(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
    }

    override fun showError(errorMessage: String) {
        isLoading = false
        clearMovie()
        parent.enableFab()
        val error = view!!.findViewById(R.id.error) as TextView
        error.text = errorMessage
        updateViews(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
        parent.appBar.collapseToolbar(true)
    }

    override fun showContent(movie: Movie) {
        isLoading = false
        showMovie(movie)
        parent.enableFab()
        updateViews(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
        if (!parent.toolbarIsCollapsed) parent.appBar.expandToolbar(true)
    }

    private fun clearMovie() {
        backdrop.animate().alpha(ALPHA_CLEAR).duration = OUT_DURATION
        poster.setImageResource(android.R.color.transparent)
        parent.title = ""
        backdropTitle.text = ""
    }

    private fun showMovie(movie: Movie) {
        holder.movie = movie
        parent.title = movie.title
        backdropTitle.text = movie.title
        loadBackdrop(movie.getFullBackdropPath())
        loadPoster(movie.getFullPosterPath())
        if (view != null) {
            val trailer = view!!.findViewById(R.id.trailer)
            trailer.visibility = if (movie.getYoutubeTrailer() == null) View.GONE
            else View.VISIBLE
            val genres = view!!.findViewById(R.id.genres) as LinearLayout
            genres.removeAllViews()
            movie.genres.slice(0..Math.min(movie.genres.size-1, 2)).forEach { genre ->
                val card = parent.layoutInflater.inflate(R.layout.card_filter, null)
                val name = card.findViewById(R.id.name) as TextView
                name.text = genre.name
                genres.addView(card)
                (card.layoutParams as ViewGroup.MarginLayoutParams).setMargins(0, 0, 16, 0)
            }
            adapter.cast = movie.credits.cast
        }
        val item = parent.menu?.findItem(R.id.action_bookmark) ?: return
        item.isChecked = movie.isBookmarked
        item.setIcon(
                if (movie.isBookmarked) R.drawable.ic_bookmark_24dp
                else R.drawable.ic_bookmark_outline_24dp
        )
    }

    override fun showBookmarkUpdate(isBookmarked: Boolean, notify: Boolean) {
        holder.movie?.isBookmarked = isBookmarked
        val item = parent.menu?.findItem(R.id.action_bookmark) ?: return
        item.isChecked = isBookmarked
        item.setIcon(
                if (isBookmarked) R.drawable.ic_bookmark_24dp
                else R.drawable.ic_bookmark_outline_24dp
        )
        if (!notify) return
        Snackbar.make(view!!,
                if (isBookmarked) "Bookmark added"
                else "Bookmark removed", Snackbar.LENGTH_SHORT).show()
    }

    override fun showBookmarkError(isBookmarked: Boolean) {
        Snackbar.make(view!!,
                if (isBookmarked) "Failed to add bookmark"
                else "Failed to remove bookmark", Snackbar.LENGTH_SHORT).show()
    }

    override fun getContent() = holder.movie

    private fun updateViews(contentState: Int, loadingState:Int, errorState: Int) {
        val content = view?.findViewById(R.id.content)
        val loading = view?.findViewById(R.id.loading)
        val error = view?.findViewById(R.id.error)
        content?.visibility = contentState
        loading?.visibility = loadingState
        error?.visibility   = errorState
    }

    fun loadBackdrop(imagePath: String) {
        Glide.with(parent.applicationContext).load(imagePath)
                ?.asBitmap()
                ?.listener(object: RequestListener<String, Bitmap> {
                    override fun onResourceReady(resource: Bitmap?, model: String?,
                                                 target: Target<Bitmap>?,
                                                 isFromMemoryCache: Boolean,
                                                 isFirstResource: Boolean): Boolean {
                        Palette.from(resource).generate { palette ->
                            val magic = PaletteMagic(palette)
                            parent.setTheme(magic.primary, magic.dark, magic.accent)
                        }
                        backdrop.animate().alpha(ALPHA_HALF).duration = IN_DURATION
                        return false
                    }

                    override fun onException(e: Exception?, model: String?,
                                             target: Target<Bitmap>?,
                                             isFirstResource: Boolean): Boolean {
                        e?.printStackTrace()
                        return false
                    }

                })
                ?.transform(blur)
                ?.into(backdrop)
    }

    fun loadPoster(imagePath: String) {
        Glide.with(parent.applicationContext).load(imagePath)
                ?.crossFade(IN_DURATION.toInt())
                ?.into(poster)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parent.setToolbarStubLayout(R.layout.backdrop_movie)
        holder = MovieViewHolder(view!!)

        backdrop = parent.root.findViewById(R.id.backdrop) as ImageView
        poster = parent.root.findViewById(R.id.poster) as ImageView
        backdropTitle = parent.root.findViewById(R.id.backdrop_title) as TextView
        backdrop.alpha = ALPHA_HALF

        val trailer = view!!.findViewById(R.id.trailer)
        trailer.setOnClickListener {
            val id = holder.movie?.getYoutubeTrailer() ?: return@setOnClickListener
            val uri = Uri.parse("https://www.youtube.com/watch?v=$id")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        blur = ImageBlur(context, 20f)
        parent.appBar.addOnOffsetChangedListener { layout, i ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                poster.elevation = if(layout.y < -40) 0f else 4f
            poster.alpha = (100 + layout.y) / 100
            backdropTitle.alpha = (100 + layout.y) / 100
            parent.supportActionBar?.setDisplayShowTitleEnabled(parent.toolbarIsCollapsed)
        }

        // Set up cast list
        adapter = CastAdapter(context)
        castList = view!!.findViewById(R.id.cast_list) as RecyclerView
        castList.isNestedScrollingEnabled = false
        castList.adapter = adapter

        // Initialization of presenter
        val movies = parent.application.getSystemService(LastPickApp.MOVIE_MANAGER)
        val bookmarks = parent.application.getSystemService(LastPickApp.BOOKMARKS_MANAGER)
        presenter = MoviePresenterImpl(movies as MovieManager, bookmarks as BookmarkManager)
        presenter.attachActor(this)
    }

    override fun onResume() {
        super.onStart()
        presenter.attachActor(this)
    }

    override fun onPause() {
        presenter.detachActor()
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                showFilterSettings()
                return true
            }
            R.id.action_bookmark -> {
                holder.movie?.let { presenter.updateBookmark(it, !it.isBookmarked) }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val isBookmarked = holder.movie?.isBookmarked ?: false
        val item = menu.findItem(R.id.action_bookmark)
        item.isChecked = isBookmarked
        item.setIcon(
                if (isBookmarked) R.drawable.ic_bookmark_24dp
                else R.drawable.ic_bookmark_outline_24dp
        )
    }

    fun callForUpdate() {
        presenter.updateMovie()
    }

    fun showFilterSettings() {
        // Create sheet view
        val sheetView = activity.layoutInflater.inflate(R.layout.sheet_filter, null)

        // Set up genre picker
        val recyclerView = sheetView.findViewById(R.id.genres) as RecyclerView
        recyclerView.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)
        val adapter = GenreAdapter(presenter.getSelectedGenres())
        recyclerView.adapter = adapter

        // Set up release year range bar
        val rangeBar = sheetView.findViewById(R.id.years) as RangeBar
        val gteText = sheetView.findViewById(R.id.year_gte) as TextView
        val lteText = sheetView.findViewById(R.id.year_lte) as TextView
        gteText.text = presenter.getGte()
        lteText.text = presenter.getLte()
        rangeBar.setRangePinsByValue(
                presenter.getGte().toFloat(),
                presenter.getLte().toFloat()
        )
        rangeBar.setOnRangeBarChangeListener { rangeBar, l, r, lv, rv ->
            gteText.text = lv
            lteText.text = rv
        }

        // Set up filter bottom sheet dialog
        val sheet = ExpandedBottomSheetDialog(activity)
        sheet.setContentView(sheetView)
        sheet.setOnDismissListener {
            Log.d("Movie", "Filter sheet dismissed")
            presenter.updateFilter(
                    adapter.selected,
                    rangeBar.rightPinValue,
                    rangeBar.leftPinValue
            )
        }
        sheet.show()
    }
}