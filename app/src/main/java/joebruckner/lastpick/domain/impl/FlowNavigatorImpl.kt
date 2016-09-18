package joebruckner.lastpick.domain.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import joebruckner.lastpick.R
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.model.Showcase
import joebruckner.lastpick.view.about.AboutActivity
import joebruckner.lastpick.view.home.HomeActivity
import joebruckner.lastpick.view.movie.MovieActivity
import joebruckner.lastpick.view.specials.SpecialsActivity
import javax.inject.Inject
import javax.inject.Named

class FlowNavigatorImpl @Inject constructor(
        @Named("Activity") val context: Context
): FlowNavigator {

    override fun sendFeedback() {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("joebrucknerdev@gmail.com") +
                "?subject=" + Uri.encode("Feedback")
        val uri = Uri.parse(uriText)
        send.data = uri
        context.startActivity(Intent.createChooser(send, "Send Feedback..."))
    }

    override fun share(url: String, message: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        message?.let { intent.putExtra(Intent.EXTRA_SUBJECT, it) }
        intent.putExtra(Intent.EXTRA_TEXT, url)
        context.startActivity(Intent.createChooser(intent, "Share with"))
    }

    override fun view(url: String) {
        Log.d("View", url)
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun showHome() {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }

    override fun showRandom() {
        context.startActivity(Intent(context, MovieActivity::class.java))
    }

    override fun showMovie(id: Int) {
        val intent = Intent(context, MovieActivity::class.java)
        intent.putExtra("movie", id)
        context.startActivity(intent)
    }

    override fun showSpecial(type: Showcase) {
        val intent = Intent(context, SpecialsActivity::class.java)
        intent.putExtra("type", type.ordinal)
        context.startActivity(intent)
    }

    override fun showAbout() {
        context.startActivity(Intent(context, AboutActivity::class.java))
    }

    override fun showLogin() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            val intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setProviders(AuthUI.EMAIL_PROVIDER, AuthUI.GOOGLE_PROVIDER)
                    .setLogo(R.mipmap.ic_launcher)
                    .build()
            (context as Activity).startActivityForResult(intent, 42)
        } else {
            AuthUI.getInstance().signOut(context as Activity)
        }
        //context.startActivity(Intent(context, LoginActivity::class.java))
    }
}