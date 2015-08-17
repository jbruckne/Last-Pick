package joebruckner.lastpick.presenters;

import java.util.List;

import joebruckner.lastpick.actors.Actor;

public interface CategoriesPresenter {
	void attachActor(Actor<List<String>> actor);
	void detachActor();
	void start();
	void savePreferences(List<String> preferences);
}
