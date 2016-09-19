package joebruckner.lastpick.view.home

class HomeContract {

    interface Presenter {
        fun onAttach(view: View)
        fun onDetach()
        fun onResume()
        fun onAccountStatusToggled()
    }

    interface View {
        fun showLoggedIn(name: String, email: String, profilePic: String)
        fun showLoggedOut()
    }
}