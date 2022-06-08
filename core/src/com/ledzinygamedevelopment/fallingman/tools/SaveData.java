package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveData {

    private Preferences prefs;

    public SaveData() {
        prefs = Gdx.app.getPreferences("StickManFallingPreferences");
    }

    public void setHighScore(int highScore) {
        if (prefs.getInteger("highscore") < highScore) {
            prefs.putInteger("highscore", highScore);
            prefs.flush();
        }
    }

    public void addGold(int gold) {
        prefs.putInteger("gold", prefs.getInteger("gold") + gold);
        prefs.flush();
    }

    public int getGold() {
        return prefs.getInteger("gold");
    }

    public int getHighScore() {
        return prefs.getInteger("highscore");
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

}
