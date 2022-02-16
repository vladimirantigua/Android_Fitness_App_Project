package com.example.fitnessapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private FitnessAppRepository weatherRepository;

    //private final LiveData<List<WeatherData>> allWeather;

    public WeatherViewModel (Application application){
        super(application);
        weatherRepository = new FitnessAppRepository(application);
        //allWeather = weatherRepository.getWeatherList();
    }

    //LiveData<List<WeatherData>> getAllWeather() {return allWeather;}

    public void insert(WeatherData weather) {weatherRepository.insertWeather(weather);}
}
