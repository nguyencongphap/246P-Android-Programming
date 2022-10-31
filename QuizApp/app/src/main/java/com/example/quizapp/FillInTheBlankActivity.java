package com.example.quizapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizapp.CustomJavaObjects.Timer;

public class FillInTheBlankActivity extends AppCompatActivity {

    private static final String TAG = "PhapNguyen";

    private static int availableAttempts;
    private Timer onScreenTimer;
    private Timer lifeCycleTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_the_blank);

        availableAttempts = 2;
        onScreenTimer = new Timer();
        lifeCycleTimer = new Timer();

        // Set onClickListener to the submit answer button
        Button btnSubmit = findViewById(R.id.btnSubmitFillTheBlank);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubmitAnswer();
            }
        });

        Button btnConfigFTB = findViewById(R.id.btnConfigFTB);
        btnConfigFTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConfigurationActivity();
            }
        });

        onScreenTimer.allowTimerToRun();
        lifeCycleTimer.allowTimerToRun();
        MultipleChoiceActivity.runTimer(onScreenTimer, findViewById(R.id.tvOnScreenTimerFTB));
        MultipleChoiceActivity.runTimer(lifeCycleTimer, findViewById(R.id.tvTotalTimerFTB));
    }

    ActivityResultLauncher<Intent> configurationActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Execute this if the user comes back to this activity from
                    // ConfigurationActivity with no error or cancellation
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // Get the data passed from ConfigurationActivity
                        boolean hideOnScreenTimer = false;
                        boolean hideLifecycleTimer = false;
                        if (data != null) {
                            hideOnScreenTimer = data.getExtras().getBoolean("hideOnScreenTimer");
                            hideLifecycleTimer = data.getExtras().getBoolean("hideLifecycleTimer");
                        }

                        Log.d(TAG, "onActivityResult in FillInTheBlankActivity: \n" +
                                "hideOnScreenTimer: " + hideOnScreenTimer + "\n" +
                                "hideLifecycleTimer: "  + hideLifecycleTimer + "\n"
                        );

                        findViewById(R.id.tvOnScreenTimerFTB).setVisibility(hideOnScreenTimer ? View.INVISIBLE : View.VISIBLE );
                        findViewById(R.id.tvTotalTimerFTB).setVisibility(hideLifecycleTimer ? View.INVISIBLE : View.VISIBLE);
                    }
                }
            }
    );

    private void startConfigurationActivity() {
        Intent i = new Intent(this, ConfigurationActivity.class);
        configurationActivityLauncher.launch(i);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        // Save custom values into the bundle
        savedInstanceState.putInt("availableAttempts", availableAttempts);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        availableAttempts = savedInstanceState.getInt("availableAttempts");
    }

    // TODO: Add implementations to onPause and onEdit so that the typed answer are not reset when hit Back
    @Override
    protected void onPause() {
        super.onPause();
        onScreenTimer.stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onScreenTimer.allowTimerToRun();
    }


    private void onClickSubmitAnswer() {
        EditText etFITBUserAnswer = (EditText) findViewById(R.id.etFITBUserAnswer);
        String userAnswer = etFITBUserAnswer.getText().toString().toLowerCase();
        String solution = getString(R.string.fitb_solution).toLowerCase();

        if (userAnswer.equals(solution)) {
            MultipleChoiceActivity.incrementPassCount();
//            load ResultActivity that user has PASSED
            Intent i = new Intent(this, ResultActivity.class);
            i.putExtra("userPassedQuiz", true);
            startActivity(i);
        }
        else {
            availableAttempts--;
            if (availableAttempts == 0) {
                MultipleChoiceActivity.incrementFailCount();
                // load ResultActivity that user has FAILED
                Intent i = new Intent(this, ResultActivity.class);
                i.putExtra("userPassedQuiz", false);
                startActivity(i);
            }
            else {
                Toast.makeText(getApplicationContext(), "You have "
                + availableAttempts + " attempts remaining", Toast.LENGTH_SHORT).show();
            }
        }

    }
}