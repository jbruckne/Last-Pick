package joebruckner.lastpick.presenters;

import android.support.annotation.Nullable;

import java.util.List;

import joebruckner.lastpick.actors.Actor;

public class CategoriesPresenterImpl implements CategoriesPresenter {
	@Nullable Actor actor;

	@Override public void attachActor(Actor<List<String>> actor) {
		this.actor = actor;
	}

	@Override public void detachActor() {
		this.actor = null;
	}

	@Override public void start() {
		if (actor != null)
			actor.showLoading();
	}

	@Override public void savePreferences(List<String> preferences) {
		if (actor != null)
			actor.showLoading();
	}
}