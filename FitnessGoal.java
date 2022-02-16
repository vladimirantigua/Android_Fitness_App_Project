package com.example.fitnessapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FitnessGoal extends AppCompatActivity implements View.OnClickListener {

    private UserInfoViewModel userInfoViewModel;

    String db_age;
    String db_height;
    String db_weight;
    String db_sex;

    // Preference File
    SharedPreferences prefs;

    // Global Variables
    Boolean isGainWeight;
    Boolean isSedentary;

    // GUI
    ToggleButton isSedentaryButton;
    ToggleButton isGainWeightButton;
    EditText pounds;
    TextView goal;
    Button createGoal;

    private void loadStoredValues() {

        // Sets TextEdit for Pounds to Loose/Gain
        pounds = findViewById(R.id.editTextPoundsLooseGain);
        pounds.setText(prefs.getString("poundsLooseGain", ""));

        // Set the Goal Output Text
        goal = findViewById(R.id.planResult);
        goal.setText(prefs.getString("fitnessGoal", ""));

        // Sets the First Toggle Button
        isGainWeightButton = findViewById(R.id.isGainWeight);
        isGainWeight = Boolean.FALSE;
        if (prefs.getString("isGainWeight", "").equals("True")) {
            isGainWeightButton.toggle();
            isGainWeight = Boolean.TRUE;
        }

        // Sets second Toggle Button
        isSedentaryButton = findViewById(R.id.isSedentary);
        isSedentary = Boolean.FALSE;
        if (prefs.getString("isSedentary", "").equals("True")) {
            isSedentaryButton.toggle();
            isSedentary = Boolean.TRUE;
        }
    }

    private void setOnClickListeners() {

        // Click Listener for Create Goal Button
        createGoal = findViewById(R.id.buttonCreateGoal);
        createGoal.setOnClickListener(this);

        // Click Listener for First Toggle Button
        isGainWeightButton = (ToggleButton) findViewById(R.id.isGainWeight);
        isSedentaryButton = (ToggleButton) findViewById(R.id.isSedentary);
        isSedentaryButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isSedentary = Boolean.TRUE;
            } else {
                isSedentary = Boolean.FALSE;
            }
        });

        // Click Listener for Second Toggle Button
        isGainWeightButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isGainWeight = Boolean.TRUE;
            } else {
                isGainWeight = Boolean.FALSE;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initializes XML
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_goal);

        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        userInfoViewModel.getUserInfo().observe(this, allUserInfo -> {

            // Get the db field values and set global variables

            db_age = allUserInfo.get(0).getAge();
            db_height = allUserInfo.get(0).getHeight();
            db_weight = allUserInfo.get(0).getWeight();
            db_sex = allUserInfo.get(0).getSex();

        });

        // Sets Preference File
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Loads GUI Values from Preference File
        loadStoredValues();

        // Sets CreateGoalButton and ToggleButton Listeners
        setOnClickListeners();
    }

    @Override
    public void onClick(View view) {

        // Convert Initialize Variables with Values from Prefs
        float floatWeight = Float.parseFloat(db_weight);
        float floatHeight = Float.parseFloat(db_height);
        float floatAge = Float.parseFloat(db_age);
        String sex = db_sex;

        // Create Local Variables
        float BMR;
        float dailyCalBurn;
        float poundsToLooseOrGain = Float.parseFloat(((TextView) findViewById(R.id.editTextPoundsLooseGain)).getText().toString());
        int dailyCalGoal;
        String goalOutputText;

        // Calculate BMR
        if (sex.equals("Male")) {
            BMR = (float) (66.47 + (6.24 * floatWeight) + (12.7 * floatHeight - (6.755 * floatAge)));
        } else {
            BMR = (float) (655.1 + (4.35 + floatWeight + (4.7 * floatHeight) - (4.7 * floatAge)));
        }

        // Calculate Daily Calories Burned
         // https://www.healthline.com/health/what-is-basal-metabolic-rate#How-many-calories-you-need-everyday-
         // https://www.livestrong.com/article/312510-how-many-calories-are-burned-daily-by-active-sedentary-people/
        if (isSedentary) {
            dailyCalBurn = (float) (BMR * 1.2); // Sedentary
        } else {
            dailyCalBurn = (float) (BMR * 1.635); // Active (Average of Moderately Active & Very Active)
        }

        // Calculate Daily Calorie Goal (1 lb = 3500 calories)
        // http://www.bmrcalculator.org/how-many-calories-to-lose-a-pound/
        if (isGainWeight) {
            // (dailyCalGoal - dailyCalBurn) * 7 = (poundsToLooseOrGain * 3500) => Solve for dailyCalGoal
            dailyCalGoal = (int) (dailyCalBurn + ((poundsToLooseOrGain * 3500) / 7));
        } else {
            dailyCalGoal = (int) (dailyCalBurn - ((poundsToLooseOrGain * 3500) / 7));
        }

        // Create Goal Output Text
        if (poundsToLooseOrGain > 2) {
            goalOutputText = "You should not try to increase or decrease your weight by more than 2 pounds per week. " +
                    "Please decrease the number of pounds per week.";
        } else if (sex.equals("Female") && dailyCalGoal < 1000) {
            goalOutputText = "You will not meet the 1000 calories needed per/day with this goal. " +
                    "Please decrease the number of pounds to loose per week.";
        } else if (sex.equals("Male") && dailyCalGoal < 1200) {
            goalOutputText = "You will not meet the 1200 calories needed per/day with this goal. " +
                    "Please decrease the number of pounds to loose per week.";
        } else {
            goalOutputText = "You burn about " + (int)dailyCalBurn + " calories per day. Eat about " + dailyCalGoal + " calories per day to reach your goal.";
        }

        // Display Goal Output Text
        TextView goalOutput = (TextView) findViewById(R.id.planResult);
        goalOutput.setText(goalOutputText);

        // Update Stored Values
        prefs.edit().putString("poundsLooseGain", String.valueOf(poundsToLooseOrGain)).apply();
        prefs.edit().putString("fitnessGoal",goalOutputText).apply();
        if (isGainWeight) {
            prefs.edit().putString("isGainWeight","True").apply();
        } else {
            prefs.edit().putString("isGainWeight","False").apply();
        }
        if (isSedentary) {
            prefs.edit().putString("isSedentary","True").apply();
        } else {
            prefs.edit().putString("isSedentary","False").apply();
        }
    }
}