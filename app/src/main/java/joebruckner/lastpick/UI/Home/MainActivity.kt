package joebruckner.lastpick.ui.home


import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.graphics.Palette
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import joebruckner.lastpick.R
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.ui.common.DetailActivity
import joebruckner.lastpick.ui.history.HistoryActivity
import joebruckner.lastpick.widgets.ImageBlur
import joebruckner.lastpick.widgets.PaletteMagic
import kotlinx.android.synthetic.activity_main.*

class MainActivity : DetailActivity(), RequestListener<String, Bitmap> {
    override val layoutId = R.layout.activity_main
    lateinit var blur: BitmapTransformation

    private var fabIsEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        val movie = intent.extras?.getString("movie") ?: null
        val fragment = MovieFragment(movie)

        fab.setOnClickListener({ sendAction(Action.SHUFFLE) });
        replaceFrame(R.id.frame, fragment, false)
        blur = ImageBlur(this, 20f)
        appBar.addOnOffsetChangedListener { layout, i ->
            poster.elevation = if(layout.y < -40) 0f else 4f
            poster.alpha = (100 + layout.y) / 100
        }
    }

    override fun setTitle(title: String) {
        collapsingToolbar.title = title
    }

    override fun setBackdrop(imagePath: String) {
        Glide.with(this).load(imagePath)
                .asBitmap()
                .listener(this)
                .transform(blur)
                .into(backdrop)
    }

    override fun setPoster(imagePath: String) {
        Glide.with(this).load(imagePath)
                .crossFade()
                .into(poster)
    }

    override fun onException(e: Exception, model: String,
                             target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
        Log.e("Glide", e.toString());
        return false;
    }

    override fun onResourceReady(resource: Bitmap, model: String, target: Target<Bitmap>,
                                 isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
        Palette.from(resource).generate { palette ->
            val magic = PaletteMagic(palette)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                window.statusBarColor = magic.dark
            appBar.setBackgroundColor(magic.primary)
            collapsingToolbar.setBackgroundColor(magic.primary)
            backdrop.setBackgroundColor(magic.primary)
            poster.setBackgroundColor(magic.accent)
            collapsingToolbar.setContentScrimColor(magic.primary)
            fab.backgroundTintList = ColorStateList.valueOf(magic.accent)
        }
        return false
    }

    override fun enableFab() {
        fabIsEnabled = true
        fab.show(object: FloatingActionButton.OnVisibilityChangedListener() {
            override fun onShown(f: FloatingActionButton?) {
                if (!fabIsEnabled) disableFab()
            }
        })
    }

    override fun disableFab() {
        fabIsEnabled = false
        fab.hide(object: FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(f: FloatingActionButton?) {
                if (fabIsEnabled) enableFab()
            }
        })
    }

    override fun removeFab() {
        fab.visibility = View.GONE
    }

    override fun clearBackdrop() {
        backdrop.setImageResource(android.R.color.transparent)
    }

    override fun clearPoster() {
        poster.setImageResource(android.R.color.transparent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                showHistory()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showHistory() {
        startActivity(Intent(this, javaClass<HistoryActivity>()))
    }
}