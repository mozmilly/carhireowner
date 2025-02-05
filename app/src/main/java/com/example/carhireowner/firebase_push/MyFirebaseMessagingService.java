package com.example.carhireowner.firebase_push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.carhireowner.MainActivity;
import com.example.carhireowner.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final int NOTIFICATION_ID = 118;
    String title, message, bigpicture, url, username;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        Toast.makeText(MyFirebaseMessagingService.this, "message", Toast.LENGTH_SHORT).show();
        Log.i("TAG", "Complete 42");
        title = remoteMessage.getNotification().getTitle();
        message = remoteMessage.getNotification().getBody();





        Intent intent;


        intent = new Intent(this, MainActivity.class);


        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.song);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setSound(soundUri)
                .setContentText(message);


        if(soundUri != null){
            // Changing Default mode of notification
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            // Creating an Audio Attribute
            AudioAttributes audioAttributes = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                // Creating Channel
                NotificationChannel notificationChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = new NotificationChannel("CH_ID","Testing_Audio", NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.setSound(soundUri,audioAttributes);
                    notificationManager.createNotificationChannel(notificationChannel);
                }


            }


        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        try {
            resultPendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }



}

