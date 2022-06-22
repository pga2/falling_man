package com.ledzinygamedevelopment.fallingman.sprites.fallingfromwallsobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

import java.util.Random;

public class Rock extends Sprite {

    private Random random;
    private GameScreen gameScreen;
    private World world;
    private Body b2body;
    private int radius;

    public Rock(GameScreen gameScreen, World world) {
        this.gameScreen = gameScreen;
        this.world = world;

        random = new Random();
        int chosenSprite = random.nextInt(10);

        if (chosenSprite == 0) {
            chosenSprite = 0;
        } else if (chosenSprite < 8) {
            chosenSprite = 1;
        } else {
            chosenSprite = 2;
        }
        switch (chosenSprite) {
            case 0:
                setBounds(0, 0, 48 / FallingMan.PPM, 48 / FallingMan.PPM);
                setRegion(gameScreen.getAtlas().findRegion("rock48"), 0, 0, 48, 48);
                break;
            case 1:
                setBounds(0, 0, 96 / FallingMan.PPM, 96 / FallingMan.PPM);
                setRegion(gameScreen.getAtlas().findRegion("rock96"), 0, 0, 96, 96);
                break;
            default:
                setBounds(0, 0, 160 / FallingMan.PPM, 160 / FallingMan.PPM);
                setRegion(gameScreen.getAtlas().findRegion("rock160"), 0, 0, 160, 160);
                break;
        }

        setOrigin(getWidth() / 2, getHeight() / 2);
        defineRock(chosenSprite);
    }

    public Rock(GameScreen gameScreen, World world, boolean bigRock) {
        this.gameScreen = gameScreen;
        this.world = world;
        random = new Random();

        setBounds(0, 0, 1056 / FallingMan.PPM, 1056 / FallingMan.PPM);
        setRegion(gameScreen.getAtlas().findRegion("rock1056"), 0, 0, 1056, 1056);
        setOrigin(getWidth() / 2, getHeight() / 2);
        defineRock(11);
    }

