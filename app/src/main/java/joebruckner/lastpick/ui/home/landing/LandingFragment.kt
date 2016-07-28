package joebruckner.lastpick.ui.home.landing


import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.CondensedMovie
import joebruckner.lastpick.data.State
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieActivity
import joebruckner.lastpick.ui.specials.SpecialsActivity
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.visibleIf

/**
 * A simple [Fragment] subclass.
 */
class LandingFragment : BaseFragment(), LandingContract.View {
    override val layoutId: Int = R.layout.fragment_landing

    val loading by lazy { find<View>(R.id.loading) }
    val content by lazy { find<View>(R.id.content) }
    val error   by lazy { find<TextView>(R.id.error) }
    val specialLists by lazy { find<RecyclerView>(R.id.special_lists) }

    private val presenter: LandingContract.Presenter by lazy { LandingPresenter(
            activity.application.getSystemService(LastPickApp.Companion.MOVIE_MANAGER) as MovieInteractor
    ) }
    override var state: State = State.LOADING

    private fun updateViews() {
        content.visibleIf(state == State.CONTENT)
        loading.visibleIf(state == State.LOADING)
        error.visibleIf(state == State.ERROR)
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
