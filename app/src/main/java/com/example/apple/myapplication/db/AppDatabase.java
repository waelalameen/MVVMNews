package com.example.apple.myapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.apple.myapplication.db.news.News;
import com.example.apple.myapplication.db.news.NewsDao;

@Database(entities = {News.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE = "news_db";
    private static final Object LOCK = new Object();
    private static volatile AppDatabase mInstance;
    public abstract NewsDao newsDao();

    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE)
                        .build();
            }
        }
        return mInstance;
    }
}
