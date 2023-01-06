package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.menu;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects.Cloud;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;

import java.util.Random;

public class ReturnMenuButton  extends Button {


    private GameScreen gameScreen;

    public ReturnMenuButton(GameScreen gameScreen, World world, float posX, float posY) {
        super(gameScreen, world, posX, posY);
        this.gameScreen = gameScreen;
        width = 256 / FallingMan.PPM;
        height = 256 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("return_menu_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        super.touched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("return_menu_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        super.notTouched();
        if (clicked && !gameScreen.isChangeScreen()) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("return_menu_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            clicked = false;

            Random random = new Random();
            //currentScreen = FallingMan.MENU_SCREEN;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 26; j++) {
                    gameScreen.getClouds().add(new Cloud(gameScreen, ((i * 640) - random.nextInt(220)) / FallingMan.PPM, ((-150 * j) - random.nextInt(21)) / FallingMan.PPM, false, FallingMan.MENU_SCREEN));
                }
            }
            gameScreen.setChangeScreen(true);
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("return_menu_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
    }
}
