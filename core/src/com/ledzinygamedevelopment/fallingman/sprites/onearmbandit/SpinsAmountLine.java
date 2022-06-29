package com.ledzinygamedevelopment.fallingman.sprites.onearmbandit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class SpinsAmountLine extends Sprite {

    private TextureRegion oneArmedBanditTexture;
    private GameScreen gameScreen;
    private World world;
    private BodyDef bdef;
    private Body b2body;
    private FixtureDef fdef;

    public SpinsAmountLine(GameScreen gameScreen, World world, float posX, float posY) {
        this.gameScreen = gameScreen;
        this.world = world;
        oneArmedBanditTexture = new TextureRegion(gameScreen.getDefaultAtlas().findRegion("spins_amount_line"), 0, 0, 19, 120);
        setBounds(0, 0, 19 / FallingMan.PPM, 120 / FallingMan.PPM);
        setRegion(oneArmedBanditTexture);
        setPosition(posX, posY);
    }
}
