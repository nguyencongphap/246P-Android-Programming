package com.example.quizapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


// TODO: test run the timer service, everything is pretty clean now
public class MultipleChoiceActivity extends AppCompatActivity {

    private static final String TAG = "PhapNguyen";

    private static int availableAttempts;
    private static int passCount = 0;
    private static int failCount = 0;

    private boolean hideOnScreenTimer;
    private boolean hideLifecycleTimer;
    private long timeLimitInSeconds;
    private boolean useImageButtons;
    private boolean questionIsFinished;

    private ServiceConnection timerServiceConnection;
    private TimerService timerService;
    private boolean timerServiceIsBound = false;


    // ### ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        availableAttempts = getResources().getStringArray(R.array.mc_answers).length - 2;
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



        // When MainActivity is created, it creates a ServiceConnection object
        // A ServiceConnection is an interface that enables your activity to bind to a service.
        timerServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                // Code that runs when the service is connected
                // It takes two parameters: a ComponentName object that describes the service that’s been
                // connected to, and an IBinder object that’s defined by the service

                // Use its IBinder parameter to get a reference to the service we’re connected to
                timerService = ((TimerService.TimerServiceBinder) iBinder).getTimerServiceReference();
                timerServiceIsBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                // record that the activity is no longer bound to the service
                timerServiceIsBound = false;
            }
        };

        if (timerServiceIsBound && timerService != null) {

        }
        // Bind activity to service in onCreate() to receive updates from the service even
        // when the activity’s stopped.
        // BIND_AUTO_CREATE: automatically create the service as long as the binding exists.
        Intent intent = new Intent(this, TimerService.class);
        bindService(intent, timerServiceConnection, Context.BIND_AUTO_CREATE);

        displayTime(findViewById(R.id.tvOnScreenTimerMC), findViewById(R.id.tvLifecycleTimerMC));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");

        if (timerServiceIsBound && timerService != null) {
            timerService.getOnScreenTimer().runTimer();

            Log.d(TAG, "onResume: ");
        }


    }

    // This is called when the activity is no longer in the foreground
    // (though it may still be visible if the user is in multi-window mode)
    // it's best to save state through the use of Shared Preferences in using onPause
    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause: ");

        if (timerServiceIsBound && timerService != null) {
            timerService.getOnScreenTimer().stopTimer();

            Log.d(TAG, "onPause: ");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: ");

        unbindTimerService();
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

    private void displayTime(TextView tvOnScreenTimer, TextView tvLifecycleTimer) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "run: Calling run");
                String onScreenTimeRepresentation = "00:00:00";
                String lifecycleTimeRepresentation = "00:00:00";

                if (timerServiceIsBound && timerService != null) {
                    // call methods of the Service
                    timerService.getOnScreenTimer().incrementOneSecond();
                    timerService.getLifecycleTimer().incrementOneSecond();

                    onScreenTimeRepresentation = timerService.getOnScreenTimer().getTimeRepresentation();
                    lifecycleTimeRepresentation = timerService.getLifecycleTimer().getTimeRepresentation();

                    Log.d(TAG, "onScreenTimeRepresentation: " + onScreenTimeRepresentation + "\n" +
                            "lifecycleTimeRepresentation: " + lifecycleTimeRepresentation);
                }
                tvOnScreenTimer.setText(onScreenTimeRepresentation);
                tvLifecycleTimer.setText(lifecycleTimeRepresentation);

                if (timeLimitInSeconds > -1) {
                    Log.d(TAG, "run: \n" +
                            "timerService.getLifecycleTimer(): " + timerService.getLifecycleTimer().getSeconds() + "\n" +
                            "timeLimitInSeconds: " + timeLimitInSeconds + "\n");

                    if (timerService.getLifecycleTimer().getSeconds() >= timeLimitInSeconds ||
                            timerService.getOnScreenTimer().getSeconds() >= timeLimitInSeconds
                    ) {
                        showResultToast();

                        showNotification("Time is up!", "You have failed your quiz", 1);

                        // load ResultActivity that user has FAILED
                        loadResultActivityWithFail();
                        finish();
                    }
                }

                if (!questionIsFinished) {
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    private void showResultToast() {
        // Use a thread other than the main application thread to show toast
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(timerService, "You have failed your quiz", Toast.LENGTH_LONG).show();
            }
        });

//        final Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(timerService, "You have failed your quiz", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void showNotification(final String title, final String text, final int NOTIFICATION_ID) {

        // Create a notification builder
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this,"notify_001")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVibrate(new long[] {0, 1000})
                        .setAutoCancel(true)
                ;

        // Create an action that acts when the notification is clicked
        // by setting a pending intent to the notification builder
        Intent actionIntent = new Intent(this, ResultActivity.class);
        // Create a pending intent
        PendingIntent actionPendingIntent = PendingIntent.getActivity(
                this,
                0,  // parameter is a flag that's used if we ever need to retrieve the pending intent. We don't need to, so we use 0.
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT); // This means that if there's a matching pending intent, its Extras will be updated with the current's Extras
        builder.setContentIntent(actionPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android 8 (Android O) introduced a new requirement of setting the channelId property by using a NotificationChannel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }

        //Issue the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());   // Display the notification using the notification manager
    }

    // Callback function that executes after navigating back to this activity from ConfigurationActivity
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
        findViewById(R.id.tvLifecycleTimerMC).setVisibility(hideLifecycleTimer ? View.INVISIBLE : View.VISIBLE);
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
        i.putExtra("lifecycleTimerIsVisible", findViewById(R.id.tvLifecycleTimerMC).getVisibility() == View.INVISIBLE);
        i.putExtra("useImageButtons", findViewById(R.id.imgbtnSubmitMC).getVisibility() == View.VISIBLE);
        configurationActivityLauncher.launch(i);
    }

    private void unbindTimerService() {
        // use ServiceConnection object to unbind from the service
        if (timerServiceIsBound && timerService != null) {
            unbindService(timerServiceConnection);
            timerServiceIsBound = false;
        }
    }

    private void loadResultActivityWithFail() {
        // load ResultActivity that user has FAILED
        questionIsFinished = true;
        incrementFailCount();
        unbindTimerService();
        Intent i = new Intent(MultipleChoiceActivity.this, ResultActivity.class);
        i.putExtra("userPassedQuiz", false);
        startActivity(i);
        finish();
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
            unbindTimerService();
            Intent i = new Intent(this, FillInTheBlankActivity.class);
            startActivity(i);
            finish();
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