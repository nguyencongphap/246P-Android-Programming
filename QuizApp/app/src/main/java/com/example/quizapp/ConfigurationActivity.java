package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigurationActivity extends AppCompatActivity {

    private static final String TAG = "PhapNguyen ConfigurationActivity";
    private Switch switchOnScreenTimer;
    private Switch switchLifecycleTimer;
    private EditText etTimeLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        // Reflect the state of timer in parent activity
        switchOnScreenTimer = findViewById(R.id.switchQ1OnScreenTimer);
        boolean onScreenTimerIsVisible = getIntent().getBooleanExtra("onScreenTimerIsVisible", false);
        switchOnScreenTimer.setChecked(onScreenTimerIsVisible);

        // Reflect the state of timer in parent activity
        switchLifecycleTimer = findViewById(R.id.switchQ1AppLifecycleTimer);
        boolean lifecycleTimerIsVisible = getIntent().getBooleanExtra("lifecycleTimerIsVisible", false);
        switchLifecycleTimer.setChecked(lifecycleTimerIsVisible);

        etTimeLimit = findViewById(R.id.etTimeLimit);

        Button btnUpdateConfig = findViewById(R.id.btnUpdateConfig);
        btnUpdateConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateConfig();
            }
        });

    }

    private void onClickUpdateConfig() {
        Intent intentReturnToParentActivity = new Intent();
        intentReturnToParentActivity.putExtra("hideOnScreenTimer", switchOnScreenTimer.isChecked());
        intentReturnToParentActivity.putExtra("hideLifecycleTimer", switchLifecycleTimer.isChecked());
        intentReturnToParentActivity.putExtra("timeLimit", convertToSeconds(etTimeLimit.getText().toString()));

        Log.d(TAG, "onClick btnUpdateConfig:\n"
                + "hideOnScreenTimer: " + intentReturnToParentActivity.getExtras().getBoolean("hideOnScreenTimer") + "\n"
                + "hideLifecycleTimer: " + intentReturnToParentActivity.getExtras().getBoolean("hideLifecycleTimer") + "\n"
                + "timeLimit: " + intentReturnToParentActivity.getExtras().getLong("timeLimit") + "\n"
        );

        // Return config data to the parent activity
        setResult(RESULT_OK, intentReturnToParentActivity); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    private long convertToSeconds(String timeStrInput) {
        String[] timeData = timeStrInput.split(":");
        String hours = timeData[0];
        String minutes = timeData[1];
        String seconds = timeData[2];

        Log.d(TAG, "convertToSeconds: \n" +
                "hours: " + hours + "\n" +
                "minutes: " + minutes + "\n" +
                "seconds: " + seconds
        );

        return Long.valueOf(hours) * 3600 + Long.valueOf(minutes) * 60 + Long.valueOf(seconds);
    }



}