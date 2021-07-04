package com.example.fit5046_assignment3.util;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.fit5046_assignment3.R;

public class NotificationHelper extends ContextWrapper {
    public static  final String channel1ID="channel1ID";
    public static final String channel1Name="Channel 1";
    private NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }
    }
    public void createChannels(){
        NotificationChannel channel = new NotificationChannel(channel1ID,channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.design_default_color_primary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }
    public NotificationManager getManager(){
        if (mManager==null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title,String message){
        return new NotificationCompat.Builder(getApplicationContext(),channel1ID).setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.mapbox_compass_icon);

    }
}
