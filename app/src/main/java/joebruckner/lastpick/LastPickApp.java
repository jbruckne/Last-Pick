package joebruckner.lastpick;

import android.app.Application;
import android.util.Log;

public class LastPickApp extends Application {
	public static final String APP_NAME = "Last Pick";

	@Override public void onCreate() {
		super.onCreate();
		Log.d(APP_NAME, "App launched :)");
	}
}
