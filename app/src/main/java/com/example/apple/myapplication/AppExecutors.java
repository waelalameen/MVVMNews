package com.example.apple.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors mInstance;
    private final Executor diskIO;
    private final Executor networkIO;

    private AppExecutors(Executor mainThread, Executor networkIO, Executor diskIO) {
        this.networkIO = networkIO;
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance() {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new AppExecutors(new MainThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        Executors.newSingleThreadExecutor());
            }
        }
        return mInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {

        /*
            What is Looper?

            Looper is a class which is used to execute the Messages(Runnables) in a queue. Normal
            threads have no such queue, e.g. simple thread does not have any queue. It executes once
            and after method execution finishes, the thread will not run another Message(Runnable).

            Where we can use Looper class?

            If someone wants to execute multiple messages(Runnables) then he should use the Looper
            class which is responsible for creating a queue in the thread. For example, while writing
            an application that downloads files from the internet, we can use Looper class to put files
            to be downloaded in the queue.

            How it works?

            There is prepare() method to prepare the Looper. Then you can use loop() method to create
            a message loop in the current thread and now your Looper is ready to execute the requests
            in the queue until you quit the loop.
        */

        private final Handler mThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mThreadHandler.post(runnable);
        }
    }
}
