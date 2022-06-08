package com.ledzinygamedevelopment.fallingman.sprites.onearmbandit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

import java.util.Random;

public class Roll extends Sprite {

    private GameScreen gameScreen;
    private World world;
    private float posX;
    private float posY;
    private float width;
    private float height;
    private boolean rollingCurrently;
    private int currentTextureNumber;

    public Roll(GameScreen gameScreen, World world, float posX, float posY, float width, float height) {
        this.gameScreen = gameScreen;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;


        setBounds(0, 0, width, height);
        currentTextureNumber = new Random().nextInt(4);
        setRegion(gameScreen.getAtlas().findRegion("roll" + currentTextureNumber), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
        rollingCurrently = false;
    }

    public Roll(GameScreen gameScreen, World world, float posX, float posY, float width, float height, int textureNumber) {
        this.gameScreen = gameScreen;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;


        setBounds(0, 0, width, height);
        currentTextureNumber = textureNumber;
        setRegion(gameScreen.getAtlas().findRegion("roll" + currentTextureNumber), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
        rollingCurrently = false;
    }

    public void setNewRegion(int regionNumber) {
        setRegion(gameScreen.getAtlas().findRegion("roll" + regionNumber), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
    }

    public int getCurrentTextureNumber() {
        return currentTextureNumber;
    }

    public void setCurrentTextureNumber(int currentTextureNumber) {
        this.currentTextureNumber = currentTextureNumber;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public boolean isRollingCurrently() {
        return rollingCurrently;
    }

    public void setRollingCurrently(boolean rollingCurrently) {
        this.rollingCurrently = rollingCurrently;
    }
}
