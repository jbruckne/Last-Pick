package joebruckner.lastpick.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import joebruckner.lastpick.R;
import joebruckner.lastpick.actors.Actor;
import joebruckner.lastpick.presenters.CategoriesPresenter;
import joebruckner.lastpick.presenters.CategoriesPresenterImpl;


public class CategoriesFragment extends Fragment implements Actor<List<String>> {
	@Bind(R.id.content) View content;

	CategoriesPresenter presenter = new CategoriesPresenterImpl();
	Coordinator coordinator;

	Animation up;
	Animation down;

	@Override public void onAttach(Activity activity) {
		super.onAttach(activity);
		up = AnimationUtils.loadAnimation(activity, R.anim.slide_up);
		down = AnimationUtils.loadAnimation(activity, R.anim.slide_down);
		if (activity instanceof Coordinator)
			coordinator = (Coordinator) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_categories, container, false);
	}

	@Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		presenter.attachActor(this);
		presenter.start();
		coordinator.getFab().setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				presenter.savePreferences(null);
			}
		});
	}

	@Override public void showLoading() {
	}

	@Override public void showContent(List<String> strings) {

	}

	@Override public void showError(String errorMessage) {

	}
}
