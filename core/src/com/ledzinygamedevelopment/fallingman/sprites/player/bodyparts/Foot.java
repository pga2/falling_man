package com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class Foot extends PlayerBodyPart {

    public Foot(World world, GameScreen playScreen, int texturePos, int sideOfBodyPart, int mapHeight, int spriteNumber) {
        super(world, playScreen, texturePos, sideOfBodyPart, mapHeight, spriteNumber);
    }

    @Override
    public void defineBodyPart() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 0.0075f;
        fdef.friction = 0.001f;
        fdef.restitution = 0.01f;

        PolygonShape shape = new PolygonShape();
        if(sideOfBodyPart == 0) {
            float[] shapeVertices = {-11 / FallingMan.PPM, 13 / FallingMan.PPM, 11 / FallingMan.PPM, 14 / FallingMan.PPM,
                    -11 / FallingMan.PPM, -13 / FallingMan.PPM, 11 / FallingMan.PPM, -13 / FallingMan.PPM};
            shape.set(shapeVertices);
        } else {
            float[] shapeVertices = {-11 / FallingMan.PPM, 14 / FallingMan.PPM, 11 / FallingMan.PPM, 13 / FallingMan.PPM,
                    -11 / FallingMan.PPM, -13 / FallingMan.PPM, 11 / FallingMan.PPM, -13 / FallingMan.PPM};
            shape.set(shapeVertices);
        }

        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.PLAYER_FOOT_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.STOP_WALKING_ENEMY_BIT | FallingMan.WALL_INSIDE_TOWER | FallingMan.ROCK_BIT
                 | FallingMan.PLAYER_SHIN_BIT;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
        b2body.setUserData(this);
        b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y, 3.14f);
        b2bodies.add(b2body);
    }
}
