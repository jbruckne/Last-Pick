package joebruckner.lastpick

import dagger.Component
import joebruckner.lastpick.domain.DomainModule
import joebruckner.lastpick.ui.home.HomeActivity
import joebruckner.lastpick.ui.home.HomeComponent
import joebruckner.lastpick.ui.movie.MovieActivity
import joebruckner.lastpick.ui.movie.MovieComponent
import joebruckner.lastpick.ui.specials.SpecialsActivity
import joebruckner.lastpick.ui.specials.SpecialsComponent
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