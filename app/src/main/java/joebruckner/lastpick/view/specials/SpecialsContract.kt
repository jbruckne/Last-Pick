package joebruckner.lastpick.view.specials

import joebruckner.lastpick.model.presentation.Showcase
import joebruckner.lastpick.model.presentation.State
import joebruckner.lastpick.model.tmdb.SlimMovie

class SpecialsContract {
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun getSpecialList(type: Showcase)
        fun movieSelected(movie: SlimMovie)
    }
    interface View {
        fun showError(errorMessage: String)
        fun showContent(content: List<SlimMovie>)
        fun showLoading()
        var state: State
    }
}