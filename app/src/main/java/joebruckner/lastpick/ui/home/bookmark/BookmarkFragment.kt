package joebruckner.lastpick.ui.home.bookmark

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.State
import joebruckner.lastpick.interactors.BookmarkInteractor
import joebruckner.lastpick.interactors.MovieInteractor
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieActivity
import joebruckner.lastpick.ui.movie.adapters.MovieAdapter
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.visibleIf

class BookmarkFragment : BaseFragment(), BookmarkContract.View {
    override val layoutId = R.layout.fragment_bookmarks
    lateinit var presenter: BookmarkContract.Presenter
    lateinit var adapter: MovieAdapter

    override var state = State.LOADING

    val content by lazy { find<RecyclerView>(R.id.content) }
    val loading by lazy { find<View>(R.id.loading) }
    val error   by lazy { find<TextView>(R.id.error) }

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

        adapter = MovieAdapter(context, R.layout.card_movie_list)
        adapter.addOnItemClickListener { position ->
            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra("movie", adapter.movies[position].id)
            startActivity(intent)
        }

        content.layoutManager = LinearLayoutManager(activity)
        content.adapter = adapter

        val movieManager = parent.application
                .getSystemService(LastPickApp.MOVIE_MANAGER) as MovieInteractor
        val bookmarkManager = parent.application
                .getSystemService(LastPickApp.BOOKMARKS_MANAGER) as BookmarkInteractor
        presenter = BookmarkPresenter(movieManager, bookmarkManager)
        presenter.attachView(this)
        presenter.getBookmarks()
    }
}
