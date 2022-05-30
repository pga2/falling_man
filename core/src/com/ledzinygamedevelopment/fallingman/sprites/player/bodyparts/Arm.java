package com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts;

import static com.ledzinygamedevelopment.fallingman.FallingMan.WALL_INSIDE_TOWER;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
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
        fdef.restitution = 0.3f;

        PolygonShape shape = new PolygonShape();
        float[] shapeVertices = {-8 / FallingMan.PPM, 45 / FallingMan.PPM, 8 / FallingMan.PPM, 45 / FallingMan.PPM,
                -8 / FallingMan.PPM, -25 / FallingMan.PPM,
                0, -45 / FallingMan.PPM,
                8 / FallingMan.PPM, -25 / FallingMan.PPM};
        shape.set(shapeVertices);

        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.PLAYER_ARM_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.DEAD_MACHINE_BIT | FallingMan.WALL_INSIDE_TOWER | FallingMan.ROCK_BIT
                | FallingMan.PLAYER_BELLY_BIT | FallingMan.PLAYER_FORE_ARM_BIT;
        b2body.createFixture(fdef).setUserData(this);


        bdef = new BodyDef();
        bdef.position.set(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2bodyInvisible = world.createBody(bdef);

        fdef = new FixtureDef();
        fdef.density = 1f;
        fdef.friction = 0.01f;
        fdef.restitution = 1;

        shape = new PolygonShape();
        float[] shapeVertices2 = {-7 / FallingMan.PPM, 44 / FallingMan.PPM, 7 / FallingMan.PPM, 44 / FallingMan.PPM,
                -1 / FallingMan.PPM, -50 / FallingMan.PPM,
                1 / FallingMan.PPM, -50 / FallingMan.PPM};
        shape.set(shapeVertices2);

        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.INVISIBLE_BODY_PART_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.WALL_INSIDE_TOWER;
        b2bodyInvisible.createFixture(fdef).setUserData(this);

        WeldJointDef joint = new WeldJointDef();

        joint.localAnchorA.x = 0;
        joint.localAnchorB.x = 0;
        joint.localAnchorA.y = 0;
        joint.localAnchorB.y = 0;
        joint.bodyA = b2body;
        joint.bodyB = b2bodyInvisible;
        world.createJoint(joint);

        b2bodies.add(b2body);
        b2bodies.add(b2bodyInvisible);
    }
}
