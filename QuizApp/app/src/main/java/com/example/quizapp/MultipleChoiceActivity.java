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
import android.widget.ImageButton;
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
    private boolean hideOnScreenTimer;
    private boolean hideLifecycleTimer;
    private long timeLimitInSeconds;
    private boolean useImageButtons;
    private boolean questionIsFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        availableAttempts = getResources().getStringArray(R.array.mc_answers).length - 2;
        onScreenTimer = new Timer();
        lifeCycleTimer = new Timer();
        hideOnScreenTimer = false;
        hideLifecycleTimer = false;
        timeLimitInSeconds = -1;
        useImageButtons = false;
        questionIsFinished = false;

        // Set onClickListener to the submit answer button
        Button btnSubmit = findViewById(R.id.btnSubmitMC);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubmitAnswer();
            }
        });
        ImageButton imgbtnSubmit = findViewById(R.id.imgbtnSubmitMC);
        imgbtnSubmit.setOnClickListener(new View.OnClickListener() {
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


                        if (data != null) {
                            hideOnScreenTimer = data.getExtras().getBoolean("hideOnScreenTimer");
                            hideLifecycleTimer = data.getExtras().getBoolean("hideLifecycleTimer");
                            timeLimitInSeconds = data.getExtras().getLong("timeLimit");
                            useImageButtons = data.getExtras().getBoolean("useImageButtons");
                        }

                        Log.d(TAG, "onActivityResult in FillInTheBlankActivity: \n" +
                                "hideOnScreenTimer: " + hideOnScreenTimer + "\n" +
                                "hideLifecycleTimer: "  + hideLifecycleTimer + "\n" +
                                "timeLimit: " +  timeLimitInSeconds + "\n" +
                                "useImageButtons: " + useImageButtons + "\n"
                        );

                        showTimerConfigPreference();
                        showButtonConfigPreference();
                    }
                }
            }
    );

    private void showTimerConfigPreference() {
        findViewById(R.id.tvOnScreenTimerMC).setVisibility(hideOnScreenTimer ? View.INVISIBLE : View.VISIBLE );
        findViewById(R.id.tvTotalTimerMC).setVisibility(hideLifecycleTimer ? View.INVISIBLE : View.VISIBLE);
    }

    private void showButtonConfigPreference() {
        if (useImageButtons) {
            findViewById(R.id.btnSubmitMC).setVisibility(View.INVISIBLE);
            findViewById(R.id.imgbtnSubmitMC).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.btnSubmitMC).setVisibility(View.VISIBLE);
            findViewById(R.id.imgbtnSubmitMC).setVisibility(View.INVISIBLE);
        }
    }

    private void startConfigurationActivity() {
        Intent i = new Intent(this, ConfigurationActivity.class);
        i.putExtra("onScreenTimerIsVisible", findViewById(R.id.tvOnScreenTimerMC).getVisibility() == View.INVISIBLE);
        i.putExtra("lifecycleTimerIsVisible", findViewById(R.id.tvTotalTimerMC).getVisibility() == View.INVISIBLE);
        i.putExtra("useImageButtons", findViewById(R.id.imgbtnSubmitMC).getVisibility() == View.VISIBLE);
        configurationActivityLauncher.launch(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save custom values into the bundle
        savedInstanceState.putInt("availableAttempts", availableAttempts);
        savedInstanceState.putInt("passCount", passCount);
        savedInstanceState.putInt("failCount", failCount);
        savedInstanceState.putBoolean("hideOnScreenTimer", hideOnScreenTimer);
        savedInstanceState.putBoolean("hideLifecycleTimer", hideLifecycleTimer);
        savedInstanceState.putLong("timeLimit", timeLimitInSeconds);
        savedInstanceState.putBoolean("useImageButtons", useImageButtons);

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
        hideOnScreenTimer = savedInstanceState.getBoolean("hideOnScreenTimer");
        hideLifecycleTimer = savedInstanceState.getBoolean("hideLifecycleTimer");
        timeLimitInSeconds = savedInstanceState.getLong("timeLimit");
        useImageButtons = savedInstanceState.getBoolean("useImageButtons");
        showTimerConfigPreference();
        showButtonConfigPreference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onScreenTimer.allowTimerToRun();
    }

    // This is called when the activity is no longer in the foreground
    // (though it may still be visible if the user is in multi-window mode)
    // it's best to save state through the use of Shared Preferences in using onPause
    @Override
    protected void onPause() {
        super.onPause();
        onScreenTimer.stopTimer();
    }

    private void loadResultActivityWithFail() {
        // load ResultActivity that user has FAILED
        questionIsFinished = true;
        incrementFailCount();
        Intent i = new Intent(MultipleChoiceActivity.this, ResultActivity.class);
        i.putExtra("userPassedQuiz", false);
        startActivity(i);
    }

    private void onClickSubmitAnswer() {
        Spinner MCAnswersSpinner1 = findViewById(R.id.MCAnswersSpinner1);
        String choice1 = String.valueOf(MCAnswersSpinner1.getSelectedItem());
        Spinner MCAnswersSpinner2 = findViewById(R.id.MCAnswersSpinner2);
        String choice2 = String.valueOf(MCAnswersSpinner2.getSelectedItem());

        final String solution1 = getString(R.string.mc_solution);
        final String solution2 = "Novak Djokovic";

        if (choice1.equals(solution1) && choice2.equals(solution2)) {
            questionIsFinished = true;
            Intent i = new Intent(this, FillInTheBlankActivity.class);
            startActivity(i);
        }
        else {
            availableAttempts--;
            if (availableAttempts == 0) {
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