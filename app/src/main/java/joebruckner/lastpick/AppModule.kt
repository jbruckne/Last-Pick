package joebruckner.lastpick

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import joebruckner.lastpick.model.guidebox.GuideboxMovie
import joebruckner.lastpick.source.DatabaseHelper
import joebruckner.lastpick.source.GuideboxService
import joebruckner.lastpick.source.TmdbService
import joebruckner.lastpick.utils.SourceInfoDeserializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(val mainApp: MainApp) {

    @Provides
    @Singleton
    fun provideMainApp(): MainApp {
        return mainApp
    }

    @Provides
    @Singleton
    @Named("Application")
    fun provideContext(): Context {
        return mainApp
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(mainApplication: MainApp): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(mainApplication)
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(): ConnectivityManager {
        return mainApp.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideAnalytics(): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(mainApp)
    }

    @Provides
    @Singleton
    fun provideDatabaseHelper(@Named("Application") context: Context): DatabaseHelper {
        return DatabaseHelper(context)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(GuideboxMovie::class.java, SourceInfoDeserializer())
                .create()
    }

    @Provides
    @Singleton
    fun provideOkhttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideTheMovieDb(client: OkHttpClient): TmdbService {
        return Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(TmdbService::class.java)
    }

    @Provides
    @Singleton
    fun provideGuidebox(gson: Gson, client: OkHttpClient): GuideboxService {
        return Retrofit.Builder()
                .baseUrl("https://api-public.guidebox.com/v1.43/us/${MainApp.GUIDEBOX_API_KEY}/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(GuideboxService::class.java)
    }
}