package joebruckner.lastpick.ui.home

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.gson.Gson
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.network.HistoryManager
import joebruckner.lastpick.presenters.HistoryPresenter
import joebruckner.lastpick.presenters.HistoryPresenterImpl
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieActivity
import joebruckner.lastpick.ui.movie.MovieAdapter

class HistoryFragment : BaseFragment(), HistoryPresenter.HistoryView {
    override val layoutId = R.layout.fragment_history
    override var isLoading = false
    lateinit var presenter: HistoryPresenter
    lateinit var adapter: MovieAdapter

    override fun showContent(movies: List<Movie>) {
        adapter.setNewMovies(movies)
    }

    override fun showError(errorMessage: String) {
        Log.e("ERROR", errorMessage)
    }

    override fun onStart() {
        super.onStart()

        adapter = MovieAdapter(context)
        adapter.addOnItemClickListener { position ->
            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra("movie", Gson().toJson(adapter.movies[position]))
            activity.startActivity(intent)
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
