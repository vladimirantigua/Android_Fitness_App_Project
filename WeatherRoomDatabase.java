package com.example.fitnessapp;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {WeatherData.class}, version = 1, exportSchema = false)
public abstract class WeatherRoomDatabase extends RoomDatabase {

    public abstract WeatherDao weatherDao();

    private static volatile WeatherRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WeatherRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WeatherRoomDatabase.class, "weather_database").build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback WeatherRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                WeatherDao dao = INSTANCE.weatherDao();
                dao.deleteAll();

                //WeatherData weatherData = new WeatherData("");
                //dao.insert(weatherData);

            });
        }
    };
}
