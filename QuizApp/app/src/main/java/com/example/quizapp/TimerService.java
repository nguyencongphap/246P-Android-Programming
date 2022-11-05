package com.example.quizapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.quizapp.CustomJavaObjects.Timer;

// TODO: This moddule is done! Bind and use it in MultipleChoiceActivity

public class TimerService extends Service {
    private final IBinder timeServiceBinder = new TimerServiceBinder();

    private Timer onScreenTimer;
    private Timer lifecycleTimer;

    public TimerService() {
        onScreenTimer = new Timer();
        lifecycleTimer = new Timer();
    }

    public Timer getOnScreenTimer() {
        return onScreenTimer;
    }

    public Timer getLifecycleTimer() {
        return lifecycleTimer;
    }

    // We’re going to define a binder called TimerServiceBinder that MainActivity can use to
    // get a reference to TimerService.
    public class TimerServiceBinder extends Binder {
        TimerService getTimerServiceReference() {
            return TimerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        return timeServiceBinder;
    }
}