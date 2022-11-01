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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.CustomJavaObjects.Timer;

public class FillInTheBlankActivity extends AppCompatActivity {

    private static final String TAG = "PhapNguyen";

    private static int availableAttempts;
    private Timer onScreenTimer;
    private Timer lifeCycleTimer;
    private static long timeLimitInSeconds;
    private boolean questionIsFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_the_blank);

        availableAttempts = 2;
        onScreenTimer = new Timer();
        lifeCycleTimer = new Timer();
        timeLimitInSeconds = -1;
        questionIsFinished = false;

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
        runTimer(onScreenTimer, findViewById(R.id.tvOnScreenTimerFTB));
        runTimer(lifeCycleTimer, findViewById(R.id.tvTotalTimerFTB));
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
                            timeLimitInSeconds = data.getExtras().getLong("timeLimit");
                        }

                        Log.d(TAG, "onActivityResult in FillInTheBlankActivity: \n" +
                                "hideOnScreenTimer: " + hideOnScreenTimer + "\n" +
                                "hideLifecycleTimer: "  + hideLifecycleTimer + "\n" +
                                "timeLimit: " +  timeLimitInSeconds
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

    private void loadResultActivityWithPass() {
        // load ResultActivity that user has PASSED
        questionIsFinished = true;
        MultipleChoiceActivity.incrementPassCount();
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("userPassedQuiz", true);
        startActivity(i);
    }

    private void loadResultActivityWithFail() {
        // load ResultActivity that user has FAILED
        questionIsFinished = true;
        MultipleChoiceActivity.incrementFailCount();
        Intent i = new Intent(FillInTheBlankActivity.this, ResultActivity.class);
        i.putExtra("userPassedQuiz", false);
        startActivity(i);
    }


    private void onClickSubmitAnswer() {
        EditText etFITBUserAnswer = (EditText) findViewById(R.id.etFITBUserAnswer);
        String userAnswer = etFITBUserAnswer.getText().toString().toLowerCase();
        String solution = getString(R.string.fitb_solution).toLowerCase();

        if (userAnswer.equals(solution)) {
            // load ResultActivity that user has PASSED
            loadResultActivityWithPass();
        }
        else {
            availableAttempts--;
            if (availableAttempts == 0) {
                // load ResultActivity that user has FAILED
                loadResultActivityWithFail();
            }
            else {
                Toast.makeText(getApplicationContext(), "You have "
                + availableAttempts + " attempts remaining", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void runTimer(Timer timer, TextView targetTimerTextView) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                targetTimerTextView.setText(timer.getTimeRepresentation());

                if (timeLimitInSeconds > -1) {

                    Log.d(TAG, "run: \n" +
                            "timer.getSeconds(): " + timer.getSeconds() + "\n" +
                            "timeLimitInSeconds: " + timeLimitInSeconds + "\n");

                    if (timer.getSeconds() >= timeLimitInSeconds) {
                        // load ResultActivity that user has FAILED
                        loadResultActivityWithFail();
                        // Remove any pending posts of callbacks and sent messages whose obj is token. If token is null, all callbacks and messages will be removed.
                        return;
                    }
                }

                if (timer.isRunning()) {
                    long sum = timer.getSeconds() + 1L;
                    if (sum > Long.MAX_VALUE) {
                        throw new ArithmeticException("Overflow in summing long variables!");
                    }
                    timer.setSeconds(sum);
                }

                // Post the code in the Runnable to be run again after
                // a delay of 1,000 ms or 1 s.
                // As this line of code is included in the Runnable's run() method,
                // this run() method will keep getting called.
                if (!questionIsFinished) {
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }
}