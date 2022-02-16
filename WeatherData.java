package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_table")
public class WeatherData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "city")
    private String city;

    @NonNull
    @ColumnInfo(name = "temp")
    private String temp;

    @NonNull
    @ColumnInfo(name = "weather")
    private String weatherCondition;

    @NonNull
    @ColumnInfo(name = "icon")
    private String icon;



    public WeatherData(@NonNull String temp, @NonNull String weatherCondition, @NonNull String icon, @NonNull String city){
        this.temp = temp;
        this.weatherCondition = weatherCondition;
        this.icon = icon;
        this.city = city;
    }

    public String getTemp(){return this.temp; }
    public String getWeatherCondition(){return this.weatherCondition;}
    public String getIcon(){return this.icon;}
    public String getCity(){return this.city;}

}
