package com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class Hand extends PlayerBodyPart {
    public Hand(World world, PlayScreen playScreen, int texturePos, int sideOfBodyPart) {
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
        fdef.restitution = 0.3f;

        PolygonShape shape = new PolygonShape();
            float[] shapeVertices = {-12 / FallingMan.PPM, 7 / FallingMan.PPM, 12 / FallingMan.PPM, 7 / FallingMan.PPM,
                    -12 / FallingMan.PPM, -7 / FallingMan.PPM, 12 / FallingMan.PPM, -7 / FallingMan.PPM};
            shape.set(shapeVertices);

        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.PLAYER_HAND_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.DEAD_MACHINE_BIT | FallingMan.PLAYER_FORE_ARM_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }
}
