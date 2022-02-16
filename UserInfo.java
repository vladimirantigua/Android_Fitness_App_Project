package com.example.fitnessapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
// SharedPreferences
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserInfo extends AppCompatActivity implements View.OnClickListener {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private UserInfoViewModel userInfoViewModel;

    Button submitUserInfo;
    Button takeProfilePic;
    Button reset;

    SharedPreferences prefs;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    EditText fname;
    EditText lname;
    EditText age;
    EditText city;
    EditText country;
    EditText height;
    EditText weight;
    Spinner sex;

    String sFirstName;
    String sLastName;
    String sAge;
    String sCity;
    String sCountry;
    String sHeight;
    String sWeight;
    String sSex;

    private void setOnClickListeners() {
        submitUserInfo = findViewById(R.id.buttonUpdateUserInfo);
        submitUserInfo.setOnClickListener(this);

        takeProfilePic = findViewById(R.id.TakeProfilePic);
        takeProfilePic.setOnClickListener(this);

        reset = findViewById(R.id.resetUserInfo);
        reset.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void changeButtonIfFirstTime() {
        String firstTime = prefs.getString("firstTime","True");

        if (firstTime.equals("True")) {
            submitUserInfo.setText("Submit");
            prefs.edit().putString("firstTime","False").apply();
        }
    }

    private void toastErrorMessage(String value) {

        Toast.makeText(UserInfo.this, "Please Enter a Valid " + value, Toast.LENGTH_LONG).show();
    }

    private boolean userInputIsErrorFree() {

        sFirstName = fname.getText().toString();
        sLastName = lname.getText().toString();
        sAge = age.getText().toString();
        sCity = city.getText().toString();
        sCountry = country.getText().toString();
        sHeight = height.getText().toString();
        sWeight = weight.getText().toString();
        sSex = String.valueOf(((Spinner)findViewById(R.id.spinnerSex)).getSelectedItem());

        // Checks For Input Errors and Tracks Empty Fields
        boolean errorFree = true;
        if (sFirstName.length() == 0 || sLastName.length() == 0) {
            toastErrorMessage("First & Last Name");
            errorFree = false;
        }
        if (!TextUtils.isDigitsOnly(sAge)) {
            toastErrorMessage("Age");
            errorFree = false;
        }
        if (!TextUtils.isDigitsOnly(sHeight)) {
            toastErrorMessage("Height");
            errorFree = false;
        }
        if (!TextUtils.isDigitsOnly(sWeight)) {
            toastErrorMessage("Weight");
            errorFree = false;
        }
        // Made it to the end so error free
        return errorFree;
    }

    private void updateDatabaseTable() {
        sFirstName = fname.getText().toString();
        sLastName = lname.getText().toString();
        sAge = age.getText().toString();
        sCity = city.getText().toString();
        sCountry = country.getText().toString();
        sHeight = height.getText().toString();
        sWeight = weight.getText().toString();
        sSex = String.valueOf(((Spinner)findViewById(R.id.spinnerSex)).getSelectedItem());


        TableUserInfo updatedUserInfo = new TableUserInfo( sFirstName, sLastName, sAge, sCity, sCountry, sHeight, sWeight, sSex);
        userInfoViewModel.update(updatedUserInfo);
    }



    // Todo: Remove initialization of database


    // Todo: Figure out first time button and see if first time used for anything else
    // Todo: Move as much of UserInfo to UserInfoViewModel as possible (just display on UserInfo)

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonUpdateUserInfo: {
                if (userInputIsErrorFree()) {
                    updateDatabaseTable();
                    Intent modules = new Intent(UserInfo.this, Modules.class);
                    startActivity(modules);
                }
                break;
            }

            case R.id.TakeProfilePic: {
                if (userInputIsErrorFree()) {
                    updateDatabaseTable();
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
                break;
            }
            case R.id.resetUserInfo: {
                fname.setText("");
                lname.setText("");
                age.setText("");
                city.setText("");
                country.setText("");
                height.setText("");
                weight.setText("");
                sex.setSelection(0);

                TableUserInfo updatedUserInfo = new TableUserInfo( "", "", "", "", "", "", "", "");
                userInfoViewModel.update(updatedUserInfo);
                prefs.edit().putString("picFilePath", "").apply();



                Intent modules = new Intent(UserInfo.this, UserInfo.class);
                startActivity(modules);
            }
        }
    }

    // Saves Profile pic to internal storage
    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/profile.jpg";
    }

    // Runs after image is captured
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap thumbnailImage = (Bitmap) extras.get("data");
            prefs.edit().putString("picFilePath", saveToInternalStorage(thumbnailImage)).apply();
        }
    }

    // @SuppressLint is used for setting the text to submit
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

        userInfoViewModel.getUserInfo().observe(this, allUserInfo -> {
            // Update the cached copy of the words in the adapter.

//            this.allUserInfo = allUserInfo;

            Spinner spinner = (Spinner) findViewById(R.id.spinnerSex);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.sexArray, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            if (allUserInfo.size() > 0) {

                // Get the db field values
                String db_fname = allUserInfo.get(0).getFname();
                String db_lname = allUserInfo.get(0).getLname();
                String db_age = allUserInfo.get(0).getAge();
                String db_city = allUserInfo.get(0).getCity();
                String db_country = allUserInfo.get(0).getCountry();
                String db_height = allUserInfo.get(0).getHeight();
                String db_weight = allUserInfo.get(0).getWeight();
                String db_sex = allUserInfo.get(0).getSex();

                // Display the db field values
                fname = findViewById(R.id.editTextFName);
                if (db_fname != null) {
                    fname.setText(db_fname);
                } else {
                    fname.setText("");
                }
                lname = findViewById(R.id.editTextLName);
                if (db_lname != null) {
                    lname.setText(db_lname);
                } else {
                    lname.setText("");
                }
                age = findViewById(R.id.editTextAge);
                if (db_age != null) {
                    age.setText(db_age);
                } else {
                    age.setText("");
                }
                city = findViewById(R.id.editTextCity);
                if (db_city != null) {
                    city.setText(db_city);
                } else {
                    city.setText("");
                }
                country = findViewById(R.id.editTextCountry);
                if (db_country != null) {
                    country.setText(db_country);
                } else {
                    country.setText("");
                }
                height = findViewById(R.id.editTextHeight);
                if (db_height != null) {
                    height.setText(db_height);
                } else {
                    height.setText("");
                }
                weight = findViewById(R.id.editTextWeight);
                if (db_weight != null) {
                    weight.setText(db_weight);
                } else {
                    weight.setText("");
                }
                sex = spinner;
                if (db_sex != null) {
                    if (allUserInfo.get(0).getSex().equals("Male")) {
                        sex.setSelection(0);
                    } else {
                        sex.setSelection(1);
                    }
                }

            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        setOnClickListeners(); //Todo: Edit so updates LiveData
//        changeButtonIfFirstTime(); //Todo: move to ViewModel


    }

}