package com.ledzinygamedevelopment.fallingman.sprites.onearmbandit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class SpinsBackground extends Sprite {
    private TextureRegion oneArmedBanditTexture;
    private GameScreen gameScreen;
    private World world;
    private BodyDef bdef;
    private Body b2body;
    private FixtureDef fdef;

    public SpinsBackground(GameScreen gameScreen, World world, float posX, float posY) {
        this.gameScreen = gameScreen;
        this.world = world;
        oneArmedBanditTexture = new TextureRegion(gameScreen.getDefaultAtlas().findRegion("one_armed_bandit_spins_amount_background"), 0, 0, 956, 120);
        setBounds(0, 0, 956 / FallingMan.PPM, 120 / FallingMan.PPM);
        setRegion(oneArmedBanditTexture);
        setPosition(posX, posY);
    }

}
