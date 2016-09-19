package joebruckner.lastpick.view.home

import dagger.Subcomponent
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.view.home.bookmark.BookmarkFragment
import joebruckner.lastpick.view.home.history.HistoryFragment
import joebruckner.lastpick.view.home.landing.LandingFragment

@ActivityScope
@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        HomeModule::class
))
interface HomeComponent {
    fun inject(activity: HomeActivity)
    fun inject(fragment: LandingFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: BookmarkFragment)

    fun getNavigator(): FlowNavigator
    fun getHomePresenter(): HomePresenter
}