package joebruckner.lastpick.view.specials

import dagger.Subcomponent
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.ActivityScope

@ActivityScope
@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        SpecialsModule::class
))
interface SpecialsComponent {
    fun inject(fragment: SpecialsFragment)
}