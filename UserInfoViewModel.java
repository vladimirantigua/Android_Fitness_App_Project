package com.example.fitnessapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserInfoViewModel extends AndroidViewModel {

    private FitnessAppRepository mRepository;

    private final LiveData<List<TableUserInfo>> allUserInfo;

    public UserInfoViewModel (Application application) {
        super(application);
        mRepository = new FitnessAppRepository(application);
        allUserInfo = mRepository.getAllUserInfo();
    }

    LiveData<List<TableUserInfo>> getUserInfo() { return allUserInfo; }

    public void insert(TableUserInfo tableUserInfo) { mRepository.insert(tableUserInfo); }

    public void update(TableUserInfo tableUserInfo) { mRepository.update(tableUserInfo);}

    public void delete(TableUserInfo tableUserInfo) {mRepository.delete(tableUserInfo);}
}