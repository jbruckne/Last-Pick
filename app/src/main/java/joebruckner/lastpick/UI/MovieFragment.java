package joebruckner.lastpick.ui;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.Bind;
import butterknife.ButterKnife;
import joebruckner.lastpick.LastPickApp;
import joebruckner.lastpick.R;
import joebruckner.lastpick.data.Movie;
import joebruckner.lastpick.data.MovieViewHolder;
import joebruckner.lastpick.presenters.MoviePresenter;
import joebruckner.lastpick.presenters.MoviePresenterImpl;
import joebruckner.lastpick.widgets.OnAnimationFinishedListener;


public class MovieFragment extends Fragment implements
        MoviePresenter.MovieView, RequestListener<String, Bitmap> {
	@Bind(R.id.content) View content;
	@Bind(R.id.poster) ImageView poster;

	MoviePresenter presenter;
	Coordinator coordinator;
	MovieViewHolder holder;

	private Animation enter;
	private Animation exit;

    private boolean isLoading = true;

	@Override public void onAttach(Context context) {
		super.onAttach(context);
		enter = AnimationUtils.loadAnimation(context, R.anim.slide_up);
		enter.setAnimationListener(new OnAnimationFinishedListener() {
			@Override public void onAnimationEnd(Animation animation) {
				onAnimationFinished(animation);
			}
		});
		exit = AnimationUtils.loadAnimation(context, R.anim.slide_down);
		exit.setAnimationListener(new OnAnimationFinishedListener() {
			@Override public void onAnimationEnd(Animation animation) {
				onAnimationFinished(animation);
			}
		});
		if (context instanceof Coordinator)
			coordinator = (Coordinator) context;

        LastPickApp app = (LastPickApp) getActivity().getApplication();
        presenter = new MoviePresenterImpl(app.getBus());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_movie, container, false);
	}

	@Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		holder = new MovieViewHolder(view);
		presenter.attachActor(this);
		presenter.shuffleMovie();
		coordinator.getFab().setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				presenter.shuffleMovie();
			}
		});
	}

	@Override public void showLoading() {
        isLoading = true;
		coordinator.getFab().setClickable(false);
		content.startAnimation(exit);
	}

	@Override public void showContent(Movie movie) {
        isLoading = false;
		Log.d(this.getClass().getSimpleName(), "Showing Movie " + movie.toString());
		Glide.with(this).load(movie.getBackdropPath())
				.asBitmap()
				.listener(this)
				.centerCrop()
				.into(poster);
		Glide.with(this).load(movie.getPosterPath())
				.centerCrop()
				.into(coordinator.getBackdrop());
		holder.setData(movie);
		if (exit.hasStarted()) {
			exit.setAnimationListener(new OnAnimationFinishedListener() {
				@Override public void onAnimationEnd(Animation animation) {
					content.startAnimation(enter);
				}
			});
		} else content.startAnimation(enter);
	}

	@Override public void showError(String errorMessage) {
        isLoading = false;
		Snackbar.make(coordinator.getRootView(), errorMessage, Snackbar.LENGTH_LONG).show();
	}

	protected void onAnimationFinished(Animation animation) {
		if (animation.equals(enter))
			coordinator.getFab().setClickable(true);
	}

	@Override
	public boolean onException(Exception e, String model, Target<Bitmap> target,
	                           boolean isFirstResource) {
		Log.e("Glide", e.toString());
		return false;
	}

	@Override
	public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target,
	                               boolean isFromMemoryCache, boolean isFirstResource) {
		Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
			@Override public void onGenerated(Palette palette) {
				generateTheme(palette);
			}
		});
		return false;
	}

	private void generateTheme(Palette palette) {
		Palette.Swatch muted = palette.getMutedSwatch();
		Palette.Swatch vibrant = palette.getVibrantSwatch();
		int primary = Color.parseColor("#455A64");
		int dark = Color.parseColor("#263238");
		int accent = Color.parseColor("#607D8B");
		if (muted != null) {
			Log.d("Palette", "Muted swatch used");
			primary = palette.getMutedColor(Color.BLACK);
			float[] hsl = muted.getHsl();
			hsl[2] *= 0.9;
			dark = Color.HSVToColor(hsl);
			hsl[2] *= 1.8;
			accent = Color.HSVToColor(hsl);
		} else if (vibrant != null) {
			Log.d("Palette", "Vibrant swatch used");
			primary = palette.getVibrantColor(Color.BLACK);
			float[] hsl = vibrant.getHsl();
			hsl[2] *= 0.9;
			dark = Color.HSVToColor(hsl);
			hsl[2] *= 1.8;
			accent = Color.HSVToColor(hsl);
		} else Log.d("Palette", "Default swatch used");
		coordinator.setThemeColors(primary, dark, accent);
		poster.setBackgroundColor(primary);
	}

    @Override
    public boolean isLoading() {
        return isLoading;
    }
}
