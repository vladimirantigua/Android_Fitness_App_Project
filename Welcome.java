package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
// SharedPreferences 
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    // GUI
    ImageButton mButtonSubmit;

    // Shared Preferences File
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize XML
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_welcome);

        // Set On Click Listener for Welcome Button
        mButtonSubmit = findViewById(R.id.buttonWelcome);
        mButtonSubmit.setOnClickListener(this);

        // Load Shared Preferences File
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        // Reset the App by uncommenting this
        // prefs.edit().putString("firstTime", "True").apply();
    }

    @Override
    public void onClick(View view) {

        if (prefs.getString("firstTime","").equals("False")) {
            startActivity(new Intent(Welcome.this, Modules.class));
        } else {
            startActivity(new Intent(Welcome.this, UserInfo.class));
        }
    }
}