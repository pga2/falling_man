package com.ledzinygamedevelopment.fallingman.screens.windows;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class DefaultWindow extends Sprite {

    private PlayScreen playScreen;
    private World world;
    private float width;
    private float height;
    /*private boolean clicked;
    private boolean locked;*/

    public DefaultWindow(PlayScreen playScreen, World world) {
        this.playScreen = playScreen;
        this.world = world;
        /*clicked = false;
        locked = false;*/

        width = 1312 / FallingMan.PPM;
        height = 2048 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(playScreen.getAtlas().findRegion("window"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition((FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM - width) / 2, playScreen.getPlayer().b2body.getPosition().y - height / 2);

    }
}
