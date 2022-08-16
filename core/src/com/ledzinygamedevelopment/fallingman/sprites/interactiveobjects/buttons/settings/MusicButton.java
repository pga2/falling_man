package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.settings;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.SettingsScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

public class MusicButton extends Button {

    private boolean musicOn;
    private SettingsScreen settingsScreen;
    private SaveData saveData;

    public MusicButton(SettingsScreen settingsScreen, World world, float posX, float posY, float width, float height) {
        super(settingsScreen, world, posX, posY, width, height);
        this.settingsScreen = settingsScreen;
        //yPosPlayerDiff = 434;
        setBounds(0, 0, width, height);
        saveData = settingsScreen.getSaveData();
        musicOn = saveData.getMusic();
        if (musicOn) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("music_on"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        } else {
            setRegion(gameScreen.getDefaultAtlas().findRegion("music_off"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        }
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        super.touched();
        if (musicOn) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("music_on_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        } else {
            setRegion(gameScreen.getDefaultAtlas().findRegion("music_off_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        }
        clicked = true;
    }

    @Override
    public void notTouched() {
        super.notTouched();
        if (clicked) {
            if (musicOn) {
                saveData.setMusic(false);
                musicOn = false;
                setRegion(gameScreen.getDefaultAtlas().findRegion("music_off"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            } else {
                saveData.setMusic(true);
                musicOn = true;
                setRegion(gameScreen.getDefaultAtlas().findRegion("music_on"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            }
            clicked = false;
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        //setRegion(gameScreen.getDefaultAtlas().findRegion("high_scores"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
    }
}
