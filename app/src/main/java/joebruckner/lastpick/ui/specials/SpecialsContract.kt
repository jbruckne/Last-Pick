package joebruckner.lastpick.ui.specials

import joebruckner.lastpick.data.CondensedMovie
import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.data.State

class SpecialsContract {
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun getSpecialList(type: ListType)
    }
    interface View {
        fun showError(errorMessage: String)
        fun showContent(content: List<CondensedMovie>)
        fun showLoading()
        var state: State
    }
}