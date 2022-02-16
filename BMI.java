package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.os.Bundle;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.math.*;

public class BMI extends AppCompatActivity{

    private EditText weight;
    private TextView result;

    private UserInfoViewModel userInfoViewModel;

    String db_height;
    String db_weight;

    private void displayWeight() {
        weight = (EditText) findViewById(R.id.editTextWeight);
        weight.setText(db_weight);
        result = (TextView) findViewById(R.id.bmiResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        userInfoViewModel.getUserInfo().observe(this, allUserInfo -> {

            // Get the db field values and set global variables
            db_height = allUserInfo.get(0).getHeight();
            db_weight = allUserInfo.get(0).getWeight();
            displayWeight();
        });
    }

    public void BMICalc(View view) {

        // Uses whatever weight they enter in plut the height from the db
        String weightString = weight.getText().toString();
        String heightString = db_height;

        if (heightString != null && !"".equals(heightString)
                && weightString != null  &&  !"".equals(weightString)) {
            float heightValue = Float.parseFloat(heightString);
            float weightValue = Float.parseFloat(weightString);

            float bmi = (weightValue / (heightValue * heightValue)) * 703;
            show_bmi(bmi);
        }
    }

    private void show_bmi(float bmi) {
        String label_BMI = "";

        if (Float.compare(bmi, 15f) <= 0) {
            label_BMI = "You Are About to Die";//getString(R.string.verySeverelyUnderweight);
        } else if (Float.compare(bmi, 15f) > 0  &&  Float.compare(bmi, 16f) <= 0) {
            label_BMI = "Approaching Death";//getString(R.string.severelyUnderweight);
        } else if (Float.compare(bmi, 16f) > 0  &&  Float.compare(bmi, 18.5f) <= 0) {
            label_BMI = "Underweight"; //getString(R.string.underweight);
        } else if (Float.compare(bmi, 18.5f) > 0  &&  Float.compare(bmi, 25f) <= 0) {
            label_BMI = "Healthy Weight";//getString(R.string.healthyNormalWeight);
        } else if (Float.compare(bmi, 25f) > 0  &&  Float.compare(bmi, 30f) <= 0) {
            label_BMI = "Overweight";//getString(R.string.overweight);
        } else if (Float.compare(bmi, 30f) > 0  &&  Float.compare(bmi, 35f) <= 0) {
            label_BMI = "Obesity Class I";//getString(R.string.obesityClass_I);
        } else if (Float.compare(bmi, 35f) > 0  &&  Float.compare(bmi, 40f) <= 0) {
            label_BMI = "Obesity Class II";//getString(R.string.obesityClass_II);
        } else {
            label_BMI = "Obesity Class III";//getString(R.string.obesityClass_III);
        }

        DecimalFormat df = new DecimalFormat("#.#");

        label_BMI = "Result: " + df.format(bmi) + " - " + label_BMI;
        result.setText(label_BMI);
    }
}

//  Reference:
//  https://www.ncbi.nlm.nih.gov/books/NBK541070/