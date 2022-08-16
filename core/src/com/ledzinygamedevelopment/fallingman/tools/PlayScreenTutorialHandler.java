package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.tutorial.playscreen.PlayScreenTutorialRect;
import com.ledzinygamedevelopment.fallingman.sprites.tutorial.playscreen.TutorialThumb;

public class PlayScreenTutorialHandler {

    private PlayScreenTutorialRect leftPlayScreenTutorialRect;
    private PlayScreenTutorialRect rightPlayScreenTutorialRect;
    private TutorialThumb leftThumbTutorial;
    private TutorialThumb rightThumbTutorial;
    private PlayScreen playScreen;
    private boolean tutorialOn;

    public PlayScreenTutorialHandler(PlayScreen playScreen, World world, float posY) {

        this.playScreen = playScreen;
        tutorialOn = true;

        leftPlayScreenTutorialRect = new PlayScreenTutorialRect(playScreen, world, 247 / FallingMan.PPM, posY);
        rightPlayScreenTutorialRect = new PlayScreenTutorialRect(playScreen, world, FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM + 247 / FallingMan.PPM, posY);

        leftThumbTutorial = new TutorialThumb(playScreen, world, 0, posY, true);
        leftThumbTutorial.setTimerSizeIncrease(0.4f);
        rightThumbTutorial = new TutorialThumb(playScreen, world, FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM - 300 / FallingMan.PPM, posY, false);

    }

    public void update(float dt, float playerPosY, float screenHeight) {
        if (tutorialOn) {
            leftPlayScreenTutorialRect.update(dt, playerPosY - (leftPlayScreenTutorialRect.getHeight() / 2));
            rightPlayScreenTutorialRect.update(dt, playerPosY - (rightPlayScreenTutorialRect.getHeight() / 2));
            leftThumbTutorial.update(dt, playerPosY - (screenHeight / 2));
            rightThumbTutorial.update(dt, playerPosY - (screenHeight / 2));
            if (leftThumbTutorial.isSetRectToStartAnimation()) {
                leftPlayScreenTutorialRect.setStartAnimation(true);
            } else {
                leftPlayScreenTutorialRect.setStartAnimation(false);
            }
            if (rightThumbTutorial.isSetRectToStartAnimation()) {
                rightPlayScreenTutorialRect.setStartAnimation(true);
            } else {
                rightPlayScreenTutorialRect.setStartAnimation(false);
            }
        }
    }

    public void draw(Batch batch) {
        if (tutorialOn) {
            leftPlayScreenTutorialRect.draw(batch);
            rightPlayScreenTutorialRect.draw(batch);
            leftThumbTutorial.draw(batch);
            rightThumbTutorial.draw(batch);
        }
    }

    public boolean isTutorialOn() {
        return tutorialOn;
    }

    public void setTutorialOn(boolean tutorialOn) {
        this.tutorialOn = tutorialOn;
    }
}
