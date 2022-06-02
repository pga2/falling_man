package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class PlayButton extends Button{

    public PlayButton(GameScreen gameScreen, World world, float posX, float posY, float width, float height) {
        super(gameScreen, world, posX, posY, width, height);

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getAtlas().findRegion("play_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        setRegion(gameScreen.getAtlas().findRegion("play_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        setRegion(gameScreen.getAtlas().findRegion("play_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        if(clicked) {
            gameScreen.setLoadNewGame(true);
        }
    }
}
