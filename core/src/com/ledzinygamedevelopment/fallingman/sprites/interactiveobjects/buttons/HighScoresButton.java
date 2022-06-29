package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class HighScoresButton extends Button{

    public HighScoresButton(GameScreen gameScreen, World world, float posX, float posY, float width, float height) {
        super(gameScreen, world, posX, posY, width, height);
        yPosPlayerDiff = 434;
        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("high_scores"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("high_scores_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("high_scores"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        gameScreen.setCurrentScreen(FallingMan.ONE_ARMED_BANDIT_SCREEN);
    }
}
