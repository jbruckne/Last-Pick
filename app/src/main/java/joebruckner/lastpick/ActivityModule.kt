package joebruckner.lastpick

import android.content.Context
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ActivityModule(val activity: AppCompatActivity) {

    @Provides
    @ActivityScope
    fun provideActivity(): AppCompatActivity {
        return activity
    }

    @Provides
    @ActivityScope
    @Named("Activity")
    fun provideContext(): Context {
        return activity
    }

}