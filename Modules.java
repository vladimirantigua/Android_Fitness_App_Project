package com.example.fitnessapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class Modules extends AppCompatActivity implements View.OnClickListener {

    ImageButton mButtonBMI;
    ImageButton mButtonProfile;
    ImageButton mButtonFitnessGoal;
    ImageView profilePic;
    ImageButton mButtonMap;
    ImageButton mButtonWeather;
    ImageButton mButtonStepCounter;
    // SharedPreferences

    String db_fname;
    String db_lname;
    String db_age;
    String db_city;
    String db_country;
    String db_height;
    String db_weight;
    String db_sex;

    SharedPreferences prefs;


    private UserInfoViewModel userInfoViewModel;


    private boolean missingField(String field) {
        return prefs.getString(field, "").equals("True");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //User Info intent button to enter User Info interface
            case R.id.imageButtonProfile: {
                Intent profile = new Intent(Modules.this, UserInfo.class);
                startActivity(profile);
                break;
            }
            //User Info intent button to enter User Info interface
            case R.id.profilePic: {
                Intent profile = new Intent(Modules.this, UserInfo.class);
                startActivity(profile);
                break;
            }
            // BMI intent button to enter BMI interface
            case R.id.imageButtonBMI: {
                // Required: Height & Weight
                if (db_height.length() == 0 || db_weight.length() == 0) {
                    Toast.makeText(Modules.this, "Profile must have height and weight.", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(Modules.this, BMI.class));
                }
                break;
            }
            case R.id.imageButtonMap:
                startActivity(new Intent(Modules.this, HikeActivity.class ));
                break;

            // BMR intent button to enter BMR interface:
            case R.id.imageButtonFitnessGoal: {
                // Required: Age, Height, Weight
                if (db_height.length() == 0 || db_weight.length() == 0 || db_age.length() == 0) {
                    Toast.makeText(Modules.this, "Profile must have height, weight, and age.", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(Modules.this, FitnessGoal.class));
                }
                break;
            }

            //User Info intent button to display the WEATHER:
            case R.id.imageButtonWeather: {
                Intent weather = new Intent(Modules.this, Weather.class);
                startActivity(weather);
                System.out.println("Starting weather");
                break;
            }

            case R.id.imageButtonStepCounter: {
                Intent stepCounter = new Intent(Modules.this, StepCounter.class);
                startActivity(stepCounter);
                break;
            }

        }
    }
    protected void setProfilePic (){
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String picFilePath = prefs.getString("picFilePath","");
            Bitmap bitmap = BitmapFactory.decodeFile(picFilePath);
            profilePic = (ImageView) findViewById(R.id.profilePic);
            profilePic.setImageBitmap(bitmap);
        }
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_modules);

            userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

            userInfoViewModel.getUserInfo().observe(this, allUserInfo -> {

                // Get the db field values and set global variables
                db_fname = allUserInfo.get(0).getFname();
                db_lname = allUserInfo.get(0).getLname();
                db_age = allUserInfo.get(0).getAge();
                db_city = allUserInfo.get(0).getCity();
                db_country = allUserInfo.get(0).getCountry();
                db_height = allUserInfo.get(0).getHeight();
                db_weight = allUserInfo.get(0).getWeight();
                db_sex = allUserInfo.get(0).getSex();

            });

            mButtonBMI = findViewById(R.id.imageButtonBMI);
            mButtonBMI.setOnClickListener(this);
            mButtonProfile = findViewById(R.id.imageButtonProfile);
            mButtonProfile.setOnClickListener(this);

            mButtonMap = findViewById(R.id.imageButtonMap);
            mButtonMap.setOnClickListener(this);

            mButtonFitnessGoal = findViewById(R.id.imageButtonFitnessGoal);
            mButtonFitnessGoal.setOnClickListener(this);

            mButtonWeather = findViewById(R.id.imageButtonWeather);
            mButtonWeather.setOnClickListener(this);

            mButtonStepCounter = findViewById(R.id.imageButtonStepCounter);
            mButtonStepCounter.setOnClickListener(this);

            setProfilePic();
        }
    }