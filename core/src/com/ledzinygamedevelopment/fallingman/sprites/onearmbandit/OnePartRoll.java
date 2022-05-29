package com.ledzinygamedevelopment.fallingman.sprites.onearmbandit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

import java.util.Random;

public class OnePartRoll extends Sprite {

    private PlayScreen playScreen;
    private float width;
    private float height;
    private int currentTextureNumber;
    private boolean winOneArmedBanditScaleUp;

    public OnePartRoll(PlayScreen playScreen, float posX, float posY, float width, float height, int rollTexture) {
        this.playScreen = playScreen;
        this.width = width;
        this.height = height;


        setBounds(0, 0, width, height);
        setRegion(playScreen.getAtlas().findRegion("smallRoll" + rollTexture), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
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
}
