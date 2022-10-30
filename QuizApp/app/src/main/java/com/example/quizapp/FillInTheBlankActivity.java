package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        onScreenTimer.allowTimerToRun();
        lifeCycleTimer.allowTimerToRun();
        MultipleChoiceActivity.runTimer(onScreenTimer, findViewById(R.id.tvOnScreenTimerFTB));
        MultipleChoiceActivity.runTimer(lifeCycleTimer, findViewById(R.id.tvTotalTimerFTB));
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

    // This is called when the activity is no longer visible to the user.
    @Override
    protected void onStop() {
        super.onStop();
        onScreenTimer.stopTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();
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