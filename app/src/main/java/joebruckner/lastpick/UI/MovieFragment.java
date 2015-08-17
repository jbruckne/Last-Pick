package joebruckner.lastpick.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.model.MovieDb;
import joebruckner.lastpick.R;
import joebruckner.lastpick.actors.Actor;
import joebruckner.lastpick.presenters.MoviePresenter;
import joebruckner.lastpick.presenters.MoviePresenterImpl;
import joebruckner.lastpick.widgets.OnAnimationFinishedListener;


public class MovieFragment extends Fragment implements Actor<MovieDb> {
	@Bind(R.id.content) View content;
	@Bind({ R.id.title, R.id.summary, R.id.cast, R.id.year }) List<TextView> info;

	MoviePresenter presenter = new MoviePresenterImpl();
	Coordinator coordinator;

	Animation up;
	Animation down;

	@Override public void onAttach(Activity activity) {
		super.onAttach(activity);
		up = AnimationUtils.loadAnimation(activity, R.anim.slide_up);
		down = AnimationUtils.loadAnimation(activity, R.anim.slide_down);
		up.setAnimationListener(new OnAnimationFinishedListener() {
			@Override public void onAnimationEnd(Animation animation) {
				content.setVisibility(View.VISIBLE);
			}
		});
		down.setAnimationListener(new OnAnimationFinishedListener() {
			@Override public void onAnimationEnd(Animation animation) {
				content.setVisibility(View.GONE);
			}
		});
		if (activity instanceof Coordinator)
			coordinator = (Coordinator) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_movie, container, false);
	}

	@Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		presenter.attachActor(this);
		presenter.start();
		coordinator.getFab().setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				presenter.start();
			}
		});
	}

	@Override public void showLoading() {
		coordinator.collapseAppBar();
		content.startAnimation(down);
	}

	@Override public void showContent(MovieDb movie) {
		Snackbar.make(coordinator.getRootView(), movie.toString(), Snackbar.LENGTH_LONG).show();
		info.get(0).setText(movie.getTitle());
		info.get(1).setText(movie.getOverview());
		coordinator.expandAppBar();
		content.startAnimation(up);
	}

	@Override public void showError(String errorMessage) {
		Snackbar.make(coordinator.getRootView(), errorMessage, Snackbar.LENGTH_LONG).show();
	}
}
