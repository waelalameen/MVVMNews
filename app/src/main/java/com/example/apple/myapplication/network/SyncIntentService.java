package com.example.apple.myapplication.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.apple.myapplication.utils.InjectorUtils;

public class SyncIntentService extends IntentService {

    private static final String LOG_TAG = SyncIntentService.class.getSimpleName();

    public SyncIntentService() {
        super("Handling intent services");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "Service is started");
        NetworkDataSource dataSource = InjectorUtils.provideNetworkDataSource(getApplicationContext());
        dataSource.fetchNews();
    }
}
