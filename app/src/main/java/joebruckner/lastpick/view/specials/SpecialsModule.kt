package joebruckner.lastpick.view.specials

import dagger.Binds
import dagger.Module
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.domain.impl.FlowNavigatorImpl

@Module
abstract class SpecialsModule {

    @Binds
    @ActivityScope
    abstract fun bindFlowNavigator(navigator: FlowNavigatorImpl): FlowNavigator

    @Binds
    abstract fun bindSpecialsPresenter(presenter: SpecialsPresenter): SpecialsContract.Presenter
}