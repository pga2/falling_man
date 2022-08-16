package com.ledzinygamedevelopment.fallingman.sprites.tutorial.playscreen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class PlayScreenTutorialRect extends Sprite {

    private float timerSizeIncrease;
    private PlayScreen playScreen;
    private World world;
    private float posX;
    private float posY;
    private float width;
    private float height;
    private boolean sizeUp;
    private boolean startAnimation;

    public PlayScreenTutorialRect(PlayScreen playScreen, World world, float posX, float posY) {
        this.playScreen = playScreen;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.width = 226 / FallingMan.PPM;
        this.height = 800 / FallingMan.PPM;


        setBounds(0, 0, width, height);
        setRegion(playScreen.getDefaultAtlas().findRegion("play_screen_tutorial_rect"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setOrigin(getWidth() / 2, getHeight() / 2);
        setPosition(posX, posY);
        setScale(3);
        startAnimation = false;
        /*timerSizeIncrease = 0f;*/
        sizeUp = false;
    }

    public void update(float dt, float posY) {
        setPosition(getX(), posY);
        /*timerSizeIncrease += dt;
        if (timerSizeIncrease > 1) {
            if (sizeUp) {
                if (getScaleX() < 3.1) {
                    setScale(getScaleX() + 0.005f);
                } else {
                    sizeUp = false;
                }
            } else {
                if (getScaleX() > 3) {
                    setScale(getScaleX() - 0.005f);
                } else {
                    setScale(3);
                    timerSizeIncrease = 0;
                    sizeUp = true;
                }
            }
        }*/
        if (startAnimation) {
            setScale(getScaleX() - 0.01f);
        } else {
            if (getScaleX() < 3) {
                setScale(getScaleX() + 0.01f);
            } else {
                setScale(3);
            }
        }
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setStartAnimation(boolean startAnimation) {
        this.startAnimation = startAnimation;
    }
}
