package joebruckner.lastpick.domain;


import com.google.firebase.analytics.FirebaseAnalytics;

import dagger.Module;
import dagger.Provides;
import joebruckner.lastpick.domain.impl.FirebaseAnalyticsLogger;
import joebruckner.lastpick.domain.impl.HistoryInteractorImpl;
import joebruckner.lastpick.domain.impl.MovieInteractorImpl;
import joebruckner.lastpick.source.history.LocalHistoryRepository;
import joebruckner.lastpick.source.movie.LocalMovieDataSource;
import joebruckner.lastpick.source.movie.NetworkMovieDataSource;

@Module
public class DomainModule {

    @Provides
    MovieInteractor bindMovieInteractor(
            NetworkMovieDataSource network,
            LocalMovieDataSource local
    ) {
        return new MovieInteractorImpl(network, local);
    }

    @Provides
    HistoryInteractor bindHistoryInteractor(
            LocalHistoryRepository localHistoryRepository
    ) {
        return new HistoryInteractorImpl(localHistoryRepository);
    }

    @Provides
    EventLogger bindEventLogger(FirebaseAnalytics analytics) {
        return new FirebaseAnalyticsLogger(analytics);
    }
}