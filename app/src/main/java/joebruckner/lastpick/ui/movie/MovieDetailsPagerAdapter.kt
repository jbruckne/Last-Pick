package joebruckner.lastpick.ui.movie

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MovieDetailsPagerAdapter(val fragmentManager: FragmentManager):
        FragmentPagerAdapter(fragmentManager) {

    val info = MovieInfoFragment()
    val videos = MovieVideosFragment()

    override fun getItem(position: Int) = when (position) {
        0 -> info
        else -> videos
    }

    override fun getCount() = 2
}