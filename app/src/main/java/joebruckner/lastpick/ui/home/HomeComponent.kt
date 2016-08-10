package joebruckner.lastpick.ui.home

import dagger.Subcomponent
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.ui.home.HomeModule
import joebruckner.lastpick.ui.home.bookmark.BookmarkFragment
import joebruckner.lastpick.ui.home.history.HistoryFragment
import joebruckner.lastpick.ui.home.landing.LandingFragment

@ActivityScope
@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        HomeModule::class
))
interface HomeComponent {
    fun inject(fragment: LandingFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: BookmarkFragment)
}