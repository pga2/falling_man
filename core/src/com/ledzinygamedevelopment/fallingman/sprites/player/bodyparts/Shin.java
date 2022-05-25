package com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class Shin extends PlayerBodyPart {
    public Shin(World world, PlayScreen playScreen, int texturePos, int sideOfBodyPart) {
        super(world, playScreen, texturePos, sideOfBodyPart);
    }

    @Override
    public void defineBodyPart() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 0.2f;
        fdef.friction = 0.01f;
        fdef.restitution = 0.1f;

        PolygonShape shape = new PolygonShape();
        float[] shapeVertices = {-8 / FallingMan.PPM, 0 / FallingMan.PPM, 8 / FallingMan.PPM, 0 / FallingMan.PPM,
                -8 / FallingMan.PPM, -65 / FallingMan.PPM,
                0, -70 / FallingMan.PPM,
                8 / FallingMan.PPM, -65 / FallingMan.PPM};
        shape.set(shapeVertices);

        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.PLAYER_SHIN_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.COIN_BIT | FallingMan.DEAD_MACHINE_BIT
                | FallingMan.PLAYER_THIGH_BIT | FallingMan.PLAYER_FOOT_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }
}
