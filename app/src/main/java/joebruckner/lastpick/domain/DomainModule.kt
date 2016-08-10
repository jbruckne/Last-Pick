package joebruckner.lastpick.domain

import dagger.Binds
import dagger.Module
import joebruckner.lastpick.domain.impl.BookmarkInteractorImpl
import joebruckner.lastpick.domain.impl.FirebaseAnalyticsLogger
import joebruckner.lastpick.domain.impl.HistoryInteractorImpl
import joebruckner.lastpick.domain.impl.MovieInteractorImpl

@Module
abstract class DomainModule {

    @Binds
    abstract fun bindMovieInteractor(interactor: MovieInteractorImpl): MovieInteractor

    @Binds
    abstract fun bindHistoryInteractor(interactor: HistoryInteractorImpl): HistoryInteractor

    @Binds
    abstract fun bindBookmarkInteractor(interactor: BookmarkInteractorImpl): BookmarkInteractor

    @Binds
    abstract fun bindEventLogger(logger: FirebaseAnalyticsLogger): EventLogger
}