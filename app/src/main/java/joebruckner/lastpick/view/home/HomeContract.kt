package joebruckner.lastpick.view.home

class HomeContract {

    interface Presenter {
        fun onAttach(view: View)
        fun onDetach()
    }

    interface View {

    }
}