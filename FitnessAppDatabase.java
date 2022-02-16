package com.example.fitnessapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TableUserInfo.class, WeatherData.class, TableStepCounter.class}, version = 1, exportSchema = false)
public abstract class FitnessAppDatabase extends RoomDatabase {

    public abstract UserInfoDao userInfoDao();
    public abstract WeatherDao weatherDao();
    public abstract StepCounterDao stepCounterDao();

    private static volatile FitnessAppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static FitnessAppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FitnessAppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FitnessAppDatabase.class, "fitness_app_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }


    private static FitnessAppDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        // Todo: Removed NonNull so table can start at null
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // TODO: Comment out this block
            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.t
                // If you want to start with more words, just add them.
                UserInfoDao dao = INSTANCE.userInfoDao();
                //WeatherDao wDao = INSTANCE.weatherDao();
                dao.deleteAll();
                WeatherDao wdao = INSTANCE.weatherDao();
                dao.deleteAll();
                TableUserInfo tableUserInfo = new TableUserInfo(null, null, null, null, null, null, null, null);
                dao.insert(tableUserInfo);
                StepCounterDao sdao = INSTANCE.stepCounterDao();
                sdao.deleteAll();
//                TableStepCounter tableStepCounter = new TableStepCounter("0","");
//                sdao.insert(tableStepCounter);
            });
        }
    };
}
