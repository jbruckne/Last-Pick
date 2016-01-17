package joebruckner.lastpick.ui.bookmarks

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.Gson
import com.squareup.otto.Bus
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.presenters.BookmarksPresenter
import joebruckner.lastpick.presenters.BookmarksPresenterImpl
import joebruckner.lastpick.ui.SimpleMovieAdapter
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieActivity
import kotlinx.android.synthetic.fragment_bookmarks.*

class BookmarksFragment : BaseFragment(), BookmarksPresenter.BookmarksView {
    override val layoutId = R.layout.fragment_bookmarks
    override var isLoading = false
    lateinit var presenter: BookmarksPresenter
    lateinit var adapter: SimpleMovieAdapter

    override fun showContent(movies: List<Movie>) {
        movies.forEach { Log.d(logTag, it.isBookmarked.toString()) }
        adapter.setNewMovies(movies)
    }

    override fun showError(errorMessage: String) {
        Log.e("ERROR", errorMessage)
    }

    override fun onStart() {
        super.onStart()

        val intent = Intent(context, javaClass<MovieActivity>())
        adapter = SimpleMovieAdapter(context)
        adapter.addOnItemClickListener { position ->
            intent.putExtra("movie", Gson().toJson(adapter.movies[position]))
            startActivity(intent)
        }
        content.layoutManager = LinearLayoutManager(activity)
        content.adapter = adapter

        val bus = parent.application.getSystemService(LastPickApp.BUS) as Bus
        presenter = BookmarksPresenterImpl(bus)
        presenter.attachView(this)
        presenter.getBookmarks()
    }
}
