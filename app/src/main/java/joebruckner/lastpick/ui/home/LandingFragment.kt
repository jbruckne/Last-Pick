package joebruckner.lastpick.ui.home


import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import joebruckner.lastpick.LastPickApp
import joebruckner.lastpick.R
import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.data.State
import joebruckner.lastpick.find
import joebruckner.lastpick.network.MovieManager
import joebruckner.lastpick.ui.common.BaseFragment
import joebruckner.lastpick.ui.movie.MovieAdapter
import joebruckner.lastpick.visibleIf

/**
 * A simple [Fragment] subclass.
 */
class LandingFragment : BaseFragment(), LandingContract.View {
    override val layoutId: Int = R.layout.fragment_landing

    val loading by lazy { find<View>(R.id.loading) }
    val content by lazy { find<View>(R.id.content) }
    val error   by lazy { find<TextView>(R.id.error) }
    val nowPlayingList  by lazy { find<RecyclerView>(R.id.now_playing_list) }
    val topRatedList    by lazy { find<RecyclerView>(R.id.top_rated_list) }
    val upcomingList    by lazy { find<RecyclerView>(R.id.upcoming_list) }
    val popularList     by lazy { find<RecyclerView>(R.id.popular_list) }

    private val presenter: LandingContract.Presenter by lazy { LandingPresenter(
            activity.application.getSystemService(LastPickApp.MOVIE_MANAGER) as MovieManager
    ) }
    private var state: State = State.LOADING

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

    override fun showContent(movies: List<Movie>, type: ListType) {
        updateViews()
        when (type) {
            ListType.NOW_PLAYING -> (nowPlayingList.adapter as MovieAdapter).setNewMovies(movies)
            ListType.TOP_RATED -> (topRatedList.adapter as MovieAdapter).setNewMovies(movies)
            ListType.UPCOMING -> (upcomingList.adapter as MovieAdapter).setNewMovies(movies)
            ListType.POPULAR -> (popularList.adapter as MovieAdapter).setNewMovies(movies)
        }
    }

    private fun getNewAdapter(): MovieAdapter = MovieAdapter(context, R.layout.card_movie_slide)

    override fun onStart() {
        super.onStart()

        nowPlayingList.adapter = getNewAdapter()
        topRatedList.adapter = getNewAdapter()
        upcomingList.adapter = getNewAdapter()
        popularList.adapter = getNewAdapter()

        presenter.attachView(this)
    }

    override fun onPause() {
        presenter.detachView()
        super.onPause()
    }
}