    public void update(float dt, Vector2 playerPos, boolean stopRocks) {
        if (stopRocks) {
            if (b2body.getLinearVelocity().x > 5) {
                b2body.setLinearVelocity(new Vector2(5, b2body.getLinearVelocity().y));
            } else if (b2body.getLinearVelocity().x < -5) {
                b2body.setLinearVelocity(new Vector2(-5, b2body.getLinearVelocity().y));
            }
            if (b2body.getPosition().y < playerPos.y + (800 + radius) / FallingMan.PPM) {
                if (b2body.getLinearVelocity().y < 0) {
                    b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 0));
                }
                b2body.applyLinearImpulse(new Vector2(b2body.getLinearVelocity().x, 10f), b2body.getWorldCenter(), true);
            } else if (b2body.getPosition().y < playerPos.y + (1100 + radius) / FallingMan.PPM) {
                if (b2body.getLinearVelocity().y < -5) {
                    b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, -5));
                }
                b2body.applyLinearImpulse(new Vector2(b2body.getLinearVelocity().x, 10f), b2body.getWorldCenter(), true);
            } else if (b2body.getPosition().y > playerPos.y + 1900 / FallingMan.PPM) {
                if (b2body.getLinearVelocity().y > 5) {
                    b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 5));
                }
                b2body.applyLinearImpulse(new Vector2(b2body.getLinearVelocity().x, -10f), b2body.getWorldCenter(), true);
            }
        } else {
            if (b2body.getPosition().y > playerPos.y + 1900 / FallingMan.PPM) {
                if (b2body.getLinearVelocity().y > 5) {
                    b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 5));
                }
                b2body.applyLinearImpulse(new Vector2(b2body.getLinearVelocity().x, -10f), b2body.getWorldCenter(), true);
            } else if (b2body.getPosition().y < playerPos.y + (1100 + radius) / FallingMan.PPM) {
                b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, -2));
            }
        }
        if (b2body.getPosition().x < 96 / FallingMan.PPM) {
            b2body.setTransform(160 + random.nextInt(100) / FallingMan.PPM, b2body.getPosition().y, b2body.getAngle());
            b2body.setLinearVelocity(0, 0);
        }
        if (b2body.getPosition().x > (1440 - 96) / FallingMan.PPM) {
            b2body.setTransform((1440 - (160 + random.nextInt(100))) / FallingMan.PPM, b2body.getPosition().y, b2body.getAngle());
            b2body.setLinearVelocity(0, 0);
        }
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(b2body.getAngle()));
    }

    public void defineRock(int chosenSprite) {
        BodyDef bdef = new BodyDef();
        Shape shape = new CircleShape();
        FixtureDef fdef = new FixtureDef();
        switch (chosenSprite) {
            case 0:
                bdef.position.set((random.nextInt(1440 - 96) + 48) / FallingMan.PPM, gameScreen.getPlayer().b2body.getPosition().y + (random.nextInt(800) + 1100) / FallingMan.PPM);
                shape.setRadius(24 / FallingMan.PPM);
                fdef.density = 5f;
                radius = 24;
                break;
            case 1:
                bdef.position.set((random.nextInt(1440 - 192) + 96) / FallingMan.PPM, gameScreen.getPlayer().b2body.getPosition().y + (random.nextInt(800) + 1100) / FallingMan.PPM);
                shape.setRadius(48 / FallingMan.PPM);
                fdef.density = 10f;
                radius = 48;
                break;
            case 11:
                bdef.position.set(192 + 528 / FallingMan.PPM, gameScreen.getPlayer().b2body.getPosition().y +  1100 / FallingMan.PPM);
                shape.setRadius(528 / FallingMan.PPM);
                fdef.density = 100f;
                radius = 528;
                break;
            default:
                /*bdef.position.set((random.nextInt(1440 - 320) + 160) / FallingMan.PPM, gameScreen.getPlayer().b2body.getPosition().y + (random.nextInt(800) + 1100) / FallingMan.PPM);
                shape.setRadius(80 / FallingMan.PPM);
                fdef.density = 15f;
                radius = 80;*/
                shape = new PolygonShape();
                bdef.position.set((random.nextInt(1440 - 320) + 160) / FallingMan.PPM, gameScreen.getPlayer().b2body.getPosition().y + (random.nextInt(800) + 1100) / FallingMan.PPM);

                ((PolygonShape) shape).setAsBox(160  / FallingMan.PPM, 160  / FallingMan.PPM);
                radius = 80;

                break;
        }

        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);


        fdef.shape = shape;
        fdef.friction = 0.5f;
        fdef.restitution = 0f;
        fdef.filter.categoryBits = FallingMan.ROCK_BIT;
        fdef.filter.maskBits = FallingMan.ROCK_BIT | FallingMan.DEFAULT_BIT
                | FallingMan.PLAYER_HEAD_BIT | FallingMan.PLAYER_BELLY_BIT | FallingMan.PLAYER_ARM_BIT | FallingMan.PLAYER_FORE_ARM_BIT | FallingMan.PLAYER_HAND_BIT
                | FallingMan.PLAYER_THIGH_BIT | FallingMan.PLAYER_SHIN_BIT | FallingMan.PLAYER_FOOT_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void generateMapRockUpdate(Vector2 playerPosPrevious) {
        float xDiff;
        float yDiff;
        if (b2body.getPosition().y < playerPosPrevious.y) {
            yDiff = -Math.abs(Math.abs(b2body.getPosition().y) - Math.abs(playerPosPrevious.y));
        } else {

            yDiff = Math.abs(Math.abs(b2body.getPosition().y) - Math.abs(playerPosPrevious.y));
        }

        float previosBodyAngle = b2body.getAngle();

        //Teleporting body part
        b2body.setTransform(b2body.getPosition().x, 8640 / FallingMan.PPM + yDiff, previosBodyAngle);
    }
}
