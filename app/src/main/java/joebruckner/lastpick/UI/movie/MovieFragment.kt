package joebruckner.lastpick.ui.movie

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.presenters.MoviePresenter
import joebruckner.lastpick.presenters.MoviePresenterImpl
import joebruckner.lastpick.ui.MovieViewHolder
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.home.GenreAdapter
import joebruckner.lastpick.widgets.ImageBlur
import joebruckner.lastpick.widgets.PaletteMagic

class MovieFragment() : BaseFragment(), MoviePresenter.MovieView {
    override val menuId = R.menu.menu_movie
    override val layoutId = R.layout.fragment_movie
    override var isLoading = true
    lateinit var holder: MovieViewHolder
    var presenter: MoviePresenter? = null

    lateinit var poster: ImageView
    lateinit var backdrop: ImageView
    lateinit var backdropTitle: TextView
    lateinit var blur: BitmapTransformation

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
    }

    override fun showContent(movie: Movie) {
        isLoading = false
        showMovie(movie)
        parent.enableFab()
        updateViews(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
    }

    private fun clearMovie() {
        backdrop.animate().alpha(ALPHA_CLEAR).duration = OUT_DURATION
        poster.setImageResource(android.R.color.transparent)
        parent.title = " "
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
        }
        val item = parent.menu?.findItem(R.id.action_bookmark) ?: return
        item.isChecked = movie.isBookmarked
        item.setIcon(
                if (movie.isBookmarked) R.drawable.ic_bookmark_24dp
                else R.drawable.ic_bookmark_outline_24dp
        )
    }

    override fun showBookmarkUpdate(isBookmarked: Boolean) {
        holder.movie?.isBookmarked = isBookmarked
        val item = parent.menu?.findItem(R.id.action_bookmark) ?: return
        item.isChecked = isBookmarked
        item.setIcon(
                if (isBookmarked) R.drawable.ic_bookmark_24dp
                else R.drawable.ic_bookmark_outline_24dp
        )
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
        }

        val bus: Bus = parent.application.getSystemService(LastPickApp.BUS) as Bus
        presenter = MoviePresenterImpl(bus)
        presenter?.attachActor(this)
    }

    override fun onResume() {
        super.onStart()
        presenter?.attachActor(this)
    }

    override fun onPause() {
        presenter?.detachActor()
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                showFilterSettings()
                return true
            }
            R.id.action_bookmark -> {
                val movie = holder.movie ?: return true
                presenter?.updateBookmark(movie, !movie.isBookmarked)
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
        presenter?.updateMovie()
    }

    fun showFilterSettings() {
        val sheet = BottomSheetDialog(activity)
        val sheetView = activity.layoutInflater.inflate(R.layout.sheet_filter, null)
        val genreGrid = sheetView.findViewById(R.id.genres) as RecyclerView
        genreGrid.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)
        val adapter = GenreAdapter(presenter?.getSelectedGenres())
        genreGrid.adapter = adapter
        sheet.setContentView(sheetView)
        sheet.setOnDismissListener {
            Log.d("Movie", "Filter sheet dismissed")
            presenter?.updateGenreFilter(adapter.selected)
        }
        sheet.show()
    }
}