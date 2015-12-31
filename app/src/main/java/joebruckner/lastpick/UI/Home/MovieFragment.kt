package joebruckner.lastpick.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.graphics.Palette
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.presenters.MoviePresenter
import joebruckner.lastpick.presenters.MoviePresenterImpl
import joebruckner.lastpick.ui.MovieViewHolder
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.widgets.ImageBlur
import joebruckner.lastpick.widgets.PaletteMagic
import kotlinx.android.synthetic.fragment_movie.*

class MovieFragment() : BaseFragment(),
        MoviePresenter.MovieView, RequestListener<String, Bitmap> {
    override val layoutId = R.layout.fragment_movie
    override var isLoading = true
    lateinit var holder: MovieViewHolder
    var presenter: MoviePresenter? = null

    lateinit var poster: ImageView
    lateinit var backdrop: ImageView
    lateinit var blur: BitmapTransformation

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
        backdrop.setImageResource(android.R.color.transparent)
        poster.setImageResource(android.R.color.transparent)
        parent.title = ""
    }

    private fun showMovie(movie: Movie) {
        holder.movie = movie
        parent.title = movie.title
        setBackdrop(movie.fullBackdropPath())
        setPoster(movie.fullPosterPath())
    }

    private fun updateViews(contentState: Int, loadingState:Int, errorState: Int) {
        if (view == null) return
        content.visibility = contentState
        loading.visibility = loadingState
        error.visibility   = errorState
    }

    fun setBackdrop(imagePath: String) {
        Glide.with(this).load(imagePath)
                .asBitmap()
                .listener(this)
                .transform(blur)
                .into(backdrop)
    }

    fun setPoster(imagePath: String) {
        Glide.with(this).load(imagePath)
                .crossFade()
                .into(poster)
    }

    override fun onException(e: Exception, model: String,
                             target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
        Log.e("Glide", e.toString());
        return false;
    }

    override fun onResourceReady(resource: Bitmap, model: String, target: Target<Bitmap>,
                                 isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
        Palette.from(resource).generate { palette ->
            val magic = PaletteMagic(palette)
            parent.setTheme(magic.primary, magic.dark, magic.accent)
        }
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parent.setToolbarStubLayout(R.layout.backdrop_movie)
        holder = MovieViewHolder(view)

        backdrop = parent.root.findViewById(R.id.backdrop) as ImageView
        poster = parent.root.findViewById(R.id.poster) as ImageView

        blur = ImageBlur(context, 20f)
        parent.appBar.addOnOffsetChangedListener { layout, i ->
            poster.elevation = if(layout.y < -40) 0f else 4f
            poster.alpha = (100 + layout.y) / 100
        }

        menuId = R.menu.menu_history
        val bus = parent.application.getSystemService(LastPickApp.BUS) as Bus
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

    override fun handleAction(action: Action) {
        when (action.name) {
            Action.UPDATE -> presenter?.updateMovie()
        }
    }
}