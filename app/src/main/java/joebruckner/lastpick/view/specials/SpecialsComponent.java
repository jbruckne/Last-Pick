package joebruckner.lastpick.view.specials;

import dagger.Subcomponent;
import joebruckner.lastpick.ActivityModule;
import joebruckner.lastpick.ActivityScope;

@ActivityScope
@Subcomponent(modules = {
        ActivityModule.class,
        SpecialsModule.class
        })
public interface SpecialsComponent {
    void inject(SpecialsFragment fragment);
}