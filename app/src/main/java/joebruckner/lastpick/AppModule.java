package joebruckner.lastpick;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import joebruckner.lastpick.model.guidebox.GuideboxMovie;
import joebruckner.lastpick.source.DatabaseHelper;
import joebruckner.lastpick.source.GuideboxService;
import joebruckner.lastpick.source.TmdbService;
import joebruckner.lastpick.source.collection.CollectionManager;
import joebruckner.lastpick.source.collection.CollectionRepository;
import joebruckner.lastpick.source.collection.LocalCollectionSource;
import joebruckner.lastpick.utils.SourceInfoDeserializer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class AppModule {
    private MainApp mainApp;

    public AppModule(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Provides
    @Singleton
    MainApp provideMainApp() {
        return mainApp;
    }

    @Provides
    @Singleton
    @Named("Application")
    Context provideContext() {
        return mainApp;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(MainApp mainApplication){
        return PreferenceManager.getDefaultSharedPreferences(mainApplication);
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) mainApp.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    FirebaseAnalytics provideAnalytics() {
        return FirebaseAnalytics.getInstance(mainApp);
    }

    @Provides
    @Singleton
    DatabaseHelper provideDatabaseHelper(@Named("Application") Context context) {
        return new DatabaseHelper(context);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(GuideboxMovie.class, new SourceInfoDeserializer())
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    TmdbService provideTheMovieDb(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(TmdbService.class);
    }

    @Provides
    @Singleton
    GuideboxService provideGuidebox(Gson gson, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://api-public.guidebox.com/v1.43/us/${MainApp.GUIDEBOX_API_KEY}/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(GuideboxService.class);
    }

    @Provides
    @Singleton
    CollectionRepository provideCollections(LocalCollectionSource repo) {
        return repo;
    }

    @Provides
    @Singleton
    CollectionManager provideCollectionManager(CollectionRepository repo) {
        return new CollectionManager(repo, 120);
    }
}