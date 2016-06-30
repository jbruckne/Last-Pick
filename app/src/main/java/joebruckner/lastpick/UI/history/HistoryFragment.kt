package joebruckner.lastpick.ui.history

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.gson.Gson
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.network.HistoryManager
import joebruckner.lastpick.presenters.HistoryPresenter
import joebruckner.lastpick.presenters.HistoryPresenterImpl
import joebruckner.lastpick.ui.SimpleMovieAdapter
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieActivity

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

        val intent = Intent(context, MovieActivity::class.java)
        adapter = SimpleMovieAdapter(context)
        adapter.addOnItemClickListener { position ->
            Log.d(logTag, adapter.movies[position].toString())
            intent.putExtra("movie", Gson().toJson(adapter.movies[position]))
            startActivity(intent)
        }

        val content = view?.findViewById(R.id.content) as RecyclerView

        content.layoutManager = LinearLayoutManager(activity)
        content.adapter = adapter

        val historyManager = parent.application
                .getSystemService(LastPickApp.HISTORY_MANAGER) as HistoryManager
        presenter = HistoryPresenterImpl(historyManager)
        presenter.attachView(this)
        presenter.getHistory()
    }
}
