package joebruckner.lastpick.ui.movie.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import joebruckner.lastpick.ui.movie.fragments.MovieInfoFragment
import joebruckner.lastpick.ui.movie.fragments.MovieMediaFragment
import joebruckner.lastpick.ui.movie.fragments.MovieReviewFragment

class MovieDetailsPagerAdapter(fragmentManager: FragmentManager):
        FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MovieInfoFragment()
            1 -> MovieMediaFragment()
            2 -> MovieReviewFragment()
            else -> throw Exception("Invalid position for movie pager adapter")
        }
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Info"
        1 -> "Media"
        2 -> "Reviews"
        else -> null
    }
}