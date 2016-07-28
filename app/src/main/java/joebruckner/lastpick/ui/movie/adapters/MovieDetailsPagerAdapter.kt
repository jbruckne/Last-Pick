package joebruckner.lastpick.ui.movie.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import joebruckner.lastpick.data.Movie
import joebruckner.lastpick.ui.movie.MovieInfoFragment
import joebruckner.lastpick.ui.movie.MovieVideosFragment

class MovieDetailsPagerAdapter(fragmentManager: FragmentManager):
        FragmentPagerAdapter(fragmentManager) {

    private var info = MovieInfoFragment()
    private var videos = MovieVideosFragment()

    fun updateMovie(movie: Movie) {
        info.updateMovie(movie)
        videos.updateMovie(movie)
    }

    fun scrollToTop() {
        info.scrollToTop()
    }

    override fun getItem(position: Int) = when (position) {
        0 -> info
        1 -> videos
        else -> null
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Info"
        1 -> "Videos"
        else -> null
    }
}