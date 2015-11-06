package joebruckner.lastpick.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

public interface Coordinator {
    void setTitle(@NonNull String title);

    void setThemeColors(int primary, int primaryDark, int accent);

    FloatingActionButton getFab();

    ImageView getBackdrop();

    View getRootView();
}
