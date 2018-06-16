package com.example.apple.myapplication.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.apple.myapplication.network.NetworkDataSource;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobNewsService extends JobService {
    private static final String LOG_TAG = JobNewsService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(LOG_TAG, "Job started");
        NetworkDataSource dataSource = InjectorUtils.provideNetworkDataSource(getApplicationContext());
        dataSource.startFetchDataService();

        // Finish Job
        jobFinished(jobParameters, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(LOG_TAG, "Job finished");
        return true;
    }
}
