package joebruckner.lastpick.presenters

import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.data.*
import joebruckner.lastpick.presenters.MoviePresenter.MovieView

class MoviePresenterImpl(val bus: Bus) : MoviePresenter {

    private var view: MovieView? = null
    private var registered = false
    private val genres = Genre.getAll()
    private var selected = BooleanArray(genres.size) { true }

    override fun attachActor(view: MovieView) {
        this.view = view
        if (!registered) bus.register(this)
        registered = true
    }

    override fun detachActor() {
        bus.unregister(this)
        this.view = null
        registered = false
    }

    override fun updateMovie() {
        view?.showLoading()
        bus.post(MovieRequest(
                if (selected[0]) null
                else genres.filter { selected[genres.indexOf(it)] }
        ))
    }

    override fun updateBookmark(movie: Movie, isAdding: Boolean) {
        bus.post(BookmarkUpdateRequest(movie, isAdding))
    }

    override fun updateGenreFilter(selected: BooleanArray) {
        this.selected = selected
    }

    override fun getSelectedGenres() = selected

    fun updateYearFilter() {
        //TODO
    }

    fun updateRatingFilter() {
        //TODO
    }

    fun updateKeywordFilter() {
        //TODO
    }

    @Subscribe fun bookmarkChanged(event: BookmarkUpdateEvent) {
        val id = view?.getContent()?.id
        if (id != event.movie.id) return
        if (event.isSuccess) view?.showBookmarkUpdate(event.movie.isBookmarked)
        else view?.showBookmarkError(event.movie.isBookmarked)
    }

    @Subscribe fun onNewMovie(event: MovieEvent) {
        if (view?.isLoading ?: false)
            view?.showContent(event.movie)
    }

    @Subscribe fun errorThrown(event: ErrorEvent) {
        if (view?.isLoading ?: false)
            view?.showError(event.message)
    }
}