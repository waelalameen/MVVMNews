package com.example.apple.myapplication.network;

import com.example.apple.myapplication.db.news.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("v2/top-headlines?country=us&category=business")
    Call<News> getNews(@Query("apiKey") String apiKey);
}
