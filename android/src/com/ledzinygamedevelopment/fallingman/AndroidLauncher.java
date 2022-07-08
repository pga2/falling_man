package com.ledzinygamedevelopment.fallingman;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
        }

        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        Runtime rt= Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        long freeMemory = rt.freeMemory();
        Log.d("Memory Available", "memoryClass:" + Integer.toString(memoryClass));
        Log.d("Max Memory Available", "max memory:" + Long.toString(maxMemory));
        Log.d("Free Memory", "Free Memory: " + Long.toString(freeMemory));

        initialize(new FallingMan(), config);
    }

}
