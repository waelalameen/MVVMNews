package com.example.apple.myapplication.network;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.apple.myapplication.AppExecutors;
import com.example.apple.myapplication.Singleton;
import com.example.apple.myapplication.db.news.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NetworkDataSource {
    private static final String LOG_TAG = NetworkDataSource.class.getSimpleName();
    private static final String TIME_OUT = "connect timed out";
    private static final String URL = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=";
    private static final Object LOCK = new Object();
    @SuppressLint("StaticFieldLeak")
    private static NetworkDataSource mInstance;
    private final AppExecutors executors;
    private final Context context;
    private MutableLiveData<News[]> mDownloadedNews;
    private static final String API_KEY = "050dde94fd9d42c481f5e6b6a0d42af6";

    private NetworkDataSource(Context context, AppExecutors executors) {
        this.context = context;
        this.executors = executors;
        mDownloadedNews = new MutableLiveData<>();
    }

    public static NetworkDataSource getInstance(Context context, AppExecutors executors) {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new NetworkDataSource(context, executors);
            }
        }
        return mInstance;
    }

    public MutableLiveData<News[]> getDownloadedNews() {
        return mDownloadedNews;
    }

    public void startFetchDataService() {
        Intent intent = new Intent(context, SyncIntentService.class);
        context.startService(intent);
    }

    public void fetchNews() {
        executors.getNetworkIO().execute(() -> {
            // ApiClient client = WebService.getWebService().create(ApiClient.class);
            // Make api call
            // Call<News> call = client.getNews(API_KEY);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    URL + API_KEY,
                    null,
                    response -> {

                        try {
                            String articles = response.getString("articles");

                            // Log.d(LOG_TAG, articles);

                            JSONArray jsonArray = new JSONArray(articles);

                            List<News> newsList = new ArrayList<>();

                            News news;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String source = jsonObject.getString("source");
                                JSONObject sourceObject = new JSONObject(source);

                                news = new News(
                                        i,
                                        sourceObject.getString("name"),
                                        jsonObject.getString("author"),
                                        jsonObject.getString("title"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("url"),
                                        jsonObject.getString("urlToImage"),
                                        jsonObject.getString("publishedAt")
                                );

                                newsList.add(news);
                            }

                            News[] newsArr = newsList.toArray(new News[newsList.size()]);
                            mDownloadedNews.postValue(newsArr);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e(LOG_TAG, error.getMessage());
                    }
            )
            {

            };

            Objects.requireNonNull(Singleton.Companion.getInstance()).addToRequestQueue(request);

//            call.enqueue(new Callback<News[]>() {
//                @Override
//                public void onResponse(@NonNull Call<News[]> call, @NonNull Response<News[]> response) {
//                    mDownloadedNews.postValue(response.body());
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<News[]> call, @NonNull Throwable t) {
//                    Log.e(LOG_TAG, t.getMessage());
//                }
//            });
        });
    }
}
