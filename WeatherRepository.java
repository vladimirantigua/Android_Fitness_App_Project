package com.example.fitnessapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WeatherRepository {

    private WeatherDao weatherDao;
    private LiveData<List<WeatherData>> weatherList;

    WeatherRepository(Application application) {
        FitnessAppDatabase db = FitnessAppDatabase.getDatabase(application);
        weatherDao = db.weatherDao();
        weatherList = weatherDao.getWeatherList();
    }

    //LiveData<List<WeatherData>> getWeatherList() {
     //   return getWeatherList();
    //}

    void insertWeather(WeatherData weather) {
        WeatherRoomDatabase.databaseWriteExecutor.execute(() -> {
            weatherDao.insert(weather);
        });
    }

}
