package joebruckner.lastpick.ui.common

abstract class DetailActivity: BaseActivity() {

    abstract fun setTitle(title: String)

    abstract fun setBackdrop(imagePath: String)

    abstract fun setPoster(imagePath: String)

    abstract fun clearBackdrop()

    abstract fun clearPoster()

    abstract fun enableFab()

    abstract fun disableFab()

    abstract fun removeFab()
}