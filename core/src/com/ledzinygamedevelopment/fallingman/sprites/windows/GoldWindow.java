package com.ledzinygamedevelopment.fallingman.sprites.windows;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class GoldWindow extends Sprite {
    private PlayScreen playScreen;
    private World world;
    private BodyDef bdef;
    private Body b2body;
    private FixtureDef fdef;

    public GoldWindow(PlayScreen playScreen, World world, float posY) {
        //super(gameScreen.getAtlas().findRegion("one_armed_bandit"));
        this.playScreen = playScreen;
        this.world = world;
        //oneArmedBanditTexture = new TextureRegion(getTexture(), 1315, 3033, 1056, 1040);
        setRegion(playScreen.getWindowAtlas().findRegion("gold"), 0, 0, 732, 350);

        setBounds(0, 0, 732 / FallingMan.PPM, 350 / FallingMan.PPM);

        setPosition(354 / FallingMan.PPM, posY);
    }
}