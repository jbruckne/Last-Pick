package joebruckner.lastpick.ui.history

import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.presenters.HistoryPresenter
import joebruckner.lastpick.presenters.HistoryPresenterImpl
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.SimpleMovieAdapter
import kotlinx.android.synthetic.fragment_history.*

class HistoryFragment : BaseFragment(), HistoryPresenter.HistoryView {
    override val layoutId = R.layout.fragment_history
    override var isLoading = false
    lateinit var presenter: HistoryPresenter
    lateinit var adapter: SimpleMovieAdapter

    override fun showContent(movies: List<Movie>) {
        adapter.setNewMovies(movies)
    }

    override fun showError(errorMessage: String) {
        Log.e("ERROR", errorMessage)
    }

    override fun onStart() {
        super.onStart()
        Log.d(this.javaClass.simpleName, "Starting")
        adapter = SimpleMovieAdapter(context)
        content.layoutManager = LinearLayoutManager(activity)
        content.adapter = adapter
        val bus = parent.application.getSystemService(LastPickApp.BUS) as Bus
        presenter = HistoryPresenterImpl(bus)
        presenter.attachView(this)
        presenter.getHistory()
    }
}
