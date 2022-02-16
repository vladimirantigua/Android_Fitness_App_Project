package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StepCounter extends AppCompatActivity implements View.OnClickListener {

    private StepCounterViewModel stepCounterViewModel;

    Button updateDatabase;
    Button reset;

    int table_size = 0;

    TextView db_history;

    private TextView textView;
    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;

    private float y1,y2;
    static final int MIN_DISTANCE = 150;

    public void startStepCounter() {
        textView = findViewById(R.id.stepsTextView);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent!= null){
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];

                    double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;

                    if (MagnitudeDelta > 6){
                        stepCount++;
                    }
                    textView.setText(stepCount.toString());
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    y1 = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    y2 = event.getY();
                    float deltaY = y2 - y1;
                    if (deltaY > MIN_DISTANCE)
                    {
                        Toast.makeText(this, "Step Counter Reset", Toast.LENGTH_SHORT).show ();
                        stepCount = 0;
                    }
                    else if (deltaY < -MIN_DISTANCE)
                    {
                        Toast.makeText(this, "Step Counter Activated", Toast.LENGTH_SHORT).show ();
                        startStepCounter();
                    }
                    break;
            }
            return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_steps_button: {

                String date = new SimpleDateFormat("MM/dd/yy hh:mma").format(new Date());

                TableStepCounter newStepCounter = new TableStepCounter(table_size, Integer.toString(stepCount), date);
                stepCounterViewModel.insert(newStepCounter);
                table_size++;

                loadHistory();
                break;
            }
            case R.id.reset_steps_button: {

                stepCount = 0;
                break;
            }
        }
    }

    private void setOnClickListeners() {
        updateDatabase = findViewById(R.id.save_steps_button);
        updateDatabase.setOnClickListener(this);

        reset = findViewById(R.id.reset_steps_button);
        reset.setOnClickListener(this);
    }

    private void loadHistory() {

        stepCounterViewModel = new ViewModelProvider(this).get(StepCounterViewModel.class);
        stepCounterViewModel.getUserInfo().observe(this, allUserInfo -> {

            String history = "";

            table_size = allUserInfo.size();
            int max_entries = 10;
            if (table_size < max_entries) {
                max_entries = table_size;
            }

            for (int i = table_size - 1; i >= table_size - max_entries; i--) {
                history += allUserInfo.get(i).getDate() + " ";
                history += "Steps: " + allUserInfo.get(i).getSteps();
                history += "\n";
            }

            db_history = findViewById(R.id.stepHistoryView);
            db_history.setText(history);

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        loadHistory();
        setOnClickListeners();
    }

    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }

    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }

    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);
    }
}