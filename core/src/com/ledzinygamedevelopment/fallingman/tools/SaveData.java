package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

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

    public long getMillis() {
        return prefs.getLong("time");
    }

}
