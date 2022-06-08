package com.ledzinygamedevelopment.fallingman.sprites.onearmbandit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class OnePartRoll extends Sprite {

    private GameScreen gameScreen;
    private float width;
    private float height;
    private int currentTextureNumber;
    private boolean winOneArmedBanditScaleUp;

    public OnePartRoll(GameScreen gameScreen, float posX, float posY, float width, float height, int rollTexture) {
        this.gameScreen = gameScreen;
        this.width = width;
        this.height = height;


        setBounds(0, 0, width, height);
        currentTextureNumber = rollTexture;
        setRegion(gameScreen.getAtlas().findRegion("smallRoll" + rollTexture), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
        setOrigin(getWidth() / 2, getHeight() / 2);
        winOneArmedBanditScaleUp = true;
    }

    public boolean isWinOneArmedBanditScaleUp() {
        return winOneArmedBanditScaleUp;
    }

    public void setWinOneArmedBanditScaleUp(boolean winOneArmedBanditScaleUp) {
        this.winOneArmedBanditScaleUp = winOneArmedBanditScaleUp;
    }

    public int getCurrentTextureNumber() {
        return currentTextureNumber;
    }
}
