package com.ledzinygamedevelopment.fallingman;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.windows.DefaultWindow;
import com.ledzinygamedevelopment.fallingman.tools.AdsController;
import com.ledzinygamedevelopment.fallingman.tools.ToastCreator;

import java.util.Arrays;
import java.util.List;

import de.golfgl.gdxgamesvcs.GpgsClient;

public class AndroidLauncher extends AndroidApplication implements AdsController, ToastCreator {
    FallingMan fallingMan;
    private boolean adLoaded;
    private boolean loadingAd;
    private RewardedAd mRewardedAd;

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


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //wait with ads until initialization complete
            }
        });
        adLoaded = false;
        loadingAd = false;

        initialize(fallingMan, config);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }




    @Override
    public boolean showRewardedVideo(boolean loadOnly) {
        Activity activityContext = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRewardedAd != null) {
                    if (mRewardedAd.getFullScreenContentCallback() == null) {
                        setFullScreenContentCallbackToAdd();
                    }
                    if (!loadOnly) {
                        if (adLoaded) {
                            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    // Handle the reward.
                                    GameScreen gameScreen = FallingMan.getGameScreen();
                                    gameScreen.setGoldX2(true);
                                    if (gameScreen.getClass().equals(PlayScreen.class)) {
                                        for (DefaultWindow defaultWindow : ((PlayScreen) gameScreen).getDefaultWindows()) {
                                            defaultWindow.GoldX2AfterAd();
                                        }
                                    }
                                    /*int rewardAmount = rewardItem.getAmount();
                                    String rewardType = rewardItem.getType();*/
                                }
                            });
                        } else {
                            Log.d(TAG, "The rewarded ad wasn't ready yet.");
                        }
                    }
                } else if (!loadingAd) {
                        loadingAd = true;
                        loadRewardedVideoAd();
                }
            }

        });
        return adLoaded;
    }

    @Override
    public void loadRewardedVideoAd() {
        List<String> testDeviceIds = Arrays.asList("C2BDD513B705BF3B0D03448EF396DAA4");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-9634464954224086/4949323788",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("AD LOADED ERROR on load", loadAdError.toString());
                        mRewardedAd = null;
                        loadingAd = false;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d("AD LOADED", "Ad was loaded.");
                        adLoaded = true;
                        loadingAd = false;
                    }
                });
    }

    public void setFullScreenContentCallbackToAdd() {
        if (mRewardedAd != null) {
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(TAG, "Ad dismissed fullscreen content.");
                    mRewardedAd = null;
                    loadingAd = false;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.");
                    mRewardedAd = null;
                    loadingAd = false;
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.");
                }
            });
        }
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
