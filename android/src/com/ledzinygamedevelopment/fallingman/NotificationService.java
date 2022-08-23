package com.ledzinygamedevelopment.fallingman;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import games.spooky.gdx.notifications.NotificationHandler;
import games.spooky.gdx.notifications.NotificationParameters;
import games.spooky.gdx.notifications.android.AndroidNotificationHandler;

public class NotificationService extends Service {
    //ScheduledExecutorService myschedule_executor;
    //private NotificationManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        //startForeground();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        /*myschedule_executor = Executors.newScheduledThreadPool(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, intent,
                            PendingIntent.FLAG_IMMUTABLE);
            Notification notification =
                    new Notification.Builder(this, "ForegroundServiceChannel")
                            .setContentTitle("My notification")
                            .setContentText("Hello World!")
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .setTicker("ticker")
                            .build();
            //startForeground(123, notification);
            myschedule_executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    //NotificationHandler notificationHandler = new AndroidNotificationHandler(getApplicationContext());
                    //notificationHandler.showNotification(new NotificationParameters(12, "this is title", "this is description"));
                }
            }, 1, 1, TimeUnit.SECONDS);
        }*/

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        NotificationHandler notificationHandler = new AndroidNotificationHandler(getApplicationContext());
        notificationHandler.showNotification(new NotificationParameters(12, "Stickman falling is waiting for you", "come back and earn new skins!"));
    }

    /*@Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //myschedule_executor.shutdown();
    }

    /*private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "ForegroundServiceChannel",
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }*/
}
