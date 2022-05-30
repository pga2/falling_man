package com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class Foot extends PlayerBodyPart {

    public Foot(World world, PlayScreen playScreen, int texturePos, int sideOfBodyPart) {
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
        fdef.restitution = 1;

        PolygonShape shape = new PolygonShape();
        if(sideOfBodyPart == 0) {
            float[] shapeVertices = {-18 / FallingMan.PPM, 7 / FallingMan.PPM, 18 / FallingMan.PPM, 8 / FallingMan.PPM,
                    -18 / FallingMan.PPM, -7 / FallingMan.PPM, 18 / FallingMan.PPM, -7 / FallingMan.PPM};
            shape.set(shapeVertices);
        } else {
            float[] shapeVertices = {-18 / FallingMan.PPM, 8 / FallingMan.PPM, 18 / FallingMan.PPM, 7 / FallingMan.PPM,
                    -18 / FallingMan.PPM, -7 / FallingMan.PPM, 18 / FallingMan.PPM, -7 / FallingMan.PPM};
            shape.set(shapeVertices);
        }

        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.PLAYER_FOOT_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.DEAD_MACHINE_BIT | FallingMan.WALL_INSIDE_TOWER | FallingMan.ROCK_BIT
                 | FallingMan.PLAYER_SHIN_BIT;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y, 3.14f);
        b2bodies.add(b2body);
    }
}
