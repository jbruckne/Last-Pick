package joebruckner.lastpick;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    @Named("Activity")
    Context provideContext() {
        return activity;
    }

}