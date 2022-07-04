package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;

public class SaveData {

    private Preferences prefs;

    public SaveData() {
        prefs = Gdx.app.getPreferences("StickManFallingPreferences");
    }

    public void setHighScore(long highScore) {
        if (prefs.getLong("highscore") < highScore) {
            prefs.putLong("highscore", highScore);
            prefs.flush();
        }
    }

    public void addGold(long gold) {
        prefs.putLong("gold", prefs.getLong("gold") + gold);
        prefs.flush();
    }

    public long getGold() {
        return prefs.getLong("gold");
    }

    public long getHighScore() {
        return prefs.getLong("highscore");
    }

    public void addSpins(int numberOfSpins) {
        prefs.putInteger("spins", prefs.getInteger("spins") + numberOfSpins);
        prefs.flush();
    }

    public void removeSpin() {
        int spins = prefs.getInteger("spins");
        if (spins > 0) {
            prefs.putInteger("spins", spins - 1);
            prefs.flush();
        }
    }

    public int getNumberOfSpins() {
        return prefs.getInteger("spins");
    }

    public void setMillis(long millis) {
        prefs.putLong("time", millis);
        prefs.flush();
    }

    public HashMap<String, Integer> getBodySprites() {
        HashMap<String, Integer> allBodyPartsSprites = new HashMap<>();
        allBodyPartsSprites.put("head", prefs.getInteger("head"));
        allBodyPartsSprites.put("belly", prefs.getInteger("belly"));
        allBodyPartsSprites.put("armL", prefs.getInteger("armL"));
        allBodyPartsSprites.put("foreArmL", prefs.getInteger("foreArmL"));
        allBodyPartsSprites.put("handL", prefs.getInteger("handL"));
        allBodyPartsSprites.put("armR", prefs.getInteger("armR"));
        allBodyPartsSprites.put("foreArmR", prefs.getInteger("foreArmR"));
        allBodyPartsSprites.put("handR", prefs.getInteger("handR"));
        allBodyPartsSprites.put("thighL", prefs.getInteger("thighL"));
        allBodyPartsSprites.put("shinL", prefs.getInteger("shinL"));
        allBodyPartsSprites.put("footL", prefs.getInteger("footL"));
        allBodyPartsSprites.put("thighR", prefs.getInteger("thighR"));
        allBodyPartsSprites.put("shinR", prefs.getInteger("shinR"));
        allBodyPartsSprites.put("footR", prefs.getInteger("footR"));
        for (String key : allBodyPartsSprites.keySet()) {
            if (allBodyPartsSprites.get(key) == 0) {
                prefs.putInteger(key, allBodyPartsSprites.get(key));
                prefs.flush();
            }
        }
        return allBodyPartsSprites;
    }

    public long getMillis() {
        return prefs.getLong("time");
    }

}
