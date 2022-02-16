package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class Weather extends AppCompatActivity
{

    // Global variables:
    TextView tempTextView;
    TextView dateTextView;
    TextView weatherTextView;
    TextView cityTextView;
    ImageView weatherImageView;
    public static final int NEW_WEATHER_ACTIVITY_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private WeatherViewModel weatherViewModel;
    private double longitude;
    private double latitude;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("on rESULT");
        grabWeather();
        switch (requestCode) {
            case 0:
                System.out.println("CASE 0");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    System.out.println("WORKING");

                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    @SuppressLint("MissingPermission")
    public void grabWeather(){
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                System.out.println("OnComplete");
                Location location = task.getResult();
                System.out.println("Location is: "+location);

                try {
                    Geocoder geocoder = new Geocoder(Weather.this,
                            Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1
                    );

                    String cityName = addresses.get(0).getLocality();
                    System.out.println(cityName);
                    latitude = addresses.get(0).getLatitude();
                    System.out.println("latitude:" + latitude);
                    longitude = addresses.get(0).getLongitude();
                    System.out.println("longitude:" + longitude);
                    String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+latitude+"&lon="+longitude+"&exclude=hourly,daily&appid=1b09ede48acfeddbb7aa1ad3daa80060&units=imperial";
                    System.out.println("real url:" + url);
                    System.out.println("CITY: " + cityName);
                    tempTextView = (TextView) findViewById(R.id.tempTextView);
                    dateTextView = (TextView) findViewById(R.id.dateTextView);
                    weatherTextView = (TextView) findViewById(R.id.weathertextView);
                    cityTextView = (TextView) findViewById(R.id.cityTextView);
                    cityTextView.setText(cityName);
                    weatherImageView = (ImageView) findViewById(R.id.weatherImageView);

                    dateTextView.setText(getCurrentDate());
                    //System.out.println("Latitude2: " + latut)
                    //String url = "https://api.openweathermap.org/data/2.5/onecall?lat=37.422065599999996&lon=-122.08408969999998&appid=1b09ede48acfeddbb7aa1ad3daa80060";
                    //String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+latitude+"&lon="+longitude+"&appid=1b09ede48acfeddbb7aa1ad3daa80060";
                    //"https://api.openweathermap.org/data/2.5/weather?q=Salt Lake City,Utah&appid=1b09ede48acfeddbb7aa1ad3daa80060&units=imperial";
                    //String url = "https://api.openweathermap.org/data/2.5/weather?q=Salt Lake City,Utah&appid=1b09ede48acfeddbb7aa1ad3daa80060&units=imperial";
                    Log.d("url", url);
                    // making a standard request = https://developer.android.com/training/volley/request
                    //https://developer.android.com/training/volley
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject responseObject) {
                                    System.out.println("onResponse");
                                    //tempTextView.setText("Response: " + response.toString());
                                    // Log.v will allow you to view and set the text https://developer.android.com/reference/android/util/Log#v(java.lang.String,%20java.lang.String)
                                    Log.v("WEATHER", "Response: " + responseObject.toString());

                                    try
                                    {
                                        System.out.println(responseObject.toString());
                                        JSONObject firstJSONObject = responseObject.getJSONObject("current");
                                        JSONArray weatherArray = firstJSONObject.getJSONArray("weather");
                                        JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

                                        String temp = Integer.toString((int) Math.round(firstJSONObject.getDouble("temp")));
                                        String weatherCondition = firstWeatherObject.getString("description");
                                        // GET day and night ICONS:
                                        String icons_Name = firstWeatherObject.getString("icon");
                                        //String city = responseObject.getString("name");
                                        WeatherData weather = new WeatherData(temp, weatherCondition, icons_Name, cityName);
                                        weatherViewModel.insert(weather);
                                        System.out.println("TEMP: " + temp);
                                        tempTextView.setText(temp);
                                        weatherTextView.setText(weatherCondition);

                                        // To get the icon name from the Open Weather Map Site:  https://openweathermap.org/weather-conditions#Icon-list
                                        // To get the resource class see Android documentation: https://developer.android.com/reference/android/content/res/Resources
                                        // To get the icon ID:
                                        // https://developer.android.com/reference/android/content/res/Resources#getIdentifier(java.lang.String,%20java.lang.String,%20java.lang.String)
                                        int icon_Resource_Id = getResources().getIdentifier(icons_Name, "drawable", getPackageName());
                                        //public void setImageResource (int resId)   https://developer.android.com/reference/android/widget/ImageView
                                        weatherImageView.setImageResource(icon_Resource_Id);
                                        //add the night icon I do not need to use the method below:
//                            int icon_Resource_Id = getResources().getIdentifier("icon_" + weatherCondition.replace(" ", ""), "drawable", getPackageName());
//                            //public void setImageResource (int resId)   https://developer.android.com/reference/android/widget/ImageView
//                            weatherImageView.setImageResource(icon_Resource_Id);

                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("ERROR: " + error);
                                }
                            });

                    // Access the RequestQueue through your singleton class.

                    RequestQueue queue = Volley.newRequestQueue(Weather.this);
                    System.out.println("OBJECT"+ jsObjRequest.toString());
                    queue.add(jsObjRequest);
                    System.out.println("ADDED TO QUEUE");
                } catch (Exception e) {
                    System.out.println("EXCEPTION");
                }

            }
        });

    }

    private void updateLocation() {
        System.out.println("updateLocation");
        if (ActivityCompat.checkSelfPermission(Weather.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission ok");
            grabWeather();
        } else {

            System.out.println("Asking permission");
            ActivityCompat.requestPermissions(Weather.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);

            //finish();
            //startActivity(getIntent());
        }
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        System.out.println("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        updateLocation();
        System.out.println("onCreateDone");

    }

    private String getCurrentDate ()
    {
        System.out.println("getCurrentDate");
        //to get the calendar: https://developer.android.com/reference/java/util/Calendar
        //getTime() method: https://developer.android.com/reference/java/util/Calendar#getTime()
        // SimpleDateFormat: https://developer.android.com/reference/java/text/SimpleDateFormat?hl=en
        Calendar calendar = Calendar.getInstance();
        // SimpleDateFormat and pass in the constructor the following format: day, month, day "EEE, MMM d, ''yy" Wed, Jul 4, '01
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        String formatted_Date = dateFormat.format(calendar.getTime());

        return formatted_Date;
    }
}