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
    private float posX;
    private float posY;
    private float width;
    private float height;
    private int currentTextureNumber;
    private boolean winOneArmedBanditScaleUp;
    private float flyingTime;
    private Vector3 flyingDirection;
    private boolean toRemove;
    private float endAnimationTime;
    private long Amount;
    private boolean firstTimeStageTwo;
    private float firstStageTimer;

    public OnePartRoll(GameScreen gameScreen, float posX, float posY, float width, float height, int rollTexture) {
        this.gameScreen = gameScreen;
        this.posX = posX;
        this.posY = posY;
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
        firstTimeStageTwo = true;
        firstStageTimer = 0.75f;
    }

    public void flyingRollUpdate(float dt, Vector2 lastDirection, SaveData saveData) {
        if (flyingTime < firstStageTimer) {
            setPosition(getX() + flyingDirection.x / 3, getY() + flyingDirection.y / 3);
            setRotation(getRotation() + flyingDirection.z);
            if (currentTextureNumber == 1) {
                setScale((4 - flyingTime) / 4);
            }
        } else if (flyingTime <= endAnimationTime){
            if (firstTimeStageTwo) {
                posX = getX();
                posY = getY();
                firstTimeStageTwo = false;
            }
            setPosition(posX + (lastDirection.x - posX) * ((flyingTime - firstStageTimer) / (endAnimationTime - firstStageTimer)) , posY + (lastDirection.y - posY) * ((flyingTime - firstStageTimer) / (endAnimationTime - firstStageTimer)));
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
        flyingDirection = new Vector3(random.nextFloat() / 3f - 0.16666f, random.nextFloat() / 3f - 0.16666f, (random.nextFloat() * 12 - 6));
        endAnimationTime = random.nextFloat() / 3 + 1f;
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

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosY() {
        return posY;
    }
}
