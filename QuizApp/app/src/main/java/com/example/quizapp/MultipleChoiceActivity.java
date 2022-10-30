package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.CustomJavaObjects.Timer;

public class MultipleChoiceActivity extends AppCompatActivity {

    private static final String TAG = "PhapNguyen";

    private static int availableAttempts;
    private static int passCount = 0;
    private static int failCount = 0;
    private Timer onScreenTimer;
    private Timer lifeCycleTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        availableAttempts = getResources().getStringArray(R.array.mc_answers).length - 2;
        onScreenTimer = new Timer();
        lifeCycleTimer = new Timer();

        // Set onClickListener to the submit answer button
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubmitAnswer();
            }
        });

        onScreenTimer.allowTimerToRun();
        lifeCycleTimer.allowTimerToRun();
        runTimer(onScreenTimer, findViewById(R.id.tvOnScreenTimerMC));
        runTimer(lifeCycleTimer, findViewById(R.id.tvTotalTimerMC));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save custom values into the bundle
        savedInstanceState.putInt("availableAttempts", availableAttempts);
        savedInstanceState.putInt("passCount", passCount);
        savedInstanceState.putInt("failCount", failCount);

//        savedInstanceState.putParcelable("onScreenTimer", onScreenTimer);
//        savedInstanceState.putParcelable("lifeCycleTimer", lifeCycleTimer);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
//        onScreenTimer = savedInstanceState.getParcelable("onScreenTimer");
//        lifeCycleTimer = savedInstanceState.getParcelable("lifeCycleTimer");
        availableAttempts = savedInstanceState.getInt("availableAttempts");
        passCount = savedInstanceState.getInt("passCount");
        failCount = savedInstanceState.getInt("failCount");
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
        Spinner MCAnswersSpinner = findViewById(R.id.MCAnswersSpinner);
        String choice = String.valueOf(MCAnswersSpinner.getSelectedItem());
        String solution = getString(R.string.mc_solution);

        if (choice.equals(solution)) {
            Intent i = new Intent(this, FillInTheBlankActivity.class);
            startActivity(i);
        }
        else {
            availableAttempts--;
            if (availableAttempts == 0) {
                incrementFailCount();
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

    public static void runTimer(Timer timer, TextView targetTimerTextView) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int totalSeconds = timer.getSeconds();
                int hours = totalSeconds/3600;
                int minutes = (totalSeconds%3600)/60;
                int secs = totalSeconds%60;
                String time = String.format("%d:%02d:%02d", hours, minutes, secs);

                targetTimerTextView.setText(time);

                if (timer.isRunning()) {
                    timer.setSeconds(timer.getSeconds()+1);
                }

                // Post the code in the Runnable to be run again after
                // a delay of 1,000 ms or 1 s.
                // As this line of code is included in the Runnable's run() method,
                // this run() method will keep getting called.
                handler.postDelayed(this, 1000);
            }
        });
    }

    public static void incrementPassCount() {
        passCount++;
    }

    public static void incrementFailCount() {
        failCount++;
    }

    public static int getPassCount() {
        return passCount;
    }

    public static int getFailCount() {
        return failCount;
    }


}