package com.example.fitnessapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StepCounterRepository {

    private StepCounterDao stepCounterDao;
    private LiveData<List<TableStepCounter>> allUserInfo;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    StepCounterRepository(Application application) {
        FitnessAppDatabase db = FitnessAppDatabase.getDatabase(application);
        stepCounterDao = db.stepCounterDao();
        allUserInfo = stepCounterDao.getTableUserInfo();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<TableStepCounter>> getAllUserInfo() {
        return allUserInfo;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(TableStepCounter tableStepCounter) {
        FitnessAppDatabase.databaseWriteExecutor.execute(() -> {
            stepCounterDao.insert(tableStepCounter);
        });
    }

    void update(TableStepCounter tableStepCounter) {
        FitnessAppDatabase.databaseWriteExecutor.execute(() -> {
            stepCounterDao.update(tableStepCounter);
        });
    }

    void delete(TableStepCounter tableStepCounter) {
        FitnessAppDatabase.databaseWriteExecutor.execute(() -> {
            stepCounterDao.delete(tableStepCounter);
        });
    }
}
