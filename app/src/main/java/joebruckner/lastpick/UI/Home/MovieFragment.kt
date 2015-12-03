package joebruckner.lastpick.ui.home

import android.os.Bundle
import android.support.v7.graphics.Palette
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.google.gson.Gson
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.presenters.MoviePresenter
import joebruckner.lastpick.presenters.MoviePresenterImpl
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.MovieViewHolder
import joebruckner.lastpick.ui.common.DetailActivity
import kotlinx.android.synthetic.fragment_movie.content
import kotlinx.android.synthetic.fragment_movie.error
import kotlinx.android.synthetic.fragment_movie.loading

class MovieFragment(val movie: String? = null) : BaseFragment(), MoviePresenter.MovieView {
    override val layoutId = R.layout.fragment_movie
    override var isLoading = true
    lateinit var holder: MovieViewHolder
    lateinit var detailParent: DetailActivity
    var presenter: MoviePresenter? = null

    init {
        val args = Bundle()
        args.putString("movie", movie)
        arguments = args
    }

    override fun showLoading() {
        isLoading = true
        clearMovie()
        detailParent.disableFab()
        updateViews(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
    }

    override fun showError(errorMessage: String) {
        isLoading = false
        clearMovie()
        detailParent.enableFab()
        error.text = errorMessage
        updateViews(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
    }

    override fun showContent(movie: Movie) {
        isLoading = false
        showMovie(movie)
        detailParent.enableFab()
        updateViews(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
    }

    private fun clearMovie() {
        detailParent.clearBackdrop()
        detailParent.clearPoster()
        detailParent.setTitle(" ")
    }

    private fun showMovie(movie: Movie) {
        holder.movie = movie
        detailParent.setTitle(movie.title)
        detailParent.setBackdrop(movie.fullBackdropPath())
        detailParent.setPoster(movie.fullPosterPath())
    }

    private fun updateViews(contentState: Int, loadingState:Int, errorState: Int) {
        if (view == null) return
        content.visibility = contentState
        loading.visibility = loadingState
        error.visibility   = errorState
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        detailParent = parent as DetailActivity
        holder = MovieViewHolder(view)

        val movieString = arguments?.getString("movie")
        val movie = Gson().fromJson(movieString, javaClass<Movie>())

        if (movie == null) {
            menuId = R.menu.menu_history
            val bus = parent.application.getSystemService(LastPickApp.BUS) as Bus
            presenter = MoviePresenterImpl(bus)
            presenter?.attachActor(this)
            presenter?.shuffleMovie()
        } else {
            detailParent.disableFab()
            updateViews(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
            showMovie(movie)
        }
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
            Action.SHUFFLE -> presenter?.shuffleMovie()
        }
    }
}