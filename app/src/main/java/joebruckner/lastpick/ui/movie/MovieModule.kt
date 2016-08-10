package joebruckner.lastpick.ui.movie

import dagger.Binds
import dagger.Module

@Module
abstract class MovieModule {

    @Binds
    abstract fun provideMoviePresenter(presenter: MoviePresenter): MovieContract.Presenter
}