package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class MenuButton extends Button{
    public MenuButton(GameScreen gameScreen, World world, float posX, float posY, float width, float height) {
        super(gameScreen, world, posX, posY, width, height);

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("menu_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        super.touched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("menu_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
    }

    @Override
    public void notTouched() {
        super.notTouched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("menu_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        if(clicked) {
            gameScreen.setCurrentScreen(FallingMan.MENU_SCREEN);
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("menu_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));

    }
}
