package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.SettingsScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;

public class RateUsButton extends Button {


    public RateUsButton(SettingsScreen settingsScreen, World world, float posX, float posY, float width, float height) {
        super(settingsScreen, world, posX, posY, width, height);

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("rate_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        super.touched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("rate_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        super.notTouched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("rate_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        if(clicked) {
            Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.ledzinygamedevelopment.fallingman");
            clicked = false;
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("rate_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
    }
}
