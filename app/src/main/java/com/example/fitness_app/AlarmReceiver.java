package com.example.fitness_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

//this class is used to register the alarm and send the notification to stand up
class AlarmReceiver extends BroadcastReceiver {

    //used in notification constructor to place the notification in a group
    public final String ANDROID_CHANNEL_ID = "DataCubed.ANDROID";
    public final String ANDROID_CHANNEL_NAME = "DataCubed.CHANNEL";
    private static final int NOTIFICATION_ID = 0;
    NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        //setting up notification service
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //getting the context

        //Create the content intent for the notification, which launches this activity
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //making the channels to group notification.
        createChannels();

        //building the notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_walk)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("Fitness App")
                .setContentText("Its time to stand up!");


        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//

        mNotificationManager.notify(001, mBuilder.build());
    }

    public void createChannels() {

        // create android channel
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockScreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        mNotificationManager.createNotificationChannel(androidChannel);
    }
}
