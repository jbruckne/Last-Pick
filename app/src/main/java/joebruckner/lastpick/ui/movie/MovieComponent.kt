package joebruckner.lastpick.ui.movie

import dagger.Subcomponent
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.ui.movie.fragments.MovieFragment
import joebruckner.lastpick.ui.movie.fragments.MovieInfoFragment
import joebruckner.lastpick.ui.movie.fragments.MovieMediaFragment
import joebruckner.lastpick.ui.movie.fragments.MovieReviewFragment

@ActivityScope
@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        MovieModule::class
))
interface MovieComponent {
    fun inject(fragment: MovieFragment)
    fun inject(fragment: MovieInfoFragment)
    fun inject(fragment: MovieMediaFragment)
    fun inject(fragment: MovieReviewFragment)
}