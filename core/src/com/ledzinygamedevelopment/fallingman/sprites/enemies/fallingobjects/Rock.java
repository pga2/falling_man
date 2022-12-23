package com.ledzinygamedevelopment.fallingman.sprites.enemies.fallingobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.tools.Utils;

import java.util.Random;

public class Rock {

    private Random random;
    private GameScreen gameScreen;
    private World world;
    private Body b2body;
    private int radius;
    private float animationTimer;
    private Fixture fixture;
    private Skeleton skeleton;
    private AnimationState animationState;
    private boolean soundsPlayed;
    private long soundId;

    public Rock(GameScreen gameScreen, World world, boolean bigRock) {
        this.gameScreen = gameScreen;
        this.world = world;
        //animation = new Animation(0.017f, textures);

        animationTimer = 0.0001f;

        TextureAtlas bigRockAtlas = ((PlayScreen) gameScreen).getBigRockAtlas();
        SkeletonJson json = new SkeletonJson(bigRockAtlas);
        json.setScale(1 / FallingMan.PPM);
        SkeletonData rockSkeletonData = json.readSkeletonData(Gdx.files.internal("spine_animations/big_rock.json"));
        AnimationStateData rockAnimationStateData = new AnimationStateData(rockSkeletonData);
        skeleton = new Skeleton(rockSkeletonData);
        animationState = new AnimationState(rockAnimationStateData);
        animationState.setAnimation(0, "walk", true);

        //setRegion(((PlayScreen) gameScreen).getBigRockAtlas());
        random = new Random();
        //setBounds(0, 0, 1024 / FallingMan.PPM, 2048 / FallingMan.PPM);
        //setRegion(gameScreen.getBigRockAtlas().findRegion("rock1056"), 0, 0, 1232, 1232);
        //setOrigin(getWidth() / 2, getHeight() / 2);
        defineRock(11);
        soundsPlayed = false;
    }

    public void update(float dt, Vector2 playerPos, boolean stopRocks, boolean holdClose) {
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
                b2body.applyLinearImpulse(new Vector2(b2body.getLinearVelocity().x * Utils.getDeltaTimeX1(), 10f * Utils.getDeltaTimeX1()), b2body.getWorldCenter(), true);
            } else if (b2body.getPosition().y < playerPos.y + (1100 + radius) / FallingMan.PPM) {
                if (b2body.getLinearVelocity().y < -5) {
                    b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, -5));
                }
                b2body.applyLinearImpulse(new Vector2(b2body.getLinearVelocity().x * Utils.getDeltaTimeX1(), 10f * Utils.getDeltaTimeX1()), b2body.getWorldCenter(), true);
            } else if (b2body.getPosition().y > playerPos.y + 1900 / FallingMan.PPM) {
                if (b2body.getLinearVelocity().y > 5) {
                    b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 5));
                }
                b2body.applyLinearImpulse(new Vector2(b2body.getLinearVelocity().x * Utils.getDeltaTimeX1(), -10f * Utils.getDeltaTimeX1()), b2body.getWorldCenter(), true);
            }
        } else {
            if (b2body.getPosition().y > playerPos.y + (holdClose ? 1200 / FallingMan.PPM : 1900 / FallingMan.PPM)) {
                if (b2body.getLinearVelocity().y > 5) {
                    b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 5));
                }
                if (holdClose) {
                    b2body.setLinearVelocity(gameScreen.getPlayer().b2body.getLinearVelocity());
                } else {
                    b2body.applyLinearImpulse(new Vector2(b2body.getLinearVelocity().x * Utils.getDeltaTimeX1(), -10f * Utils.getDeltaTimeX1()), b2body.getWorldCenter(), true);
                }
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
        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //setRotation((float) Math.toDegrees(b2body.getAngle()));
        skeleton.setPosition(b2body.getPosition().x, b2body.getPosition().y - 850 / FallingMan.PPM);
        //setRegion(getFrame(dt));
        //Gdx.app.log("y speed ", String.valueOf(b2body.getLinearVelocity().y));
        animationState.update(dt * -b2body.getLinearVelocity().y * 0.15f);
        animationState.apply(skeleton);


        if (gameScreen.getSaveData().getSounds()) {
            float distToSound = 12;
            if (Utils.getDistBetweenBodies(b2body, gameScreen.getPlayer().b2body) < distToSound && !soundsPlayed) {
                gameScreen.getAssetManager().getBigRockSound().stop(soundId);
                soundId = gameScreen.getAssetManager().getBigRockSound().play();
                gameScreen.getAssetManager().getBigRockSound().setLooping(soundId, true);
                soundsPlayed = true;
            } else if (Utils.getDistBetweenBodies(b2body, gameScreen.getPlayer().b2body) > distToSound + 2) {
                gameScreen.getAssetManager().getBigRockSound().stop(soundId);
                soundsPlayed = false;
            } else {
                gameScreen.getAssetManager().getBigRockSound().setVolume(soundId, 1 - Utils.getDistBetweenBodies(b2body, gameScreen.getPlayer().b2body) / distToSound);
            }
            if (stopRocks) {
                gameScreen.getAssetManager().getBigRockSound().setVolume(soundId, 0);
            }
        }
    }


    public void draw(Batch batch, SkeletonRenderer skeletonRenderer) {
        skeleton.updateWorldTransform();

        skeletonRenderer.draw(batch, skeleton);
    }

    /*private Texture getFrame(float dt) {
        animationTimer += dt;
        return (Texture) animation.getKeyFrame(animationTimer, true);
    }*/

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
                bdef.position.set(112 / FallingMan.PPM + 616 / FallingMan.PPM, gameScreen.getPlayer().b2body.getPosition().y +  2500 / FallingMan.PPM);
                shape.setRadius(616 / FallingMan.PPM);
                fdef.density = 100f;
                radius = 616;
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
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }

    public void generateMapRockUpdate(Vector2 playerPosPrevious, int mapHeight) {
        float xDiff;
        float yDiff;
        if (b2body.getPosition().y < playerPosPrevious.y) {
            yDiff = -Math.abs(Math.abs(b2body.getPosition().y) - Math.abs(playerPosPrevious.y));
        } else {

            yDiff = Math.abs(Math.abs(b2body.getPosition().y) - Math.abs(playerPosPrevious.y));
        }

        float previosBodyAngle = b2body.getAngle();

        //Teleporting body part
        b2body.setTransform(b2body.getPosition().x, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM + yDiff, previosBodyAngle);
    }

    public Fixture getFixture() {
        return fixture;
    }

    public Body getB2body() {
        return b2body;
    }

    public float getAnimationTimer() {
        return animationTimer;
    }

    public void setAnimationTimer(float animationTimer) {
        this.animationTimer = animationTimer;
    }
}
