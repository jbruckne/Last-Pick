package joebruckner.lastpick;

import javax.inject.Singleton;

import dagger.Component;
import joebruckner.lastpick.domain.DomainModule;
import joebruckner.lastpick.view.home.HomeActivity;
import joebruckner.lastpick.view.home.HomeComponent;
import joebruckner.lastpick.view.movie.MovieActivity;
import joebruckner.lastpick.view.movie.MovieComponent;
import joebruckner.lastpick.view.specials.SpecialsActivity;
import joebruckner.lastpick.view.specials.SpecialsComponent;

@Singleton
@Component(modules = {
        AppModule.class,
        DomainModule.class
})
public interface AppComponent {
    void inject(MainApp app);
    void inject(HomeActivity activity);
    void inject(MovieActivity activity);
    void inject(SpecialsActivity activity);

    HomeComponent getHomeComponent(ActivityModule activityModule);
    MovieComponent getMovieComponent(ActivityModule activityModule);
    SpecialsComponent getSpecialsComponent(ActivityModule activityModule);
}