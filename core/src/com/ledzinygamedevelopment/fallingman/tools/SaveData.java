package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SaveData {

    private Preferences prefs;
    private boolean sounds;
    private boolean music;
    private boolean vibrations;
    private boolean tutorial;

    public SaveData() {
        prefs = Gdx.app.getPreferences("StickManFallingPreferences");
        sounds = prefs.getBoolean("sounds");
        music = prefs.getBoolean("music");
        vibrations = prefs.getBoolean("vibrations");
        tutorial = prefs.getBoolean("tutorial");
    }

    public void notFirstAppUse() {
        if (!prefs.getBoolean("notFirstAppUse")) {
            Array<String> allBodyPartsNames = new Array<>();
            allBodyPartsNames.add("head");
            allBodyPartsNames.add("belly");
            allBodyPartsNames.add("armL");
            allBodyPartsNames.add("foreArmL");
            allBodyPartsNames.add("handL");
            allBodyPartsNames.add("armR");
            allBodyPartsNames.add("foreArmR");
            allBodyPartsNames.add("handR");
            allBodyPartsNames.add("thighL");
            allBodyPartsNames.add("shinL");
            allBodyPartsNames.add("footL");
            allBodyPartsNames.add("thighR");
            allBodyPartsNames.add("shinR");
            allBodyPartsNames.add("footR");
            Random random = new Random();
            for (String bodyPartName : allBodyPartsNames) {
                prefs.putInteger("owned0" + bodyPartName, random.nextInt(10000) + 101);
            }
            prefs.putBoolean("notFirstAppUse", true);
            prefs.putLong("saveCounter", 0);
            prefs.putBoolean("saveUpdated", true);
            prefs.putBoolean("sounds", true);
            prefs.putBoolean("music", true);
            prefs.putBoolean("vibrations", false);
            prefs.putBoolean("tutorial", true);
            prefs.putInteger("tutorialCounter", 0);
            prefs.flush();
        }
    }

    public void setHighScore(long highScore) {
        if (prefs.getLong("highscore") < highScore) {
            prefs.putLong("highscore", highScore);
            if (!prefs.getBoolean("saveUpdated")) {
                prefs.putBoolean("saveUpdated", true);
                prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
            }
            prefs.flush();
        }
    }

    public void addGold(long gold) {
        prefs.putLong("gold", prefs.getLong("gold") + gold);
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public void removeGold(long gold) {
        prefs.putLong("gold", prefs.getLong("gold") - gold);
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
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
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public void removeSpin() {
        int spins = prefs.getInteger("spins");
        if (spins > 0) {
            if (!prefs.getBoolean("saveUpdated")) {
                prefs.putBoolean("saveUpdated", true);
                prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
            }
            prefs.putInteger("spins", spins - 1);
            prefs.flush();
        }
    }

    public int getNumberOfSpins() {
        return prefs.getInteger("spins");
    }

    public void setMillis(long millis) {
        prefs.putLong("time", millis);
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public void setDayDailyReward(long days) {
        prefs.putLong("timeDailyReward", days);
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public HashMap<String, Integer> getBodySpritesCurrentlyWear() {
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

    public void saveCurrentBodyPartSprite(String name, int number) {
        prefs.putInteger(name, number);
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public HashMap<String, Boolean> getBodySpritesOwned() {
        HashMap<String, Boolean> bodySpritesOwned = new HashMap<>();
        Array<String> allBodyPartsNames = new Array<>();
        allBodyPartsNames.add("head");
        allBodyPartsNames.add("belly");
        allBodyPartsNames.add("armL");
        allBodyPartsNames.add("foreArmL");
        allBodyPartsNames.add("handL");
        allBodyPartsNames.add("armR");
        allBodyPartsNames.add("foreArmR");
        allBodyPartsNames.add("handR");
        allBodyPartsNames.add("thighL");
        allBodyPartsNames.add("shinL");
        allBodyPartsNames.add("footL");
        allBodyPartsNames.add("thighR");
        allBodyPartsNames.add("shinR");
        allBodyPartsNames.add("footR");
        for (int i = 0; i <= FallingMan.ALL_BODY_SPRITES_LENGHT; i++) {
            for (String bodyPartName : allBodyPartsNames) {
                bodySpritesOwned.put("owned" + i + bodyPartName, prefs.getInteger("owned" + i + bodyPartName) >= 100);
            }
        }
        return bodySpritesOwned;
    }

    public void saveBodySpritesOwned(HashMap<String, Boolean> bodySpritesOwned) {
        Random random = new Random();
        for (String key : bodySpritesOwned.keySet()) {
            if (!prefs.getBoolean("saveUpdated")) {
                prefs.putBoolean("saveUpdated", true);
                prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
            }
            prefs.putInteger(key, bodySpritesOwned.get(key) ? random.nextInt(10000) + 101 : random.nextInt(99));
        }
        prefs.flush();
    }

    public long getMillis() {
        return prefs.getLong("time");
    }

    public long getDayDailyReward() {
        return prefs.getLong("timeDailyReward");
    }

    //is set to true every time save file was updated
    public Boolean getSaveUpdated() {
        return prefs.getBoolean("saveUpdated");
    }

    public Long getSaveCounter() {
        return prefs.getLong("saveCounter");
    }

    //is set to true every time save file was updated, set to false after uploading save to cloud
    public void setSaveUpdated(Boolean saveUpdated) {
        prefs.putBoolean("saveUpdated", saveUpdated);
        prefs.flush();
    }

    public void saveTestString(String testString) {
        prefs.putString("testString", testString);
        prefs.flush();
    }

    public String getTestString() {
        return prefs.getString("testString");
    }

    public boolean getSounds() {
        return prefs.getBoolean("sounds");
    }
    public boolean getMusic() {
        return prefs.getBoolean("music");
    }
    public boolean getVibrations() {
        return prefs.getBoolean("vibrations");
    }
    public boolean getTutorial() {
        return prefs.getBoolean("tutorial");
    }

    public void setSounds(boolean value) {
        this.sounds = value;
        prefs.putBoolean("sounds", value);
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public void setMusic(boolean value) {
        this.music = value;
        prefs.putBoolean("music", value);
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public void setVibrations(boolean value) {
        this.vibrations = value;
        prefs.putBoolean("vibrations", value);
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public void setTutorial(boolean value) {
        this.tutorial = value;
        prefs.putBoolean("tutorial", value);
        if (prefs.getInteger("tutorialCounter") <=3) {
            prefs.putInteger("tutorialCounter", 4);
        }
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public void addOneToTutorialCounter() {
        int tutorialCounter = prefs.getInteger("tutorialCounter");
        prefs.putInteger("tutorialCounter", tutorialCounter + 1);
        if (tutorialCounter == 3 && tutorial) {
            prefs.putBoolean("tutorial", false);
        }
        if (!prefs.getBoolean("saveUpdated")) {
            prefs.putBoolean("saveUpdated", true);
            prefs.putLong("saveCounter", prefs.getLong("saveCounter") + 1);
        }
        prefs.flush();
    }

    public void setDayInRowDailyReward(long value) {
        prefs.putLong("daysInRow", value);
        prefs.flush();
    }

    public long getDaysInRowDailyReward() {
        return prefs.getLong("daysInRow");
    }
}
