package com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;

public abstract class PlayerBodyPart extends Sprite {
    protected TextureRegion bodyPartTexture;
    protected World world;
    protected int sideOfBodyPart;
    protected Body b2body;

    public PlayerBodyPart(World world, PlayScreen playScreen, int texturePos, int sideOfBodyPart) {
        super(playScreen.getAtlas().findRegion("player"));
        this.world = world;
        this.sideOfBodyPart = sideOfBodyPart;
        defineBodyPart();
        bodyPartTexture = new TextureRegion(getTexture(), 96 * texturePos, 0, 96, 96);
        setBounds(0, 0, 96 / FallingMan.PPM, 96 / FallingMan.PPM);
        setRegion(bodyPartTexture);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public abstract void defineBodyPart();

    public Body getB2body() {
        return b2body;
    }

    public int getSideOfBodyPart() {
        return sideOfBodyPart;
    }
}
