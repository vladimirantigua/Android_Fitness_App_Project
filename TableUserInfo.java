package com.example.fitnessapp;

import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


//@Entity(primaryKeys = {"column1","column2"}, tableName = "user_info")
@Entity(tableName = "user_info")
public class TableUserInfo {
//

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "First Name")
    private String fname;

    @ColumnInfo(name = "Last Name")
    private String lname;

    @ColumnInfo(name = "Age")
    private String age;

    @ColumnInfo(name = "City")
    private String city;

    @ColumnInfo(name = "Country")
    private String country;

    @ColumnInfo(name = "Height")
    private String height;

    @ColumnInfo(name = "Weight")
    private String weight;

    @ColumnInfo(name = "Sex")
    private String sex;

    public TableUserInfo(@NonNull String fname, @NonNull String lname, String age, String city, String country, String height, String weight, String sex) {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.city = city;
        this.country = country;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
    }


    public int getId() { return id; }

    public String getFname() {
        return fname;
    }
    public String getLname() {
        return lname;
    }
    public String getAge() {
        return age;
    }
    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
    public String getHeight() {
        return height;
    }
    public String getWeight() {
        return weight;
    }
    public String getSex() {
        return sex;
    }



    public void setId(int id) {this.id = id;  }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
