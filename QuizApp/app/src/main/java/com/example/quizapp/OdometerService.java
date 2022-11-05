package com.example.quizapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class OdometerService extends Service {
    public OdometerService() {
    }

    private final IBinder binder = new OdometerBinder();
    private final Random random = new Random();

    // We’re going to define a binder called OdometerBinder that MainActivity can use to
    // get a reference to OdometerService.
    public class OdometerBinder extends Binder {
        OdometerService getOdometer() {
            return OdometerService.this;
        }
    }

    // The onBind() method is called when a component wants to bind to the service
    // IBinder is an interface that’s used to bind your service to the activity, and you need to
    // provide an implementation of it in your service code.
    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        return binder;
    }

    public double getDistance() {
        return random.nextDouble();
    }
}