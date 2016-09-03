package joebruckner.lastpick.ui.movie

import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.StaggeredGridLayoutManager
import dagger.Module
import dagger.Provides
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.domain.FlowNavigator
import joebruckner.lastpick.domain.impl.FlowNavigatorImpl
import joebruckner.lastpick.widgets.GridAutoFitLayoutManager

@Module
class MovieModule {

    @Provides
    @ActivityScope
    fun bindFlowNavigator(navigator: FlowNavigatorImpl): FlowNavigator {
        return navigator
    }

    @Provides
    @ActivityScope
    fun provideMoviePresenter(presenter: MoviePresenter): MovieContract.Presenter {
        return presenter
    }

    @Provides
    fun provideLayoutManager(): StaggeredGridLayoutManager {
        return GridAutoFitLayoutManager(2, OrientationHelper.HORIZONTAL)
    }
}