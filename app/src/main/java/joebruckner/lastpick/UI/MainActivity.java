package joebruckner.lastpick.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.AnimRes;
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

	private Menu menu;

	@AnimRes int up = R.anim.slide_up;
	@AnimRes int down = R.anim.slide_down;

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
		this.menu = menu;
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/* CODE FOR FILTERING FEATURE. NOT FOR VERSION 1.0
		if (item.getItemId() == R.id.action_filter) {
			getSupportFragmentManager().beginTransaction()
					.setCustomAnimations(up, down, up, down)
					.add(R.id.content, new CategoriesFragment())
					.addToBackStack(null)
					.commit();
			appBarLayout.collapseToolbar(true);
			setTitle("Categories");
			item.setVisible(false);
			fab.setImageResource(R.drawable.ic_action_done);
			return true;
		} */ return super.onOptionsItemSelected(item);
	}

	@Override public void collapseAppBar() {
		appBarLayout.collapseToolbar(true);
	}

	@Override public void expandAppBar() {
		appBarLayout.expandToolbar(true);
	}

	@Override
	public void setTitle(@NonNull String title) {
		collapsingLayout.setTitle(title);
	}

	@Override public void clearTitle() {
		collapsingLayout.setTitle("");
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

	@Override public void onBackPressed() {
		super.onBackPressed();
		/* CODE FOR FILTERING FEATURE. NOT FOR VERSION 1.0
		appBarLayout.expandToolbar(true);
		menu.findItem(R.id.action_filter).setVisible(true);
		fab.setImageResource(R.drawable.ic_action_random); */
	}
}