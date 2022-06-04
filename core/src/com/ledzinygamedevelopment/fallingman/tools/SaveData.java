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
    }

    public int getGold() {
        return prefs.getInteger("gold");
    }

    public int getHighScore() {
        return prefs.getInteger("highscore");
    }

}
