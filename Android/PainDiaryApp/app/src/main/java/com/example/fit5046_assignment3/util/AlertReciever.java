package com.example.fit5046_assignment3.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.fit5046_assignment3.MainActivity;


public class AlertReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("todo","Time to Care Your Health");
        notificationHelper.getManager().notify(1,nb.build());

//        int notificationId = intent.getIntExtra("notificationId",0);
//        String message = intent.getStringExtra("todo");
//
//        Intent mainIntent = new Intent(context, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setContentTitle("Time to Care Your Health")
//                .setContentText(message)
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
//                .setContentIntent(contentIntent);
//
//        notificationManager.notify(notificationId, builder.build());
    }
}
