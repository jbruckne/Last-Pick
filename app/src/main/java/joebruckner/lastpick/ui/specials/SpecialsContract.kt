package joebruckner.lastpick.ui.specials

import joebruckner.lastpick.model.ListType
import joebruckner.lastpick.model.State
import joebruckner.lastpick.model.tmdb.CondensedMovie

class SpecialsContract {
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun getSpecialList(type: ListType)
        fun movieSelected(movie: CondensedMovie)
    }
    interface View {
        fun showError(errorMessage: String)
        fun showContent(content: List<CondensedMovie>)
        fun showLoading()
        var state: State
    }
}