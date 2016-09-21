package joebruckner.lastpick.view.specials

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Showcase
import joebruckner.lastpick.model.State
import joebruckner.lastpick.model.tmdb.SlimMovie
import joebruckner.lastpick.view.common.BaseFragment
import joebruckner.lastpick.view.movie.adapters.SlimMovieAdapter
import joebruckner.lastpick.utilities.find
import joebruckner.lastpick.utilities.visibleIf
import javax.inject.Inject

class SpecialsFragment : BaseFragment(), SpecialsContract.View {
    override val layoutId = R.layout.fragment_history
    override var state = State.LOADING

    @Inject lateinit var presenter: SpecialsContract.Presenter
    lateinit var adapter: SlimMovieAdapter

    val content by lazy { find<RecyclerView>(R.id.content) }
    val loading by lazy { find<View>(R.id.loading) }
    val error   by lazy { find<TextView>(R.id.error) }

    fun updateViews() {
        content.visibleIf(state == State.CONTENT)
        loading.visibleIf(state == State.LOADING)
        error.visibleIf(state == State.ERROR)
    }

    override fun showContent(content: List<SlimMovie>) {
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

        if (!isFirstStart) return

        adapter = SlimMovieAdapter(context, R.layout.item_movie) {
            presenter.movieSelected(it)
        }

        content.layoutManager = LinearLayoutManager(activity)
        content.adapter = adapter

        presenter.attachView(this)
        presenter.getSpecialList(Showcase.values()[arguments.getInt("type", 0)])
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
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