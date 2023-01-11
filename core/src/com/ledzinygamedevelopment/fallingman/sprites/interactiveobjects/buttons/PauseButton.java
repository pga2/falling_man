package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects.Cloud;

public class PauseButton extends Button {


    private PlayScreen playScreen;

    public PauseButton(PlayScreen playScreen, World world, float posX, float posY) {
        super(playScreen, world, posX, posY);
        this.playScreen = playScreen;
        width = 144 / FallingMan.PPM;
        height = 144 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(playScreen.getDefaultAtlas().findRegion("pause_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        super.touched();
        setRegion(playScreen.getDefaultAtlas().findRegion("pause_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        super.notTouched();
        if (clicked && !playScreen.isChangeScreen()) {
            setRegion(playScreen.getDefaultAtlas().findRegion("pause_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));

            playScreen.setPause(true);
            playScreen.setPausePlayButton(new PlayButton(playScreen, world, FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM - 256 / 2f / FallingMan.PPM, playScreen.getHud().getHudBackground().getY()));
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(playScreen.getDefaultAtlas().findRegion("pause_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
    }
}
