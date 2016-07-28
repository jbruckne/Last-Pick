package joebruckner.lastpick.ui.specials

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.CondensedMovie
import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.data.State
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieActivity
import joebruckner.lastpick.ui.movie.adapters.CondensedMovieAdapter
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.visibleIf

class SpecialsFragment : BaseFragment(), SpecialsContract.View {
    override val layoutId = R.layout.fragment_history
    lateinit var presenter: SpecialsContract.Presenter
    lateinit var adapter: CondensedMovieAdapter

    override var state = State.LOADING

    val content by lazy { find<RecyclerView>(R.id.content) }
    val loading by lazy { find<View>(R.id.loading) }
    val error   by lazy { find<TextView>(R.id.error) }

    fun updateViews() {
        content.visibleIf(state == State.CONTENT)
        loading.visibleIf(state == State.LOADING)
        error.visibleIf(state == State.ERROR)
    }

    override fun showContent(content: List<CondensedMovie>) {
        state = State.CONTENT
        adapter.setNewMovies(content)
        updateViews()
    }

    override fun showError(errorMessage: String) {
        state = State.ERROR
        error.text = errorMessage
        updateViews()
    }

    override fun showLoading() {
        state = State.LOADING
        updateViews()
    }

    override fun onStart() {
        super.onStart()

        adapter = CondensedMovieAdapter(context, R.layout.card_movie_list)
        adapter.addOnItemClickListener { position ->
            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra("movie", adapter.movies[position].id)
            activity.startActivity(intent)
        }

        content.layoutManager = LinearLayoutManager(activity)
        content.adapter = adapter

        val movieManager = parent.application
                .getSystemService(LastPickApp.MOVIE_MANAGER) as MovieInteractor
        presenter = SpecialsPresenter(movieManager)
        presenter.attachView(this)
        presenter.getSpecialList(ListType.values()[arguments.getInt("type", 0)])
    }

    companion object {
        fun newInstance(type: Int): Fragment {
            val fragment = SpecialsFragment()
            val args = Bundle()
            args.putInt("type", type)
            fragment.arguments = args
            return fragment
        }
    }
}