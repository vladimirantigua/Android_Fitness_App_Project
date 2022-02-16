package com.example.fitnessapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class FitnessAppRepository {

    private UserInfoDao userInfoDao;
    private LiveData<List<TableUserInfo>> allUserInfo;
    private WeatherDao weatherDao;
    private LiveData<List<WeatherData>> weatherList;
    Application userInfoApplication;
    Application weatherApplication;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    FitnessAppRepository(Application application) {
        FitnessAppDatabase db = FitnessAppDatabase.getDatabase(application);
        userInfoDao = db.userInfoDao();
        allUserInfo = userInfoDao.getTableUserInfo();
        weatherDao = db.weatherDao();
        weatherList = weatherDao.getWeatherList();
        userInfoApplication = application;
        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(application.getApplicationContext());

            Log.i("FitnessAppRepository", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("FitnessAppRepository", "Could not initialize Amplify", error);
        }
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<TableUserInfo>> getAllUserInfo() {
        return allUserInfo;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(TableUserInfo tableUserInfo) {
        FitnessAppDatabase.databaseWriteExecutor.execute(() -> {
            userInfoDao.insert(tableUserInfo);
            uploadFile(userInfoApplication);
        });
    }

    void insertWeather(WeatherData weather) {
        WeatherRoomDatabase.databaseWriteExecutor.execute(() -> {
            weatherDao.insert(weather);
            uploadFile(weatherApplication);
        });
    }

    void update(TableUserInfo tableUserInfo) {
        FitnessAppDatabase.databaseWriteExecutor.execute(() -> {
            userInfoDao.update(tableUserInfo);
            uploadFile(userInfoApplication);
        });
    }

    void delete(TableUserInfo tableUserInfo) {
        FitnessAppDatabase.databaseWriteExecutor.execute(() -> {
            userInfoDao.delete(tableUserInfo);
        });
    }


//    public void initAmplify(Bundle savedInstanceState) {
//
//        try {
//            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
//            Amplify.addPlugin(new AWSCognitoAuthPlugin());
//            Amplify.addPlugin(new AWSS3StoragePlugin());
//            Amplify.configure(context);
//
//            Log.i("FitnessAppRepository", "Initialized Amplify");
//        } catch (AmplifyException error) {
//            Log.e("FitnessAppRepository", "Could not initialize Amplify", error);
//        }
//
//    }

    private void uploadFile(Application application) {
//        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");
//
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
//            writer.append();
//            writer.close();
//        } catch (Exception exception) {
//            Log.e("MyAmplifyApp", "Upload failed", exception);
//        }

        //File dbFile = new File("/data/data/com.example.fitnessapp/databases/fitness_app_database");
        System.out.println("FINDING FILE");
        String filename0 = String.valueOf(application.getApplicationContext().getDatabasePath("fitness_app_database"));
        String filename1 = String.valueOf(application.getApplicationContext().getDatabasePath("fitness_app_database-shm"));
        String filename2 = String.valueOf(application.getApplicationContext().getDatabasePath("fitness_app_database-wal"));
        File dbFile0 = new File(filename0);
        File dbFile1 = new File(filename1);
        File dbFile2 = new File(filename2);
        //"/data/data/com.example.fitnessapp/databases/fitness_app_database");
        System.out.println("FILENAME TEST: " + filename0);//(filename);
        Amplify.Storage.uploadFile(
                "dbFile0",
                dbFile0,
                result -> Log.i("FitnessAppRepository", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("FitnessAppRepository", "Upload failed", storageFailure)
        );
        Amplify.Storage.uploadFile(
                "dbFile1",
                dbFile1,
                result -> Log.i("FitnessAppRepository", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("FitnessAppRepository", "Upload failed", storageFailure)
        );Amplify.Storage.uploadFile(
                "dbFile2",
                dbFile2,
                result -> Log.i("FitnessAppRepository", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("FitnessAppRepository", "Upload failed", storageFailure)
        );

    }

//    public class MyAmplifyApp extends Application {
//        @Override
//        public void onCreate() {
//            super.onCreate();
//
//            try {
//                // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
//                Amplify.addPlugin(new AWSCognitoAuthPlugin());
//                Amplify.addPlugin(new AWSS3StoragePlugin());
//                Amplify.configure(getApplicationContext());
//
//                Log.i("MyAmplifyApp", "Initialized Amplify");
//            } catch (AmplifyException error) {
//                Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
//            }
//        }
//    }





}
