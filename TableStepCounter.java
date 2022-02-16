package com.example.fitnessapp;

import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "step_counter")
public class TableStepCounter {
//
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "Steps")
    private String steps;

    @ColumnInfo(name = "Date")
    private String date;



    public TableStepCounter(int id, @NonNull String steps, String date) {
        this.id = id;
        this.steps = steps;
        this.date = date;
    }


    public int getId() { return id; }
    public String getSteps() {
        return steps;
    }
    public String getDate() {
        return date;
    }



    public void setId(int id) {this.id = id;  }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
