package joebruckner.lastpick.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

public interface Coordinator {
	void collapseAppBar();
	void expandAppBar();
	void setTitle(@NonNull String title);
	void clearTitle();
	FloatingActionButton getFab();
	ImageView getBackdrop();
	View getRootView();
}
