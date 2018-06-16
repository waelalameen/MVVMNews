package com.example.apple.myapplication.newsFragments;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.apple.myapplication.NewsRepository;

public class NewsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private NewsRepository mRepository;

    public NewsViewModelFactory(NewsRepository mRepository) {
        this.mRepository = mRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new NewsViewModel(mRepository);
    }
}
