/*
package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class BigWin {
    private TextureRegion bigChestTexture;
    private GameScreen gameScreen;
    private float growingTime;
    private boolean firstStage;


    public BigWin(GameScreen gameScreen, float posX, float posY) {
        this.gameScreen = gameScreen;

        bigChestTexture = new TextureRegion(gameScreen.getAtlas().findRegion("bigChest"), 0, 0, 960, 960);
        setBounds(0, 0, 960 / FallingMan.PPM, 960 / FallingMan.PPM);
        setRegion(bigChestTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setPosition(posX - getWidth() / 2, posY - getHeight() / 2);
        setScale(0.20f);
        growingTime = 0;
        firstStage = true;
    }
}
*/
