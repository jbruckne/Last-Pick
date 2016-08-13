package joebruckner.lastpick.ui.home.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Showcase
import joebruckner.lastpick.model.State
import joebruckner.lastpick.model.tmdb.SlimMovie
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieActivity
import joebruckner.lastpick.ui.specials.SpecialsActivity
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.visibleIf
import javax.inject.Inject

class LandingFragment : BaseFragment(), LandingContract.View {
    // Overridden properties
    override val layoutId: Int = R.layout.fragment_landing
    override var state: State = State.LOADING

    // Injected objects
    @Inject lateinit var presenter: LandingContract.Presenter
    lateinit var adapter: ShowcaseAdapter

    // Views
    val loading: View get() = find(R.id.loading)
    val content: View get() = find(R.id.content)
    val error: TextView get() = find(R.id.error)
    val specialLists: RecyclerView get() = find(R.id.special_lists)

    private fun updateViews() {
        content.visibleIf(state == State.CONTENT)
        loading.visibleIf(state == State.LOADING)
        error.visibleIf(state == State.ERROR)
    }

    override fun showContent(movie: SlimMovie, type: Showcase) {
        state = State.CONTENT
        adapter.addPair(movie, type)
        updateViews()
    }

    override fun showLoading() {
        state = State.LOADING
        updateViews()
    }

    override fun showError(message: String, type: Showcase) {
        state = State.ERROR
        error.text = message
        updateViews()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = ShowcaseAdapter(context, {
            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra("movie", it.id)
            startActivity(intent)
        }, {
            val intent = Intent(context, SpecialsActivity::class.java)
            intent.putExtra("type", it.ordinal)
            startActivity(intent)
        })
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        specialLists.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
        if (isFirstStart) presenter.loadShowcases()
    }

    override fun onPause() {
        presenter.detachView()
        super.onPause()
    }
}