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

        Button btnConfigMC = findViewById(R.id.btnConfigMC);
        btnConfigMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConfigurationActivity();
            }
        });

        onScreenTimer.allowTimerToRun();
        lifeCycleTimer.allowTimerToRun();
        runTimer(onScreenTimer, findViewById(R.id.tvOnScreenTimerMC));
        runTimer(lifeCycleTimer, findViewById(R.id.tvTotalTimerMC));
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

                        Log.d(TAG, "onActivityResult: \n" +
                                "hideOnScreenTimer: " + hideOnScreenTimer + "\n" +
                                "hideLifecycleTimer: "  + hideLifecycleTimer + "\n"
                        );

                        findViewById(R.id.tvOnScreenTimerMC).setVisibility(hideOnScreenTimer ? View.INVISIBLE : View.VISIBLE );
                        findViewById(R.id.tvTotalTimerMC).setVisibility(hideLifecycleTimer ? View.INVISIBLE : View.VISIBLE);
                    }
                }
            }
    );

    private void startConfigurationActivity() {
        Intent i = new Intent(this, ConfigurationActivity.class);
        i.putExtra("onScreenTimerIsVisible", findViewById(R.id.tvOnScreenTimerMC).getVisibility() == View.INVISIBLE);
        i.putExtra("lifecycleTimerIsVisible", findViewById(R.id.tvTotalTimerMC).getVisibility() == View.INVISIBLE);
        configurationActivityLauncher.launch(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save custom values into the bundle
        savedInstanceState.putInt("availableAttempts", availableAttempts);
        savedInstanceState.putInt("passCount", passCount);
        savedInstanceState.putInt("failCount", failCount);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        availableAttempts = savedInstanceState.getInt("availableAttempts");
        passCount = savedInstanceState.getInt("passCount");
        failCount = savedInstanceState.getInt("failCount");
    }

    // This is called when the activity is no longer in the foreground
    // (though it may still be visible if the user is in multi-window mode)
    // it's best to save state through the use of Shared Preferences in using onPause
    // TODO: Add implementations to onPause and onEdit so that chosen choices are not reset when hit Back
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
        Spinner MCAnswersSpinner1 = findViewById(R.id.MCAnswersSpinner1);
        String choice1 = String.valueOf(MCAnswersSpinner1.getSelectedItem());
        Spinner MCAnswersSpinner2 = findViewById(R.id.MCAnswersSpinner2);
        String choice2 = String.valueOf(MCAnswersSpinner2.getSelectedItem());

        final String solution1 = getString(R.string.mc_solution);
        final String solution2 = "Novak Djokovic";

        if (choice1.equals(solution1) && choice2.equals(solution2)) {
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