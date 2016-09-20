package joebruckner.lastpick.view.home

import com.google.firebase.auth.FirebaseAuth
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.BookmarkInteractor
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.view.home.HomeContract.Presenter
import joebruckner.lastpick.view.home.HomeContract.View
import javax.inject.Inject

@ActivityScope
class HomePresenter @Inject constructor(
        val bookmarkInteractor: BookmarkInteractor,
        val navigator: FlowNavigator
) : Presenter, FirebaseAuth.AuthStateListener {
    var view: View? = null

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onAttach(view: View) {
        this.view = view
        auth.addAuthStateListener(this)
    }

    override fun onDetach() {
        auth.removeAuthStateListener(this)
        view = null
    }

    override fun onResume() {
        onAuthStateChanged(auth)
    }

    override fun onAccountStatusToggled() {
        if (auth.currentUser == null) navigator.showLogin()
        else navigator.showLogout()
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user = auth.currentUser
        if (user == null) {
            view?.showLoggedOut()
        } else {
            view?.showLoggedIn(
                    user.displayName!!,
                    user.email!!,
                    user.providerData[1].photoUrl.toString()
            )
            bookmarkInteractor.syncBookmarks()
        }
    }
}