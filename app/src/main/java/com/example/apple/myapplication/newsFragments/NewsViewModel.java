package com.example.apple.myapplication.newsFragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.apple.myapplication.NewsRepository;
import com.example.apple.myapplication.db.news.News;

import java.util.List;

public class NewsViewModel extends ViewModel {

    private LiveData<List<News>> mNews;

    NewsViewModel(NewsRepository mRepository) {
        mNews = mRepository.getCurrentNews();
    }

    public LiveData<List<News>> getNews() {
        return mNews;
    }
}
