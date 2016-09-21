package joebruckner.lastpick.view.movie

import dagger.Subcomponent
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.view.movie.MovieFragment

@ActivityScope
@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        MovieModule::class
))
interface MovieComponent {
    fun inject(fragment: MovieFragment)
}