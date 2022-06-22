package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class BigChest extends Sprite {
    private boolean secondStage;
    private TextureRegion bigChestTexture;
    private float posX;
    private float posY;
    private GameScreen gameScreen;
    private float growingTime;
    private boolean firstStage;
    private boolean rightDirection;
    private boolean touched;
    private float touchedTimer;
    private float thirdStageTimer;



    public BigChest(GameScreen gameScreen, float posX, float posY) {
        this.gameScreen = gameScreen;

        bigChestTexture = new TextureRegion(gameScreen.getAtlas().findRegion("bigChest"), 0, 0, 960, 960);
        setBounds(0, 0, 960 / FallingMan.PPM, 960 / FallingMan.PPM);
        setRegion(bigChestTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setPosition(posX - getWidth() / 2, posY - getHeight() / 2);
        setScale(0.20f);
        growingTime = 0;
        firstStage = true;
        secondStage = true;
        rightDirection = true;
        touched = false;
        touchedTimer = 0;
    }

    public void update(float dt) {
        if (growingTime + dt < 2.4f) {
            setScale(0.2f + growingTime / 3);
        } else if (firstStage) {
            firstStage = false;
            posX = getX();
            posY = getY();
            setScale(1);
        } else if (secondStage){
            if (Gdx.input.isTouched() && touchedTimer < 2) {
                touched = true;
                if (rightDirection) {
                    if (getX() - posX < 2) {
                        setPosition(getX() + 0.5f, getY());
                    } else {
                        rightDirection = false;
                    }
                } else {
                    if (posX - getX() < 2) {
                        setPosition(getX() - 0.5f, getY());
                    } else {
                        rightDirection = true;
                    }
                }
                touchedTimer += dt;
            } else if (Gdx.input.isTouched() && touched) {
                gameScreen.addCoinsFromChest(100);
                touched = false;
                secondStage = false;
            }
        } else if (thirdStageTimer < 0.5f){
            setScale(1 - thirdStageTimer * 2);
            thirdStageTimer += dt;
        } else {
            gameScreen.removeChest(this);
        }


            growingTime += dt;
    }
}
