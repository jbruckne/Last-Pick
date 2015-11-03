package joebruckner.lastpick.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import joebruckner.lastpick.R;
import joebruckner.lastpick.widgets.ControllableAppBarLayout;

public class MainActivity extends AppCompatActivity implements Coordinator {
	@Bind(R.id.root) CoordinatorLayout root;
	@Bind(R.id.toolbar) Toolbar toolbar;
	@Bind(R.id.app_bar) ControllableAppBarLayout appBarLayout;
	@Bind(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingLayout;
	@Bind(R.id.fab) FloatingActionButton fab;
	@Bind(R.id.movie_bg) ImageView backdrop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		setToolbar();
		setContent();
	}

	private void setToolbar() {
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		collapsingLayout.setTitle("");
		collapsingLayout.setExpandedTitleColor(Color.parseColor("#FFC107"));
	}

	private void setContent() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content, new MovieFragment())
				.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setTitle(@NonNull String title) {
		collapsingLayout.setTitle(title);
	}

	@Override public void setThemeColors(int primary, int primaryDark, int accent) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			getWindow().setStatusBarColor(primaryDark);
		backdrop.setBackgroundColor(primary);
		collapsingLayout.setContentScrimColor(primary);
		fab.setBackgroundTintList(ColorStateList.valueOf(accent));
	}

	@Override public FloatingActionButton getFab() {
		return fab;
	}

	@Override public ImageView getBackdrop() {
		return backdrop;
	}

	@Override public View getRootView() {
		return root;
	}
}