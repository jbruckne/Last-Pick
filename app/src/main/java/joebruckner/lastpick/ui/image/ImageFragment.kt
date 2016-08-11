package joebruckner.lastpick.ui.image

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import joebruckner.lastpick.R
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.load

class ImageFragment: Fragment() {
    // Views
    val imageView: ImageView get() = find(R.id.image)
    val loading: View get() = find(R.id.loading)

    // Arguments
    val image: String by lazy { arguments.getString("imageUrl") }

    override fun onStart() {
        super.onStart()
        imageView.visibility = View.INVISIBLE
        loading.visibility = View.VISIBLE
        imageView.load(context, image, {
            Log.d("Image", "Loaded")
            imageView.visibility = View.VISIBLE
            loading.visibility = View.INVISIBLE
        }, { error ->
            error.printStackTrace()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    companion object {
        fun newInstance(imageUrl: String): ImageFragment {
            val args = Bundle()
            args.putString("imageUrl", imageUrl)
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}