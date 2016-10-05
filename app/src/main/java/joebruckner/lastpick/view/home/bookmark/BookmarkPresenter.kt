package joebruckner.lastpick.view.home.bookmark

import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.domain.MovieInteractor
import joebruckner.lastpick.model.presentation.Movie
import joebruckner.lastpick.source.collection.CollectionManager
import joebruckner.lastpick.utilities.applySchedulers
import javax.inject.Inject

@ActivityScope
class BookmarkPresenter @Inject constructor(
        val collections: CollectionManager,
        val movieInteractor: MovieInteractor,
        val navigator: FlowNavigator
): BookmarkContract.Presenter {
    private var view: BookmarkContract.View? = null

    override fun attachView(view: BookmarkContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun getBookmarks() {
        view?.showLoading()
        collections
                .getCollection(0)
                .flatMapIterable { it }
                .flatMap { movieInteractor.getMovie(it) }
                .toList()
                .compose(applySchedulers<List<Movie>>())
                .subscribe ({
                    view?.showContent(it.sortedBy { it.title.first().toUpperCase() - 'A' })
                }, {
                    it.printStackTrace()
                    view?.showError("Oops, there was a problem loading your bookmarks.")
                })
    }
}