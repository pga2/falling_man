package com.ledzinygamedevelopment.fallingman.sprites.tutorial.playscreen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class TutorialThumb extends Sprite {

    private float timerScreenSideTouched;
    private float timerSizeIncrease;
    private PlayScreen playScreen;
    private World world;
    private float posX;
    private float posY;
    private float width;
    private float height;
    private boolean sizeUp;
    private boolean setRectToStartAnimation;

    public TutorialThumb(PlayScreen playScreen, World world, float posX, float posY, boolean left) {
        this.playScreen = playScreen;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.width = 300 / FallingMan.PPM;
        this.height = 600 / FallingMan.PPM;


        setBounds(0, 0, width, height);
        setRegion(playScreen.getDefaultAtlas().findRegion("thumb_tutorial"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
        if (left) {
            setFlip(true, false);
        } else {
            setOrigin(getWidth(), 0);
        }
        //setScale(3);
        timerSizeIncrease = 0f;
        timerScreenSideTouched = 0;
        sizeUp = false;
        setRectToStartAnimation = false;
    }

    public void update(float dt, float posY) {
        setPosition(getX(), posY);
        timerSizeIncrease += dt;
        if (timerSizeIncrease > 1) {
            if (sizeUp) {
                if (getScaleX() < 1) {
                    setScale(getScaleX() + 0.01f);
                } else {
                    setScale(1);
                    timerSizeIncrease = 0;
                    sizeUp = false;
                }
            } else {
                if (getScaleX() > 0.8) {
                    setScale(getScaleX() - 0.01f);
                } else {
                    if (timerScreenSideTouched < 0.2) {
                        setRectToStartAnimation = true;
                        timerScreenSideTouched += dt;
                    } else {
                        setRectToStartAnimation = false;
                        timerScreenSideTouched = 0;
                        sizeUp = true;
                    }
                }
            }
        }
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public boolean isSetRectToStartAnimation() {
        return setRectToStartAnimation;
    }

    public void setTimerSizeIncrease(float timerSizeIncrease) {
        this.timerSizeIncrease = timerSizeIncrease;
    }
}
