package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.tutorial.PointingFinger;
import com.ledzinygamedevelopment.fallingman.sprites.tutorial.playscreen.PlayScreenTutorialRect;
import com.ledzinygamedevelopment.fallingman.sprites.tutorial.playscreen.TutorialThumb;

public class TutorialHandler {

    private PointingFinger pointingFinger;
    private boolean tutorialOn;
    private GameScreen gameScreen;

    public TutorialHandler(GameScreen gameScreen, World world, float posY) {

        this.gameScreen = gameScreen;
        tutorialOn = true;

        pointingFinger = new PointingFinger(gameScreen, world, FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM - 300 / FallingMan.PPM, posY, false);

    }

    public void update(float dt, float playerPosY, float screenHeight) {
        if (tutorialOn) {
            pointingFinger.update(dt, playerPosY - (pointingFinger.getHeight() / 2));
            /*if (leftThumbTutorial.isSetRectToStartAnimation()) {
                leftPlayScreenTutorialRect.setStartAnimation(true);
            } else {
                leftPlayScreenTutorialRect.setStartAnimation(false);
            }
            if (rightThumbTutorial.isSetRectToStartAnimation()) {
                rightPlayScreenTutorialRect.setStartAnimation(true);
            } else {
                rightPlayScreenTutorialRect.setStartAnimation(false);
            }*/
        }
    }

    public void draw(Batch batch) {
        if (tutorialOn) {
            pointingFinger.draw(batch);
        }
    }

    public boolean isTutorialOn() {
        return tutorialOn;
    }

    public void setTutorialOn(boolean tutorialOn) {
        this.tutorialOn = tutorialOn;
    }
}
