package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

public class HikeActivity extends AppCompatActivity implements View.OnClickListener {

    Button submitLocation;
    EditText mEtSearchString;
    String mSearchString;
    String mGeo = "geo:";

    private FusedLocationProviderClient fusedLocationProviderClient;

    private double longitude;
    private double latitude;

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(HikeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(HikeActivity.this,
                                    Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );

                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();
                        } catch (Exception e) {

                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(HikeActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);
        submitLocation = findViewById(R.id.submitHikeButton);
        submitLocation.setOnClickListener(this);
        //get EditText
//        mEtSearchString = findViewById(R.id.searchHikes);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        updateLocation();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

//        mSearchString = mEtSearchString.getText().toString();
        System.out.println("lat:" + latitude);
        System.out.println("long:" + longitude);

//        "geo:34.2279, 118.3813?q="
        Uri searchUri = Uri.parse(mGeo + latitude + ", " + longitude + "?q=" + " Hikes");


        //Create the implicit intent
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);

        //If there's an activity associated with this intent, launch it
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);

        }


//        if (mSearchString.length() == 0) {
//            //Complain that there's no text
//            System.out.println("Location: " + mEtSearchString);
//            Toast.makeText(HikeActivity.this, "Please enter a valid location!", Toast.LENGTH_SHORT).show();
//        } else {
//
//
//            if(mSearchString.equalsIgnoreCase("Salt Lake City")){
//                mGeo = "geo:34.2279, 118.3813?q=";
//            }else{
//                mGeo = "San Francisco";
//            }
//
//
////            mGeo = "geo:40.767778,-111.845205?q=";
//            Uri searchUri = Uri.parse(mGeo + latitude + longitude + " Hikes in " + mSearchString);
//
//
//            //Create the implicit intent
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);
//
//            //If there's an activity associated with this intent, launch it
//            if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                startActivity(mapIntent);
//
//            }
//        }
    }
}