package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.settings;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.SettingsScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

public class SoundsButton extends Button {

    private boolean soundsOn;
    private SettingsScreen settingsScreen;
    private SaveData saveData;

    public SoundsButton(SettingsScreen settingsScreen, World world, float posX, float posY, float width, float height) {
        super(settingsScreen, world, posX, posY, width, height);
        this.settingsScreen = settingsScreen;
        //yPosPlayerDiff = 434;
        setBounds(0, 0, width, height);
        saveData = settingsScreen.getSaveData();
        soundsOn = saveData.getSounds();
        if (soundsOn) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("sound_on"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        } else {
            setRegion(gameScreen.getDefaultAtlas().findRegion("sound_off"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        }
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        super.touched();
        if (soundsOn) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("sound_on_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        } else {
            setRegion(gameScreen.getDefaultAtlas().findRegion("sound_off_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        }
        clicked = true;
    }

    @Override
    public void notTouched() {
        super.notTouched();
        if (clicked) {
            if (soundsOn) {
                saveData.setSounds(false);
                soundsOn = false;
                setRegion(gameScreen.getDefaultAtlas().findRegion("sound_off"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            } else {
                saveData.setSounds(true);
                soundsOn = true;
                setRegion(gameScreen.getDefaultAtlas().findRegion("sound_on"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            }
            clicked = false;
        }
    }

    @Override
    public void restoreNotClickedTexture() {

        if (soundsOn) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("sound_on"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        } else {
            setRegion(gameScreen.getDefaultAtlas().findRegion("sound_off"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        }
    }
}
