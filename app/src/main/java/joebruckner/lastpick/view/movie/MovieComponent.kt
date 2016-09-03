package joebruckner.lastpick.view.movie

import dagger.Subcomponent
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.view.movie.fragments.MovieFragment
import joebruckner.lastpick.view.movie.fragments.MovieInfoFragment
import joebruckner.lastpick.view.movie.fragments.MovieMediaFragment
import joebruckner.lastpick.view.movie.fragments.MovieReviewFragment

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