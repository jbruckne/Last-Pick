package joebruckner.lastpick.view.home.bookmark

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.model.State
import joebruckner.lastpick.view.common.BaseFragment
import joebruckner.lastpick.view.movie.MovieActivity
import joebruckner.lastpick.view.movie.adapters.MovieAdapter
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.visibleIf
import javax.inject.Inject

class BookmarkFragment : BaseFragment(), BookmarkContract.View {
    // Overridden properties
    override val layoutId = R.layout.fragment_bookmarks
    override var state = State.LOADING

    // Injected objects
    @Inject lateinit var presenter: BookmarkContract.Presenter
    @Inject lateinit var adapter: MovieAdapter

    // Views
    val content: RecyclerView get() = find(R.id.content)
    val loading: View get() = find(R.id.loading)
    val error: TextView get() = find(R.id.error)

    fun updateViews() {
        content.visibleIf(state == State.CONTENT)
        loading.visibleIf(state == State.LOADING)
        error.visibleIf(state == State.ERROR)
    }

    override fun showContent(movies: List<Movie>) {
        state = State.CONTENT
        adapter.setNewMovies(movies)
        updateViews()
    }

    override fun showLoading() {
        state = State.LOADING
        updateViews()
    }

    override fun showError(errorMessage: String) {
        state = State.ERROR
        error.text = errorMessage
        updateViews()
    }

    override fun onStart() {
        super.onStart()
        adapter.onItemClickListener = { position ->
            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra("movie", adapter.movies[position].id)
            startActivity(intent)
        }

        content.layoutManager = LinearLayoutManager(activity)
        content.adapter = adapter

        presenter.attachView(this)
        presenter.getBookmarks()
    }
}
