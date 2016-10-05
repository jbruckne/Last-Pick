package joebruckner.lastpick.view.home;


import dagger.Subcomponent;
import joebruckner.lastpick.ActivityModule;
import joebruckner.lastpick.ActivityScope;
import joebruckner.lastpick.domain.FlowNavigator;
import joebruckner.lastpick.view.home.bookmark.BookmarkFragment;
import joebruckner.lastpick.view.home.history.HistoryFragment;
import joebruckner.lastpick.view.home.landing.LandingFragment;

@ActivityScope
@Subcomponent(modules = {
        ActivityModule.class,
        HomeModule.class
        })
public interface HomeComponent {
    void inject(HomeActivity activity);
    void inject(LandingFragment fragment);
    void inject(HistoryFragment fragment);
    void inject(BookmarkFragment fragment);

    FlowNavigator getNavigator();
    HomePresenter getHomePresenter();
}