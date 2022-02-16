package com.example.fitnessapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StepCounterViewModel extends AndroidViewModel {

    private StepCounterRepository mRepository;

    private final LiveData<List<TableStepCounter>> allUserInfo;

    public StepCounterViewModel (Application application) {
        super(application);
        mRepository = new StepCounterRepository(application);
        allUserInfo = mRepository.getAllUserInfo();
    }

    LiveData<List<TableStepCounter>> getUserInfo() { return allUserInfo; }

    public void insert(TableStepCounter tableStepCounter) { mRepository.insert(tableStepCounter); }

    public void update(TableStepCounter tableStepCounter) { mRepository.update(tableStepCounter);}

    public void delete(TableStepCounter tableStepCounter) {mRepository.delete(tableStepCounter);}
}