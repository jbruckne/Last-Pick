package joebruckner.lastpick.ui.home.landing

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.State
import joebruckner.lastpick.model.tmdb.CondensedMovie
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

    override fun showContent(movies: List<CondensedMovie>) {
        state = State.CONTENT
        Log.d(logTag, "Content")
        val adapter = SpecialListAdapter(context, movies.toTypedArray(), {
            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra("movie", it.id)
            startActivity(intent)
        }, {
            val intent = Intent(context, SpecialsActivity::class.java)
            intent.putExtra("type", it.ordinal)
            startActivity(intent)
        })
        specialLists.swapAdapter(adapter, true)
        updateViews()
    }

    override fun showLoading() {
        state = State.LOADING
        updateViews()
    }

    override fun showError(message: String) {
        state = State.ERROR
        error.text = message
        updateViews()
    }

    override fun onStart() {
        super.onStart()

        presenter.attachView(this)
        presenter.loadLists()
    }

    override fun onPause() {
        presenter.detachView()
        super.onPause()
    }
}