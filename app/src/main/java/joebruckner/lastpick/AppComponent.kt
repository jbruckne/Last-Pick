package joebruckner.lastpick

import dagger.Component
import joebruckner.lastpick.domain.DomainModule
import joebruckner.lastpick.view.home.HomeActivity
import joebruckner.lastpick.view.home.HomeComponent
import joebruckner.lastpick.view.movie.MovieActivity
import joebruckner.lastpick.view.movie.MovieComponent
import joebruckner.lastpick.view.specials.SpecialsActivity
import joebruckner.lastpick.view.specials.SpecialsComponent
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        DomainModule::class
))
interface AppComponent {
    fun inject(app: MainApp)
    fun inject(activity: HomeActivity)
    fun inject(activity: MovieActivity)
    fun inject(activity: SpecialsActivity)

    fun getHomeComponent(activityModule: ActivityModule): HomeComponent
    fun getMovieComponent(activityModule: ActivityModule): MovieComponent
    fun getSpecialsComponent(activityModule: ActivityModule): SpecialsComponent
}