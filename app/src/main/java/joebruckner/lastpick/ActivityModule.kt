package joebruckner.lastpick

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.domain.impl.FlowNavigatorImpl
import javax.inject.Named

@Module
class ActivityModule(val activity: Activity) {

    @Provides
    @ActivityScope
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    @ActivityScope
    @Named("Activity")
    fun provideContext(): Context {
        return activity
    }

    @Provides
    @ActivityScope
    fun provideFlowNavigator(): FlowNavigator {
        return FlowNavigatorImpl(activity)
    }

}