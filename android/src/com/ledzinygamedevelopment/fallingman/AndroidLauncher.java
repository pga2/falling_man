package com.ledzinygamedevelopment.fallingman;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.ledzinygamedevelopment.fallingman.scenes.HUD;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.windows.DefaultWindow;
import com.ledzinygamedevelopment.fallingman.tools.AdsController;
import com.ledzinygamedevelopment.fallingman.tools.GsClientUtils;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;
import com.ledzinygamedevelopment.fallingman.tools.ToastCreator;

import java.util.Arrays;

import de.golfgl.gdxgamesvcs.GpgsClient;

public class AndroidLauncher extends AndroidApplication implements AdsController, RewardedVideoAdListener, ToastCreator {
    //Window window;
    FallingMan fallingMan;
    private RewardedVideoAd rewardedVideoAd;
    private boolean adLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fallingMan = new FallingMan(this, this);

        fallingMan.gsClient = new GpgsClient().initialize(this, true);


        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
        }

        config.useGyroscope = false;  //default is false

        //you may want to switch off sensors that are on by default if they are no longer needed.
        config.useAccelerometer = false;
        config.useCompass = false;

        config.useImmersiveMode = true;


        fallingMan.purchaseManager = new PurchaseManagerGoogleBilling(this);

        /*NotificationHandler notificationHandler = new AndroidNotificationHandler(getContext());

        notificationHandler.showNotification(new NotificationParameters(12, "this is title", "this is description"));*/

        initialize(fallingMan, config);

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);

        //loadRewardedVideoAd();

        /*RelativeLayout layout = new RelativeLayout(this);

        adView = new AdView(this);

        AdRequest.Builder builder = new AdRequest.Builder();

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "Ad Loaded...");
                Gdx.app.log("dziala", "-----\n\n\n-----\n\n\n-----\n\n\n-----");
            }
        });
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/5224354917");

        *//*.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("ca-app-pub-9634464954224086/1161727529")*//*;

        layout.addView(gameView);

        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        layout.addView(adView, adParams);
        adView.loadAd(builder.build());

        setContentView(layout);*/

    }

    @Override
    protected void onStop() {

        super.onStop();
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }*/
    }




    @Override
    public boolean showRewardedVideo(boolean loadOnly) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardedVideoAd.isLoaded()) {
                    if (!loadOnly) {
                        rewardedVideoAd.show();
                    }
                    adLoaded = true;
                } else {
                    loadRewardedVideoAd();
                    adLoaded = false;
                }
            }
        });
        return adLoaded;
    }

    @Override
    public void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd("ca-app-pub-9634464954224086/7471266108",
                new AdRequest.Builder()/*.addTestDevice("C2BDD513B705BF3B0D03448EF396DAA4")*/.build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(this, "ad loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        Gdx.app.log("ggggggggggggggggggggggggggggggggggggggggggg\n\n\nnagroda\n\n\n000000000000000000000", "dziala\n\n\n\n\n");
        //loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //Toast.makeText(this, "Reward:", rewardItem.getAmount()).show();
        //FallingMan.getGameScreen().setNewLife(true);
        GameScreen gameScreen = FallingMan.getGameScreen();
        gameScreen.setGoldX2(true);
        if (gameScreen.getClass().equals(PlayScreen.class)) {
            for (DefaultWindow defaultWindow : ((PlayScreen) gameScreen).getDefaultWindows()) {
                defaultWindow.GoldX2AfterAd();
            }
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Gdx.app.log("adstatus", "\n\n\n_____\nfailed to load\n-----\n\n\n  " + i + "\n\n\n\n");
    }

    @Override
    public void onRewardedVideoCompleted() {

    }


    @Override
    public void makeToast(String text) {
        AndroidLauncher androidLauncher = this;
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(androidLauncher, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
