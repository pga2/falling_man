package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.ad;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class AdIcon extends Sprite {

    private float posX;
    private float posY;
    private float width;
    private float height;
    private boolean scaleUp;

    public AdIcon(GameScreen gameScreen, float posX, float posY) {
        width = 192 / FallingMan.PPM;
        height = 192 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("ad_icon"), 0, 0, (int) (width * FallingMan.PPM),  (int) (height * FallingMan.PPM));
        setPosition(posX - getWidth() / 2, posY - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);
        this.posX = getX();
        this.posY = getY();
        scaleUp = true;
    }

    public boolean isScaleUp() {
        return scaleUp;
    }

    public void setScaleUp(boolean scaleUp) {
        this.scaleUp = scaleUp;
    }
}
