package com.ledzinygamedevelopment.fallingman;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.ledzinygamedevelopment.fallingman.tools.AdsController;

public class AndroidLauncher extends AndroidApplication implements AdsController, RewardedVideoAdListener {
    //Window window;
    FallingMan fallingMan;
    private RewardedVideoAd rewardedVideoAd;

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

        config.useImmersiveMode = true;

        fallingMan = new FallingMan(this);
        fallingMan.purchaseManager = new PurchaseManagerGoogleBilling(this);



        initialize(fallingMan, config);

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

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
    public void showRewardedVideo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                }
                else loadRewardedVideoAd();
            }
        });
    }

    @Override
    public void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "ad loaded", Toast.LENGTH_SHORT).show();
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
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "Reward:", rewardItem.getAmount()).show();
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
}
