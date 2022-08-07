package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.gamestate.ILoadGameStateResponseListener;
import de.golfgl.gdxgamesvcs.gamestate.ISaveGameStateResponseListener;

public class GsClientUtils {

    public static void distanceAchievementUnlocker(IGameServiceClient gsClient, long dist) {
        if (dist <= 10000) {
            switch ((int) dist) {
                case 1:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQBA");
                    break;
                case 2:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQAQ");

                    break;
                case 5:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQBQ");

                    break;
                case 10:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQBg");

                    break;
                case 15:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQBw");

                    break;
                case 20:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQCA");

                    break;
                case 30:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQCQ");

                    break;
                case 40:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQCg");

                    break;
                case 50:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQCw");

                    break;
                case 75:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQDA");

                    break;
                case 100:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQDQ");

                    break;
                case 200:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQDg");

                    break;
                case 500:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQDw");

                    break;
                case 1000:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQEA");

                    break;
                case 2000:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQEQ");

                    break;
                case 5000:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQEg");

                    break;
                case 10000:
                    gsClient.unlockAchievement("CgkI-N6Fv6wJEAIQEw");

                    break;
            }
        }
    }

    public static void saveData(IGameServiceClient gsClient, long progressValue) {
        if (gsClient.isFeatureSupported(IGameServiceClient.GameServiceFeature.GameStateStorage)) {
            //gsClient.loadGameState(Gdx.files.internal(), new ILoadGameStateResponseListener() {...});
            HashMap<String, Object> prefsMap = new HashMap<>(Gdx.app.getPreferences("StickManFallingPreferences").get());
            String dataToSave = "";
            for (String prefName : prefsMap.keySet()) {
                dataToSave += prefsMap.get(prefName).getClass().getName().substring(10) + ":" + prefName + ":" + prefsMap.get(prefName) + ";";
            }

            gsClient.saveGameState("StickManFallingPreferencesCloud", dataToSave.getBytes(), progressValue,
                    new ISaveGameStateResponseListener() {
                        @Override
                        public void onGameStateSaved(boolean success, String errorCode) {
                            Gdx.app.log("success ", String.valueOf(success));
                            Gdx.app.log("error code ", errorCode);
                        }
                    });

        }
    }

    public static void loadData(IGameServiceClient gsClient, final GameScreen gameScreen) {
        gsClient.loadGameState("StickManFallingPreferencesCloud", new ILoadGameStateResponseListener() {
            @Override
            public void gsGameStateLoaded(byte[] gameState) {
                String output = "";
                if (gameState != null) {
                    output = new String(gameState, StandardCharsets.UTF_8);

                    Preferences preferences = Gdx.app.getPreferences("StickManFallingPreferences");
                    String[] eachPrefs = output.split(";");
                    for (String eachPref : eachPrefs) {
                        String[] eachPrefValues = eachPref.split(":");
                        switch (eachPrefValues[0]) {
                            case "Long":
                                if (eachPrefValues[1].equals("saveCounter")) {
                                    if (preferences.getLong("saveCounter") < Long.parseLong(eachPrefValues[2])) {
                                        preferences.putLong("saveCounter", Long.parseLong(eachPrefValues[2]));
                                    }
                                } else {
                                    preferences.putLong(eachPrefValues[1], Long.parseLong(eachPrefValues[2]));
                                }
                                preferences.flush();
                                break;
                            case "Integer":
                                preferences.putInteger(eachPrefValues[1], Integer.parseInt(eachPrefValues[2]));
                                preferences.flush();
                                break;
                            case "Boolean":
                                preferences.putBoolean(eachPrefValues[1], Boolean.parseBoolean(eachPrefValues[2]));
                                preferences.flush();

                                break;
                        }
                    }
                    gameScreen.getGoldAndHighScoresIcons().setGold(preferences.getLong("gold"));
                    gameScreen.getGoldAndHighScoresIcons().setHighScore(preferences.getLong("highscore"));
                }
            }});
    }

}
