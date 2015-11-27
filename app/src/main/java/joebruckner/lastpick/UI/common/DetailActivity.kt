package joebruckner.lastpick.ui.common

/**
 * Created by joebruckner on 11/27/15.
 */
abstract class DetailActivity: BaseActivity() {

    abstract fun setTitle(title: String)

    abstract fun setBackdrop(imagePath: String)

    abstract fun setPoster(imagePath: String)

    abstract fun clearBackdrop()

    abstract fun clearPoster()

    abstract fun enableFab()

    abstract fun disableFab()
}