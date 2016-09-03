package joebruckner.lastpick.view.home.bookmark

import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.BookmarkInteractor
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.model.Movie
import joebruckner.lastpick.utils.applySchedulers
import javax.inject.Inject

@ActivityScope
class BookmarkPresenter @Inject constructor(
        val bookmarkManager: BookmarkInteractor,
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
        bookmarkManager
                .getBookmarks()
                .compose(applySchedulers<List<Movie>>())
                .subscribe ({
                    view?.showContent(it.sortedBy { it.title.first().toUpperCase() - 'A' })
                }, {
                    it.printStackTrace()
                    view?.showError("Oops, there was a problem loading your bookmarks.")
                })
    }
}