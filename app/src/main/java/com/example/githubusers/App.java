package com.example.githubusers;

import android.app.Application;

import com.example.githubusers.users.github.GitHubService;
import com.readystatesoftware.chuck.ChuckInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

  GitHubService gitHubService;

  @Override
  public void onCreate() {
    super.onCreate();
    createNetworkServices();
  }

  public GitHubService getGitHubService() {
    return gitHubService;
  }


  private void createNetworkServices() {
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    ChuckInterceptor chuckInterceptor = new ChuckInterceptor(this);
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
        .addNetworkInterceptor(chuckInterceptor)
        .build();

    Retrofit githubRetrofit = new Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
    gitHubService = githubRetrofit.create(GitHubService.class);
  }
}
