package com.example.quizapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.support.v4.app.*;


public class DelayedMessageService extends IntentService {

    public static final String EXTRA_MESSAGE = "message";
    public static final int NOTIFICATION_ID = 5453;

    public DelayedMessageService() {
        super("DelayedMessageService");
    }

    // Put the code you want the service to run in the onHandleIntent method
    // It runs in a separate thread. If it’s passed multiple intents, it deals with them one at a time.
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        synchronized (this) {
            try {
                wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

    private void showText(final String text) {

        // Create a notification builder
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this,"notify_001")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentTitle(getString(R.string.question))
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[] {0, 1000})
                    .setAutoCancel(true)
                ;

        // Create an action that acts when the notification is clicked
        // by setting a pending intent to the notification builder
        Intent actionIntent = new Intent(this, MultipleChoiceActivity.class);
        // Create a pending intent
        PendingIntent actionPendingIntent = PendingIntent.getActivity(
                this,
                0,
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


}