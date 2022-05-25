package com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class Arm extends PlayerBodyPart {

    public Arm(World world, PlayScreen playScreen, int texturePos, int sideOfBodyPart) {
        super(world, playScreen, texturePos, sideOfBodyPart);
    }

    @Override
    public void defineBodyPart() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 0.3f;
        fdef.friction = 0.01f;
        fdef.restitution = 0.1f;

        PolygonShape shape = new PolygonShape();
        float[] shapeVertices = {-8 / FallingMan.PPM, 0 / FallingMan.PPM, 8 / FallingMan.PPM, 0 / FallingMan.PPM,
                -8 / FallingMan.PPM, -70 / FallingMan.PPM,
                0, -90 / FallingMan.PPM,
                8 / FallingMan.PPM, -70 / FallingMan.PPM};
        shape.set(shapeVertices);

        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.PLAYER_ARM_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.COIN_BIT | FallingMan.DEAD_MACHINE_BIT
                | FallingMan.PLAYER_BELLY_BIT | FallingMan.FORE_ARM_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }
}
