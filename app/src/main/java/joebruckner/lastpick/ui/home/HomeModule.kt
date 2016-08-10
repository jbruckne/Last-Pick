package joebruckner.lastpick.ui.home

import dagger.Binds
import dagger.Module
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.ui.home.bookmark.BookmarkContract
import joebruckner.lastpick.ui.home.bookmark.BookmarkPresenter
import joebruckner.lastpick.ui.home.history.HistoryContract
import joebruckner.lastpick.ui.home.history.HistoryPresenter
import joebruckner.lastpick.ui.home.landing.LandingContract
import joebruckner.lastpick.ui.home.landing.LandingPresenter

@Module
abstract class HomeModule {

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