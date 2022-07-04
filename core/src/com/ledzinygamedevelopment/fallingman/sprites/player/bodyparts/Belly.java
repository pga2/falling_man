package com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class Belly extends PlayerBodyPart {


    public Belly(World world, GameScreen gameScreen, int texturePos, int sideOfBodyPart, int mapHeight, int spriteNumber) {
        super(world, gameScreen, texturePos, sideOfBodyPart, mapHeight, spriteNumber);
    }

    @Override
    public void defineBodyPart() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 0.05f;
        fdef.friction = 0.001f;
        fdef.restitution = 0.03f;

        PolygonShape shape = new PolygonShape();
        float[] shapeVertices = {-23 / FallingMan.PPM, 50 / FallingMan.PPM,
                -26 / FallingMan.PPM, 60 / FallingMan.PPM,
                26 / FallingMan.PPM, 60 / FallingMan.PPM,
                23 / FallingMan.PPM, 50 / FallingMan.PPM,
                -30 / FallingMan.PPM, -40 / FallingMan.PPM,
                -20 / FallingMan.PPM, -60 / FallingMan.PPM, 20 / FallingMan.PPM, -60 / FallingMan.PPM,
                30 / FallingMan.PPM, -40 / FallingMan.PPM};
        shape.set(shapeVertices);

        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.PLAYER_BELLY_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.DEAD_MACHINE_BIT | FallingMan.WALL_INSIDE_TOWER | FallingMan.ROCK_BIT
                | FallingMan.PLAYER_ARM_BIT | FallingMan.PLAYER_HEAD_BIT | FallingMan.PLAYER_THIGH_BIT;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
        b2body.setUserData(this);



        bdef = new BodyDef();
        bdef.position.set(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2bodyInvisible = world.createBody(bdef);

        fdef = new FixtureDef();
        fdef.density = 0.0001f;
        fdef.friction = 0.001f;
        fdef.restitution = 1f;

        shape = new PolygonShape();
        float[] shapeVertices2 = {-35 / FallingMan.PPM, 50 / FallingMan.PPM,
                -5 / FallingMan.PPM, 80 / FallingMan.PPM,
                5 / FallingMan.PPM, 80 / FallingMan.PPM,
                35 / FallingMan.PPM, 50 / FallingMan.PPM,
                -1 / FallingMan.PPM, -40 / FallingMan.PPM,
                -20 / FallingMan.PPM, -70 / FallingMan.PPM, 20 / FallingMan.PPM, -70 / FallingMan.PPM,
                1 / FallingMan.PPM, -40 / FallingMan.PPM};
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
