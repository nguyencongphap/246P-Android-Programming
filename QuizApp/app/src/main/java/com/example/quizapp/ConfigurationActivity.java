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
import android.widget.Switch;

public class ConfigurationActivity extends AppCompatActivity {

    private static final String TAG = "PhapNguyen ConfigurationActivity";
    Intent intentReturnToParentActivity;
    Switch switchOnScreenTimer;
    Switch switchLifecycleTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "Entered onCreate: ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        intentReturnToParentActivity = new Intent();

        switchOnScreenTimer = findViewById(R.id.switchQ1OnScreenTimer);
        boolean onScreenTimerIsVisible = getIntent().getBooleanExtra("onScreenTimerIsVisible", false);
        switchOnScreenTimer.setChecked(onScreenTimerIsVisible);
        setHideOnScreenTimerValueToIntent(onScreenTimerIsVisible);
        switchOnScreenTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    setHideOnScreenTimerValueToIntent(true);
                }
                else {
                    setHideOnScreenTimerValueToIntent(false);
                }
            }
        });

        switchLifecycleTimer = findViewById(R.id.switchQ1AppLifecycleTimer);
        boolean lifecycleTimerIsVisible = getIntent().getBooleanExtra("lifecycleTimerIsVisible", false);
        switchLifecycleTimer.setChecked(lifecycleTimerIsVisible);
        setHideLifecycleTimerValueToIntent(lifecycleTimerIsVisible);
        switchLifecycleTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    setHideLifecycleTimerValueToIntent(true);
                }
                else {
                    setHideLifecycleTimerValueToIntent(false);
                }
            }
        });

        Button btnUpdateConfig = findViewById(R.id.btnUpdateConfig);
        btnUpdateConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick btnUpdateConfig:\n"
                        + "hideOnScreenTimer: " + intentReturnToParentActivity.getExtras().getBoolean("hideOnScreenTimer") + "\n"
                        + "hideLifecycleTimer: " + intentReturnToParentActivity.getExtras().getBoolean("hideLifecycleTimer") + "\n"
                );

                // Return config data to the parent activity
                setResult(RESULT_OK, intentReturnToParentActivity); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        });

    }

    private void setHideOnScreenTimerValueToIntent(boolean hideOnScreenTimer) {
        intentReturnToParentActivity.putExtra("hideOnScreenTimer", hideOnScreenTimer);
    }

    private void setHideLifecycleTimerValueToIntent(boolean hideLifecycleTimer) {
        intentReturnToParentActivity.putExtra("hideLifecycleTimer", hideLifecycleTimer);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        Log.d(TAG, "Entered onPause: ");
//
//        SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        // Save the activity state
//        editor.putBoolean("switchOnScreenTimerIsChecked", switchOnScreenTimer.isChecked());
//        editor.putBoolean("switchLifecycleTimerIsChecked", switchLifecycleTimer.isChecked());
//        editor.apply();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Log.d(TAG, "Entered onResume: ");
//
//        // Restore the activity state
//        SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
//
//        // Set the state to either the value that was saved or the default value
//        boolean switchOnScreenTimerIsChecked = settings.getBoolean("switchOnScreenTimerIsChecked", false);
//        switchOnScreenTimer.setChecked(switchOnScreenTimerIsChecked);
//        setHideOnScreenTimerToItent(switchOnScreenTimerIsChecked);
//
//        boolean switchLifecycleTimerIsChecked = settings.getBoolean("switchLifecycleTimerIsChecked", false);
//        switchLifecycleTimer.setChecked(switchLifecycleTimerIsChecked);
//        setHideLifecycleTimerToItent(switchLifecycleTimerIsChecked);
//    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//
//        Log.d(TAG, "Entered onSaveInstanceState: ");
//
//        outState.putBoolean("switchOnScreenTimerIsChecked", switchOnScreenTimer.isChecked());
//        outState.putBoolean("switchLifecycleTimerIsChecked", switchLifecycleTimer.isChecked());
//        super.onSaveInstanceState(outState);
//    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//
//        Log.d(TAG, "Entered onRestoreInstanceState: ");
//
//        super.onRestoreInstanceState(savedInstanceState);
//
//        boolean switchOnScreenTimerIsChecked = savedInstanceState.getBoolean("switchOnScreenTimerIsChecked");
//        switchOnScreenTimer.setChecked(switchOnScreenTimerIsChecked);
//        setHideOnScreenTimerValueToIntent(switchOnScreenTimerIsChecked);
//
//        boolean switchLifecycleTimerIsChecked = savedInstanceState.getBoolean("switchLifecycleTimerIsChecked");
//        switchLifecycleTimer.setChecked(switchLifecycleTimerIsChecked);
//        setHideLifecycleTimerValueToIntent(switchLifecycleTimerIsChecked);
//    }




}