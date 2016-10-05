package joebruckner.lastpick.view.movie;


import dagger.Subcomponent;
import joebruckner.lastpick.ActivityModule;
import joebruckner.lastpick.ActivityScope;
import joebruckner.lastpick.view.movie.fragments.MovieFragment;
import joebruckner.lastpick.view.movie.fragments.MovieInfoFragment;
import joebruckner.lastpick.view.movie.fragments.MovieMediaFragment;
import joebruckner.lastpick.view.movie.fragments.MovieReviewFragment;

@ActivityScope
@Subcomponent(modules = {
        ActivityModule.class,
        MovieModule.class
        })
public interface MovieComponent {
    void inject(MovieFragment fragment);
    void inject(MovieInfoFragment fragment);
    void inject(MovieMediaFragment fragment);
    void inject(MovieReviewFragment fragment);
}