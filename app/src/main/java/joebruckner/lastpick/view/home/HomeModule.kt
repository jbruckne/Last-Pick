package joebruckner.lastpick.view.home

import dagger.Binds
import dagger.Module
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.domain.impl.FlowNavigatorImpl
import joebruckner.lastpick.view.home.bookmark.BookmarkContract
import joebruckner.lastpick.view.home.bookmark.BookmarkPresenter
import joebruckner.lastpick.view.home.history.HistoryContract
import joebruckner.lastpick.view.home.history.HistoryPresenter
import joebruckner.lastpick.view.home.landing.LandingContract
import joebruckner.lastpick.view.home.landing.LandingPresenter

@Module
abstract class HomeModule {

    @Binds
    @ActivityScope
    abstract fun bindFlowNavigator(navigator: FlowNavigatorImpl): FlowNavigator

    @Binds
    @ActivityScope
    abstract fun bindHomePresenter(presenter: HomePresenter): HomeContract.Presenter

    @Binds
    @ActivityScope
    abstract fun provideLandingPresenter(presenter: LandingPresenter): LandingContract.Presenter

    @Binds
    @ActivityScope
    abstract fun provideHistoryPresenter(presenter: HistoryPresenter): HistoryContract.Presenter

    @Binds
    @ActivityScope
    abstract fun provideBookmarkPresenter(presenter: BookmarkPresenter): BookmarkContract.Presenter
}