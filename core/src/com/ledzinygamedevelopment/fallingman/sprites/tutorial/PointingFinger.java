package com.ledzinygamedevelopment.fallingman.sprites.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class PointingFinger extends Sprite {

    private float moveFingerTimer;
    private boolean moveFinger;
    private float timerScreenSideTouched;
    private float timerSizeIncrease;
    private GameScreen gameScreen;
    private World world;
    private float posX;
    private float posY;
    private float width;
    private float height;
    private boolean sizeUp;
    private boolean setRectToStartAnimation;

    private enum STATE {WAIT, SIZEDOWN, SIZEUPANDMOVEFROMSCREEN, MOVEDOWN, TELEPORTUP, MOVETOSCREEN, MOVEFROMSCREEN}

    private STATE currentState;
    private float screenMiddlePosYDiff;
    private float waitTimer;
    private Sprite fingerMark;

    public PointingFinger(GameScreen gameScreen, World world, float posX, float posY, boolean left) {
        this.gameScreen = gameScreen;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.width = 300 / FallingMan.PPM;
        this.height = 600 / FallingMan.PPM;


        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("pointing_finger"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
        if (left) {
            setFlip(true, false);
        } else {
            setOrigin(getWidth(), 0);
        }
        moveFinger = false;
        //setScale(3);
        timerSizeIncrease = 0f;
        timerScreenSideTouched = 0;
        sizeUp = false;
        setRectToStartAnimation = false;
        moveFingerTimer = 0;
        currentState = STATE.SIZEDOWN;
        screenMiddlePosYDiff = 0;
        waitTimer = 0;
        fingerMark = new Sprite();
        fingerMark.setRegion(gameScreen.getDefaultAtlas().findRegion("touched_point"), 0, 0, 128, 128);
        fingerMark.setBounds(0, 0, 128 / FallingMan.PPM, 128 / FallingMan.PPM);
        fingerMark.setOrigin(fingerMark.getWidth(), 0);
    }

    public void update(float dt, float posY) {
        switch (currentState) {
            case SIZEDOWN:
                if (getScaleX() > 0.8) {
                    setPosition(getX(), posY);
                    setScale(getScaleX() - 0.01f);
                } else {
                    setPosition(getX(), posY);
                    currentState = STATE.MOVEDOWN;
                    fingerMark.setPosition(((getX() + 50 / FallingMan.PPM) + 250 / FallingMan.PPM * (1 - getScaleX())) - fingerMark.getWidth() / 2, ((getY() + 595 / FallingMan.PPM) - 595 / FallingMan.PPM * (1 - getScaleY())) - fingerMark.getHeight() / 2);
                }
                break;
            case MOVEDOWN:
                if (getY() > posY - 600 / FallingMan.PPM) {
                    screenMiddlePosYDiff -= 0.1;
                    setPosition(getX(), posY + screenMiddlePosYDiff);
                } else {
                    setPosition(getX(), posY + screenMiddlePosYDiff);
                    currentState = STATE.SIZEUPANDMOVEFROMSCREEN;
                }
                break;
            case SIZEUPANDMOVEFROMSCREEN:
                boolean readySize = false;
                boolean readyXPos = false;
                if (getScaleX() < 1) {
                    screenMiddlePosYDiff -= 0.07f;
                    setPosition(getX(), posY + screenMiddlePosYDiff);
                    setScale(getScaleX() + 0.01f);
                } else {
                    setPosition(getX(), posY + screenMiddlePosYDiff);
                    setScale(1);
                    readySize = true;
                }
                if (getX() < FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM) {
                    setPosition(getX() + 0.1f, posY + screenMiddlePosYDiff);
                } else {
                    setPosition(FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, posY + screenMiddlePosYDiff);
                    readyXPos = true;
                }
                if (readySize && readyXPos) {
                    currentState = STATE.WAIT;
                }
                break;
            case WAIT:
                if (waitTimer < 2) {
                    waitTimer += dt;
                } else {
                    currentState = STATE.TELEPORTUP;
                    waitTimer = 0;
                }
                break;
            case TELEPORTUP:
                setPosition(getX(), posY);
                screenMiddlePosYDiff = 0;
                currentState = STATE.MOVETOSCREEN;
                break;
            case MOVETOSCREEN:
                if (getX() > FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM - 300 / FallingMan.PPM) {
                    setPosition(getX() - 0.1f, posY);
                } else {
                    setPosition(FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM - 300 / FallingMan.PPM, posY);
                    currentState = STATE.SIZEDOWN;
                }
                break;

        }
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    @Override
    public void draw(Batch batch) {
        if (currentState == STATE.MOVEDOWN) {
            fingerMark.setPosition(((getX() + 50 / FallingMan.PPM) + 250 / FallingMan.PPM * (1 - getScaleX())) - fingerMark.getWidth() / 2.3f, ((getY() + 595 / FallingMan.PPM) - 595 / FallingMan.PPM * (1 - getScaleY())) - fingerMark.getHeight() / 1.2f);
            fingerMark.draw(batch);
        }
        super.draw(batch);
    }
}
