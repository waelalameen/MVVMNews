package com.example.apple.myapplication.utils;

import android.content.Context;

import com.example.apple.myapplication.AppExecutors;
import com.example.apple.myapplication.NewsRepository;
import com.example.apple.myapplication.db.AppDatabase;
import com.example.apple.myapplication.network.NetworkDataSource;
import com.example.apple.myapplication.newsFragments.NewsViewModelFactory;

public class InjectorUtils {

    public static NewsRepository provideRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        NetworkDataSource networkDataSource =
                NetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return NewsRepository.getInstance(database.newsDao(), networkDataSource, executors);
    }

    public static NetworkDataSource provideNetworkDataSource(Context context) {
        provideRepository(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return NetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static NewsViewModelFactory NewsFragmentProvider(Context context) {
        NewsRepository repository = provideRepository(context);
        return new NewsViewModelFactory(repository);
    }
}
