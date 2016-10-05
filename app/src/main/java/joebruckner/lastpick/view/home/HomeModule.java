package joebruckner.lastpick.view.home;


import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import joebruckner.lastpick.ActivityScope;
import joebruckner.lastpick.domain.EventLogger;
import joebruckner.lastpick.domain.FlowNavigator;
import joebruckner.lastpick.domain.HistoryInteractor;
import joebruckner.lastpick.domain.MovieInteractor;
import joebruckner.lastpick.domain.impl.FlowNavigatorImpl;
import joebruckner.lastpick.source.collection.CollectionManager;
import joebruckner.lastpick.view.home.bookmark.BookmarkContract;
import joebruckner.lastpick.view.home.bookmark.BookmarkPresenter;
import joebruckner.lastpick.view.home.history.HistoryContract;
import joebruckner.lastpick.view.home.history.HistoryPresenter;
import joebruckner.lastpick.view.home.landing.LandingContract;
import joebruckner.lastpick.view.home.landing.LandingPresenter;

@Module
public class HomeModule {

    @Provides
    @ActivityScope
    FlowNavigator bindFlowNavigator(@Named("Activity") Context context) {
        return new FlowNavigatorImpl(context);
    }

    @Provides
    @ActivityScope
    HomeContract.Presenter bindHomePresenter(
            CollectionManager collectionManager,
            FlowNavigator flowNavigator
    ) {
        return new HomePresenter(collectionManager, flowNavigator);
    }

    @Provides
    @ActivityScope
    LandingContract.Presenter provideLandingPresenter(
            MovieInteractor movieInteractor,
            FlowNavigator flowNavigator,
            EventLogger eventLogger
    ) {
        return new LandingPresenter(movieInteractor, flowNavigator, eventLogger);
    }

    @Provides
    @ActivityScope
    HistoryContract.Presenter provideHistoryPresenter(
            MovieInteractor movieInteractor,
            HistoryInteractor historyInteractor,
            FlowNavigator flowNavigator
    ) {
        return new HistoryPresenter(movieInteractor, historyInteractor, flowNavigator);
    }

    @Provides
    @ActivityScope
    BookmarkContract.Presenter provideBookmarkPresenter(
            CollectionManager collectionManager,
            MovieInteractor movieInteractor,
            FlowNavigator flowNavigator
    ) {
        return new BookmarkPresenter(collectionManager, movieInteractor, flowNavigator);
    }
}