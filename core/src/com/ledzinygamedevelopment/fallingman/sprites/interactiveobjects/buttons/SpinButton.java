package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class SpinButton extends Button{

    public SpinButton(PlayScreen playScreen, World world, float posX, float posY, float width, float height) {
        super(playScreen, world, posX, posY, width, height);

        setBounds(0, 0, width, height);
        setRegion(playScreen.getAtlas().findRegion("spin"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        setRegion(playScreen.getAtlas().findRegion("spin_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

//, 448, 7936, 544, 192
}
