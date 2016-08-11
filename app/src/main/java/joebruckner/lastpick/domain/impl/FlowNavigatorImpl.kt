package joebruckner.lastpick.domain.impl

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.util.Log
import joebruckner.lastpick.R
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.model.ListType
import joebruckner.lastpick.ui.about.AboutActivity
import joebruckner.lastpick.ui.home.HomeActivity
import joebruckner.lastpick.ui.movie.MovieActivity
import joebruckner.lastpick.ui.image.ImageFragment
import joebruckner.lastpick.ui.specials.SpecialsActivity
import joebruckner.lastpick.utils.replaceFrame
import javax.inject.Inject

class FlowNavigatorImpl @Inject constructor(
        val activity: AppCompatActivity
): FlowNavigator {

    override fun sendFeedback() {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("joebrucknerdev@gmail.com") +
                "?subject=" + Uri.encode("Feedback")
        val uri = Uri.parse(uriText)
        send.data = uri
        activity.startActivity(Intent.createChooser(send, "Send Feedback..."))
    }

    override fun share(url: String, message: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        message?.let { intent.putExtra(Intent.EXTRA_SUBJECT, it) }
        intent.putExtra(Intent.EXTRA_TEXT, url)
        activity.startActivity(Intent.createChooser(intent, "Share with"))
    }

    override fun view(url: String) {
        Log.d("View", url)
        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun showHome() {
        activity.startActivity(Intent(activity, HomeActivity::class.java))
    }

    override fun showRandom() {
        activity.startActivity(Intent(activity, MovieActivity::class.java))
    }

    override fun showMovie(id: Int) {
        val intent = Intent(activity, MovieActivity::class.java)
        intent.putExtra("movie", id)
        activity.startActivity(intent)
    }

    override fun showSpecial(type: ListType) {
        val intent = Intent(activity, SpecialsActivity::class.java)
        intent.putExtra("type", type.ordinal)
        activity.startActivity(intent)
    }

    override fun showAbout() {
        activity.startActivity(Intent(activity, AboutActivity::class.java))
    }

    override fun showImage(url: String) {
        activity.replaceFrame(R.id.frame, ImageFragment.newInstance(url), true)
    }
}