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

import com.ledzinygamedevelopment.fallingman.tools.GsClientUtils;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

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

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
            NotificationHandler notificationHandler = new AndroidNotificationHandler(getApplicationContext());
            notificationHandler.showNotification(new NotificationParameters(12, "Stickman falling is waiting for you", "Come back and earn new skins!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
