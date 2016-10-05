package joebruckner.lastpick.view.movie;


import android.content.Context;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;

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
import joebruckner.lastpick.widgets.GridAutoFitLayoutManager;

@Module
public class MovieModule {

    @Provides
    @ActivityScope
    FlowNavigator bindFlowNavigator(@Named("Activity") Context context) {
        return new FlowNavigatorImpl(context);
    }

    @Provides
    @ActivityScope
    MovieContract.Presenter provideMoviePresenter(
            MovieInteractor movieInteractor,
            HistoryInteractor historyInteractor,
            CollectionManager collectionManager,
            FlowNavigator flowNavigator,
            EventLogger eventLogger
    ) {
        return new MoviePresenter(
                movieInteractor,
                historyInteractor,
                collectionManager,
                flowNavigator,
                eventLogger
        );
    }

    @Provides
    StaggeredGridLayoutManager provideLayoutManager() {
        return new GridAutoFitLayoutManager(2, OrientationHelper.HORIZONTAL);
    }
}