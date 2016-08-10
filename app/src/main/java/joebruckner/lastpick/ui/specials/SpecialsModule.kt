package joebruckner.lastpick.ui.specials

import dagger.Binds
import dagger.Module

@Module
abstract class SpecialsModule {

    @Binds
    abstract fun bindSpecialsPresenter(presenter: SpecialsPresenter): SpecialsContract.Presenter

}