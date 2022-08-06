package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class WatchAdButton extends Button{

    public WatchAdButton(GameScreen gameScreen, World world, float posX, float posY) {
        super(gameScreen, world, posX, posY);

        width = 1024 / FallingMan.PPM;
        height = 256 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("watch_ad_button"), 0, 0, (int) (width * FallingMan.PPM),  (int) (height * FallingMan.PPM));
        setPosition(posX - getWidth() / 2, posY - getHeight() / 2);
        this.posX = getX();
        this.posY = getY();
    }

    @Override
    public void touched() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("watch_ad_button"), 0, 0, (int) (width * FallingMan.PPM),  (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("watch_ad_button"), 0, 0, (int) (width * FallingMan.PPM),  (int) (height * FallingMan.PPM));
        if(clicked) {
            gameScreen.watchAdButtonClicked();
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("watch_ad_button"), 0, 0, (int) (width * FallingMan.PPM),  (int) (height * FallingMan.PPM));

    }
}
