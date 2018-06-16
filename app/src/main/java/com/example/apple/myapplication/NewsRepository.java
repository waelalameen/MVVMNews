package com.example.apple.myapplication;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.apple.myapplication.db.news.News;
import com.example.apple.myapplication.db.news.NewsDao;
import com.example.apple.myapplication.network.NetworkDataSource;

import java.util.List;

public class NewsRepository {

    private static final String LOG_TAG = NewsRepository.class.getSimpleName();

    // For singleton instantiation
    private static final Object LOCK = new Object();
    private static NewsRepository mInstance;
    private final NewsDao newsDao;
    private final NetworkDataSource dataSource;
    private final AppExecutors mExecutors;
    private boolean isInitiated = false;

    private NewsRepository(NewsDao newsDao, NetworkDataSource dataSource, AppExecutors mExecutors) {
        this.newsDao = newsDao;
        this.dataSource = dataSource;
        this.mExecutors = mExecutors;
    }

    public synchronized static NewsRepository getInstance(NewsDao newsDao, NetworkDataSource dataSource, AppExecutors mExecutors) {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new NewsRepository(newsDao, dataSource,
                        mExecutors);
            }
        }
        return mInstance;
    }

    private NewsDao getNewsDao() {
        return newsDao;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (isInitiated) return;
        isInitiated = true;

        // TODO Finish this method when instructed
        mExecutors.getDiskIO().execute(this::startFetchWeatherService);
    }

    /**
     * Deletes old weather data because we don't need to keep multiple days' data
     */
    private void deleteOldData() {
//        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
//
//        AsyncTask.execute(() -> weatherDao.deleteOldData(today));
    }

    /**
     * Checks if there are enough days of future weather for the app to display all the needed data.
     *
     * @return Whether a fetch is needed
     */
//    private boolean isFetchNeeded() {
//        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
//        int count = weatherDao.CountAllFutureData(today);
//        return (count < WeatherNetworkDataSource.NUM_DAYS);
//    }

    /**
     * Network related operation
     */

    public LiveData<List<News>> getCurrentNews() {
        initializeData();
        return newsDao.getNews();
    }

    private void startFetchWeatherService() {
        // TODO Finish this method when instructed
        LiveData<News[]> networkData = dataSource
                .getDownloadedNews();

        networkData.observeForever(news -> {
            deleteOldData();
            mExecutors.getDiskIO().execute(() -> {
                getNewsDao().insertAll(news);
                Log.i(LOG_TAG, "data inserted");
            });
        });
    }
}
