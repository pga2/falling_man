package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.menu;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.MenuScreen;
import com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects.Cloud;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;

import java.util.Random;

public class OneArmedBanditScreenButton extends Button {


    private MenuScreen menuScreen;

    public OneArmedBanditScreenButton(MenuScreen gameScreen, World world, float posX, float posY) {
        super(gameScreen, world, posX, posY);
        menuScreen = gameScreen;
        width = 256 / FallingMan.PPM;
        height = 256 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("one_armed_bandit_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        super.touched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("one_armed_bandit_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        super.notTouched();
        if (clicked && !menuScreen.isChangeScreen()) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("one_armed_bandit_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            clicked = false;

            Random random = new Random();
            //currentScreen = FallingMan.MENU_SCREEN;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 26; j++) {
                    menuScreen.getClouds().add(new Cloud(menuScreen, ((i * 640) - random.nextInt(200)) / FallingMan.PPM, (menuScreen.getPlayer().getY() + menuScreen.getGamePort().getWorldHeight() / 2) + ((140 * j) - random.nextInt(21)) / FallingMan.PPM, false, FallingMan.ONE_ARMED_BANDIT_SCREEN));
                }
            }
            menuScreen.setChangeScreen(true);
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("one_armed_bandit_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
    }
}