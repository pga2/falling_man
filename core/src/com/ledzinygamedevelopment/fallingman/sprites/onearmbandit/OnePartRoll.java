package com.ledzinygamedevelopment.fallingman.sprites.onearmbandit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

import java.util.Random;

public class OnePartRoll extends Sprite {

    private GameScreen gameScreen;
    private float width;
    private float height;
    private int currentTextureNumber;
    private boolean winOneArmedBanditScaleUp;
    private float flyingTime;
    private Vector3 flyingDirection;
    private boolean toRemove;
    private float endAnimationTime;
    private long Amount;

    public OnePartRoll(GameScreen gameScreen, float posX, float posY, float width, float height, int rollTexture) {
        this.gameScreen = gameScreen;
        this.width = width;
        this.height = height;


        setBounds(0, 0, width, height);
        currentTextureNumber = rollTexture;
        setRegion(gameScreen.getDefaultAtlas().findRegion("smallRoll" + rollTexture), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
        setOrigin(getWidth() / 2, getHeight() / 2);
        winOneArmedBanditScaleUp = true;
        flyingTime = 0;
        toRemove = false;
    }

    public void flyingRollUpdate(float dt, Vector2 lastDirection, SaveData saveData) {
        if (flyingTime < 0.25f) {
            setPosition(getX() + flyingDirection.x, getY() + flyingDirection.y);
            setRotation(getRotation() + flyingDirection.z);
            if (currentTextureNumber == 1) {
                setScale((4 - flyingTime) / 4);
            }
        } else if (flyingTime <= endAnimationTime){
            setPosition(getX() + (lastDirection.x - getWidth() / 2 - getX()) * dt, getY() + (lastDirection.y - getHeight() / 4 - getY()) * dt);
            setRotation(getRotation() + flyingDirection.z * 2);
            if (currentTextureNumber == 1) {
                setScale((4 - flyingTime) / 4);
            }
        } else {
            toRemove = true;
        }
        flyingTime += dt;
    }

    public void startFlying() {
        Random random = new Random();
        flyingDirection = new Vector3(random.nextFloat() / 1.5f - 0.3333f, random.nextFloat() / 1.5f - 0.3333f, (random.nextFloat() * 12 - 6));
        endAnimationTime = random.nextFloat() + 1.5f;
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

    public boolean isToRemove() {
        return toRemove;
    }

    public long getAmount() {
        return Amount;
    }

    public void setAmount(long amount) {
        Amount = amount;
    }
}
