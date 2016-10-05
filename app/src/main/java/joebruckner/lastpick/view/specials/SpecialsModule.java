package joebruckner.lastpick.view.specials;


import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import joebruckner.lastpick.ActivityScope;
import joebruckner.lastpick.domain.EventLogger;
import joebruckner.lastpick.domain.FlowNavigator;
import joebruckner.lastpick.domain.MovieInteractor;
import joebruckner.lastpick.domain.impl.FlowNavigatorImpl;

@Module
public class SpecialsModule {

    @Provides
    @ActivityScope
    FlowNavigator bindFlowNavigator(@Named("Activity") Context context) {
        return new FlowNavigatorImpl(context);
    }

    @Provides
    SpecialsContract.Presenter bindSpecialsPresenter(
            MovieInteractor movieInteractor,
            FlowNavigator flowNavigator,
            EventLogger eventLogger
    ) {
        return new SpecialsPresenter(movieInteractor, flowNavigator, eventLogger);
    }
}