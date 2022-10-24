package com.ledzinygamedevelopment.fallingman.sprites.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coins.Spark;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.teleports.Teleport;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.teleports.TeleportTarget;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.Arm;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.Belly;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.Foot;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.ForeArm;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.Hand;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.Shin;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.Thigh;

import java.util.HashMap;

public class Player extends Sprite {

    public static final int LEFT_BODY_PART = 0;
    public static final int RIGHT_BODY_PART = 1;

    private enum CurrentState {NORMAL, TELEPORTING}

    private CurrentState currentState;

    private World world;
    public Body b2body;
    private Body b2bodyNeck;
    private TextureRegion playerHeadTexture;
    private GameScreen gameScreen;
    private HashMap<String, Integer> bodyPartsSpriteNumber;
    private boolean playerInScreenMiddle;

    //Body parts
    private Array<PlayerBodyPart> bodyParts;
    private Array<Body> bodyPartsAll;
    private Belly belly;
    private Arm armLeft;
    private Arm armRight;
    private ForeArm foreArmLeft;
    private ForeArm foreArmRight;
    private Hand handLeft;
    private Hand handRight;
    private Thigh thighLeft;
    private Thigh thighRight;
    private Shin shinLeft;
    private Shin shinRight;
    private Foot footLeft;
    private Foot footRight;
    private boolean leftArmOutOfBody;
    private boolean rightArmOutOfBody;
    private boolean leftThighOutOfBody;
    private boolean rightThighOutOfBody;
    private Body headHolderBody;
    private boolean headJointsExist;
    private boolean removeHeadJoint;
    private RevoluteJoint headJoint;

    private boolean temp;
    private boolean changeTexturesToTeleportationEffect;
    private boolean beforeTeleportation;
    private boolean createSecondStateHeadJoint;
    private float teleportationTimer;
    private float xDist;
    private float yDist;
    private Vector2 teleportTargetPos;
    private Array<Spark> sparks;

    public Player(World world, GameScreen gameScreen, int mapHeight, HashMap<String, Integer> bodyPartsSpriteNumber) {
        this.world = world;
        this.gameScreen = gameScreen;
        this.bodyPartsSpriteNumber = bodyPartsSpriteNumber;
        bodyPartsAll = new Array<>();
        definePlayer(mapHeight);
        playerHeadTexture = new TextureRegion(gameScreen.getPlayerAtlas().findRegion("player" + bodyPartsSpriteNumber.get("head")), 0, 0, 160, 160);
        setBounds(0, 0, 160 / FallingMan.PPM, 160 / FallingMan.PPM);
        setRegion(playerHeadTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
        temp = false;
        createBodyParts(new Vector2(b2body.getPosition().x, b2body.getPosition().y), mapHeight);

        for (PlayerBodyPart sprite : bodyParts) {
            bodyPartsAll.addAll(sprite.getB2bodies());
        }
        headJointsExist = false;
        removeHeadJoint = false;
        //createHeadJoint();

        leftArmOutOfBody = false;
        rightArmOutOfBody = false;
        leftThighOutOfBody = false;
        rightThighOutOfBody = false;
        currentState = CurrentState.NORMAL;
        changeTexturesToTeleportationEffect = false;
        beforeTeleportation = false;
        createSecondStateHeadJoint = false;
        headJointsExist = false;
        sparks = new Array<>();
    }

    public Player(World world, GameScreen gameScreen, int mapHeight, HashMap<String, Integer> bodyPartsSpriteNumber, float posX, float posY, boolean playerInScreenMiddle) {
        this.world = world;
        this.gameScreen = gameScreen;
        this.bodyPartsSpriteNumber = bodyPartsSpriteNumber;
        this.playerInScreenMiddle = playerInScreenMiddle;
        bodyPartsAll = new Array<>();
        definePlayerWithPositions(mapHeight, posX, posY);
        playerHeadTexture = new TextureRegion(gameScreen.getPlayerAtlas().findRegion("player" + bodyPartsSpriteNumber.get("head")), 0, 0, 160, 160);
        setBounds(0, 0, 160 / FallingMan.PPM, 160 / FallingMan.PPM);
        setRegion(playerHeadTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
        temp = false;
        createBodyParts(new Vector2(b2body.getPosition().x, b2body.getPosition().y), mapHeight);

        for (PlayerBodyPart sprite : bodyParts) {
            bodyPartsAll.addAll(sprite.getB2bodies());
        }
        headJointsExist = false;
        removeHeadJoint = false;
        //createHeadJoint();

        leftArmOutOfBody = false;
        rightArmOutOfBody = false;
        leftThighOutOfBody = false;
        rightThighOutOfBody = false;
        currentState = CurrentState.NORMAL;
        changeTexturesToTeleportationEffect = false;
        beforeTeleportation = false;
        createSecondStateHeadJoint = false;
    }

    public void createHeadJoint() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(b2body.getPosition().x, b2body.getPosition().y);
        bdef.type = BodyDef.BodyType.StaticBody;
        headHolderBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / FallingMan.PPM);

        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.DEFAULT_BIT;
        fdef.isSensor = true;
        headHolderBody.createFixture(fdef).setUserData(this);
        RevoluteJointDef headJointDef = new RevoluteJointDef();
        headJointDef.bodyA = b2body;
        headJointDef.bodyB = headHolderBody;
        headJoint = (RevoluteJoint) world.createJoint(headJointDef);/*

        RopeJointDef headRopeJointDef = new RopeJointDef();
        headRopeJointDef.bodyA = b2body;
        headRopeJointDef.bodyB = headHolderBody;
        headRopeJointDef.maxLength = 5 / FallingMan.PPM;
        ropeHeadJoint = (RopeJoint) world.createJoint(headRopeJointDef);*/
        headJointsExist = true;
    }


    public void update(float dt) {
        switch (currentState) {
            case NORMAL:
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRotation((float) Math.toDegrees(b2body.getAngle()));
                //Gdx.app.log("pos x ", String.valueOf(b2body.getPosition().x));
                //Gdx.app.log("left arm angle", String.valueOf((armLeft.getB2body().getAngle() / 6.283185307179586476925286766559 * 360) % 360));
                //Gdx.app.log("right angle", String.valueOf((armRight.getB2body().getAngle() / 6.283185307179586476925286766559 * 360) % 360));

                for (PlayerBodyPart bodyPart : bodyParts) {
                    bodyPart.update(dt);
                }

                if (removeHeadJoint) {
                    if (headJointsExist) {
                        world.destroyJoint(headJoint);
                        headJointsExist = false;
                    }
                    //world.destroyBody(playScreen.getB2WorldCreator().getButton().getB2body());
                    //world.destroyJoint(ropeHeadJoint);
                    removeHeadJoint = false;
                }

                if (leftArmOutOfBody) {
                    //setLeftArmOutOfBody();
                    leftArmOutOfBody = false;
                }
                if (rightArmOutOfBody) {
                    //setRightArmOutOfBody();
                    rightArmOutOfBody = false;
                }
                if (leftThighOutOfBody) {
                    //setLeftThighOutOfBody();
                    leftThighOutOfBody = false;
                }
                if (rightThighOutOfBody) {
                    //setRightThighOutOfBody();
                    rightThighOutOfBody = false;
                }
                break;
            case TELEPORTING:
                if (teleportationTimer < 0.5f && beforeTeleportation) {
                    setScale(8 * (1 - teleportationTimer * 2));
                    teleportationTimer += dt;
                } else if (beforeTeleportation) {
                    world.destroyJoint(headJoint);
                    world.destroyBody(headHolderBody);
                    headJointsExist = false;
                    removeHeadJoint = false;
                    teleportationTimer = 0;
                    b2body.setTransform(b2body.getPosition().x + xDist, b2body.getPosition().y + yDist, b2body.getAngle());
                    setPosition(teleportTargetPos.x - getWidth() / 2, teleportTargetPos.y - getHeight() / 2);
                    for (Body body : getBodyPartsAll()) {
                        body.setTransform(body.getPosition().x + xDist, body.getPosition().y + yDist, body.getAngle());
                    }

                    beforeTeleportation = false;
                } else if (createSecondStateHeadJoint) {
                    createSecondStateHeadJoint = false;
                    createHeadJoint();
                } else if (teleportationTimer < 0.5f) {
                    setScale(8 * teleportationTimer * 2);
                    teleportationTimer += dt;
                } else {
                    setRegion(playerHeadTexture);
                    for (PlayerBodyPart playerBodyPart : bodyParts) {
                        playerBodyPart.setTextureToBasic();
                    }
                    setScale(1);
                    world.destroyJoint(headJoint);
                    world.destroyBody(headHolderBody);
                    headJointsExist = false;
                    removeHeadJoint = false;
                    currentState = CurrentState.NORMAL;
                    gameScreen.setStopRock(false);
                }
                break;
        }
    }

    public void definePlayer(int mapHeight) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50 / FallingMan.PPM);

        fdef.shape = shape;
        fdef.density = 0.035f;
        fdef.friction = 0.001f;
        fdef.restitution = 0.03f;
        fdef.filter.categoryBits = FallingMan.PLAYER_HEAD_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.STOP_WALKING_ENEMY_BIT | FallingMan.WALL_INSIDE_TOWER | FallingMan.ROCK_BIT
                | FallingMan.PLAYER_BELLY_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void definePlayerWithPositions(int mapHeight, float posX, float posY) {
        BodyDef bdef = new BodyDef();
        if (playerInScreenMiddle) {
            bdef.position.set(posX + 330 / FallingMan.PPM, posY);
        } else {
            bdef.position.set(posX, posY);
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50 / FallingMan.PPM);

        fdef.shape = shape;
        fdef.density = 100f;
        fdef.friction = 0.001f;
        fdef.restitution = 0.03f;
        fdef.filter.categoryBits = FallingMan.PLAYER_HEAD_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.STOP_WALKING_ENEMY_BIT | FallingMan.WALL_INSIDE_TOWER | FallingMan.ROCK_BIT
                | FallingMan.PLAYER_BELLY_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    private void createBodyJoints(Body bodyA, Body bodyB, float aDistX, float aDistY, float bDistX, float bDistY, PlayerBodyPart bodyPart) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.localAnchorA.x = aDistX;
        jointDef.localAnchorB.x = bDistX;
        jointDef.localAnchorA.y = aDistY;
        jointDef.localAnchorB.y = bDistY;
        jointDef.bodyA = bodyA;
        jointDef.bodyB = bodyB;
        jointDef.collideConnected = true;
        bodyPart.getJoints().add(world.createJoint(jointDef));

        RopeJointDef ropeJointDef = new RopeJointDef();
        ropeJointDef.localAnchorA.x = aDistX;
        ropeJointDef.localAnchorB.x = bDistX;
        ropeJointDef.localAnchorA.y = aDistY;
        ropeJointDef.localAnchorB.y = bDistY;
        ropeJointDef.bodyA = bodyA;
        ropeJointDef.bodyB = bodyB;
        ropeJointDef.maxLength = 0;
        ropeJointDef.collideConnected = true;
        bodyPart.getJoints().add(world.createJoint(ropeJointDef));
    }

    public void updateBodyParts(int mapHeight, boolean inMenu) {
        //transforming player position to new map (unvisible body parts)
        float playerHeadPreviusX = b2body.getPosition().x;
        float playerHeadPreviusY = b2body.getPosition().y;
        b2body.setTransform(b2body.getPosition().x, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM, b2body.getAngle());
        for (Body body : bodyPartsAll) {

            //calculating distance between body part and player
            float yDiff;

            if (body.getPosition().y < playerHeadPreviusY) {
                yDiff = -Math.abs(Math.abs(body.getPosition().y) - Math.abs(playerHeadPreviusY));
            } else {

                yDiff = Math.abs(Math.abs(body.getPosition().y) - Math.abs(playerHeadPreviusY));
            }

            float previosBodyAngle = b2body.getAngle();

            //Teleporting body part
            body.setTransform(body.getPosition().x, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM + yDiff, body.getAngle());

            //checking pos o body part, if out of the walls, then transforming to pos inside walls
            if (!inMenu) {
                if (body.getPosition().x < 0) {
                    body.setTransform(96 / FallingMan.PPM, body.getPosition().y, previosBodyAngle);
                } else if (body.getPosition().x > FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM) {
                    body.setTransform((FallingMan.MIN_WORLD_WIDTH - 96) / FallingMan.PPM, body.getPosition().y, previosBodyAngle);
                }
            }
        }
    }

    private void createBodyParts(Vector2 headPos, int mapHeight) {

        b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y, -0.0005162548f);

        //Creating Body
        bodyParts = new Array<>();
        if (playerInScreenMiddle) {
            Body bodyHolder;
            BodyDef bdef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fdef = new FixtureDef();
            WeldJointDef rJointDef;

            //head joint
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(b2body.getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = b2body;
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);


            belly = new Belly(world, gameScreen, 1, 0, mapHeight, bodyPartsSpriteNumber.get("belly"));
            bodyParts.add(belly);
            for (Body body : belly.getB2bodies()) {
                body.setTransform(headPos.x, headPos.y - 1.1401596f * 2, -25.125046f);
            }
            belly.setBodyPartName("belly");
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(belly.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = belly.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);


            thighLeft = new Thigh(world, gameScreen, 5, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("thighL"));
            bodyParts.add(thighLeft);
            for (Body body : thighLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.22407818f * 4, headPos.y - 2.266098f * 2, 0.044655986f);
            }
            thighLeft.setBodyPartName("thighL");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(thighLeft.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = thighLeft.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);


            thighRight = new Thigh(world, gameScreen, 5, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("thighR"));
            bodyParts.add(thighRight);
            for (Body body : thighRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.22407818f * 4, headPos.y - 2.266098f * 2, 6.3245335f);
            }
            thighRight.setBodyPartName("thighR");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(thighRight.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = thighRight.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            shinLeft = new Shin(world, gameScreen, 6, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("shinL"));
            bodyParts.add(shinLeft);
            for (Body body : shinLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.22407818f * 4, headPos.y - 3.1189423f * 2, 0.051409073f);
            }
            shinLeft.setBodyPartName("shinL");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(shinLeft.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = shinLeft.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            shinRight = new Shin(world, gameScreen, 6, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("shinR"));
            bodyParts.add(shinRight);
            for (Body body : shinRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.22407818f * 4, headPos.y - 3.1189423f * 2, 0.050789133f);
            }
            shinRight.setBodyPartName("shinR");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(shinRight.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = shinRight.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            footLeft = new Foot(world, gameScreen, 7, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("footL"));
            bodyParts.add(footLeft);
            for (Body body : footLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.28530788f * 4, headPos.y - 3.5463333f * 2.3f, 0.03767248f);
            }
            footLeft.setBodyPartName("footL");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(footLeft.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = footLeft.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            footRight = new Foot(world, gameScreen, 7, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("footR"));
            bodyParts.add(footRight);
            for (Body body : footRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.28530788f * 4, headPos.y - 3.5463333f * 2.3f, 0.10148369f);
            }
            footRight.setBodyPartName("footR");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(footRight.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = footRight.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            //footLeft.getB2body().applyLinearImpulse(new Vector2(-10, 0), footLeft.getB2body().getWorldCenter(), true);
            //footRight.getB2body().applyLinearImpulse(new Vector2(10, 0), footLeft.getB2body().getWorldCenter(), true);

            armLeft = new Arm(world, gameScreen, 2, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("armL"));
            bodyParts.add(armLeft);
            for (Body body : armLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.36735058f * 6, headPos.y - 1.0676193f * 2, 6.3533444f);
            }
            armLeft.setBodyPartName("armL");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(armLeft.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = armLeft.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            armRight = new Arm(world, gameScreen, 2, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("armR"));
            bodyParts.add(armRight);
            for (Body body : armRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.36735058f * 6, headPos.y - 1.0676193f * 2, 6.227114f);
            }
            armRight.setBodyPartName("armR");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(armRight.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = armRight.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            foreArmLeft = new ForeArm(world, gameScreen, 3, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("foreArmL"));
            bodyParts.add(foreArmLeft);
            for (Body body : foreArmLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.39203787f * 6, headPos.y - 1.917366f * 2, 6.276788f);
            }
            foreArmLeft.setBodyPartName("foreArmL");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(foreArmLeft.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = foreArmLeft.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            foreArmRight = new ForeArm(world, gameScreen, 3, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("foreArmR"));
            bodyParts.add(foreArmRight);
            for (Body body : foreArmRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.39203787f * 6, headPos.y - 1.917366f * 2, -0.0034535923f);
            }
            foreArmRight.setBodyPartName("foreArmR");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(foreArmRight.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = foreArmRight.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            handLeft = new Hand(world, gameScreen, 4, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("handL"));
            bodyParts.add(handLeft);
            for (Body body : handLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.39327335f * 6, headPos.y - 2.389923f * 2.3f, 6.2916613f);
            }
            handLeft.setBodyPartName("handL");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(handLeft.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = handLeft.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);

            handRight = new Hand(world, gameScreen, 4, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("handR"));
            bodyParts.add(handRight);
            for (Body body : handRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.39327335f * 6, headPos.y - 2.389923f * 2.3f, 0.0017546086f);
            }
            handRight.setBodyPartName("handR");
            bdef = new BodyDef();
            shape = new PolygonShape();
            fdef = new FixtureDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(handRight.getB2body().getPosition());
            bodyHolder = world.createBody(bdef);
            shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            bodyHolder.createFixture(fdef);
            bodyPartsAll.add(bodyHolder);
            rJointDef = new WeldJointDef();
            rJointDef.bodyA = handRight.getB2body();
            rJointDef.bodyB = bodyHolder;
            world.createJoint(rJointDef);
        } else {
            belly = new Belly(world, gameScreen, 1, 0, mapHeight, bodyPartsSpriteNumber.get("belly"));
            createBodyJoints(b2body, belly.getB2body(), 0, -54f / FallingMan.PPM, 0, 60 / FallingMan.PPM, belly);
            for (Body body : belly.getB2bodies()) {
                body.setTransform(headPos.x + 0.004893303f, headPos.y - 1.1401596f, -25.125046f);
            }
            belly.setBodyPartName("belly");

            thighLeft = new Thigh(world, gameScreen, 5, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("thighL"));
            createBodyJoints(belly.getB2body(), thighLeft.getB2body(), belly.getWidth() / 5f / 1.6666f, (-belly.getHeight() - 24 / FallingMan.PPM) / 1.5f + 60 / FallingMan.PPM,
                    0, 50 / FallingMan.PPM, thighLeft);
            for (Body body : thighLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.22407818f, headPos.y - 2.266098f, 0.044655986f);
            }
            thighLeft.setBodyPartName("thighL");

            thighRight = new Thigh(world, gameScreen, 5, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("thighR"));

            createBodyJoints(belly.getB2body(), thighRight.getB2body(), -belly.getWidth() / 5f / 1.6666f, (-belly.getHeight() - 24 / FallingMan.PPM) / 1.5f + 60 / FallingMan.PPM,
                    0, 50 / FallingMan.PPM, thighRight);
            for (Body body : thighRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.16154957f, headPos.y - 2.2691345f, 6.3245335f);
            }
            thighRight.setBodyPartName("thighR");



            shinLeft = new Shin(world, gameScreen, 6, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("shinL"));

            createBodyJoints(thighLeft.getB2body(), shinLeft.getB2body(), 0, -50 / FallingMan.PPM,
                    0, 35 / FallingMan.PPM, shinLeft);
            for (Body body : shinLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.26449728f, headPos.y - 3.1189423f, 0.051409073f);
            }
            shinLeft.setBodyPartName("shinL");

            shinRight = new Shin(world, gameScreen, 6, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("shinR"));

            createBodyJoints(thighRight.getB2body(), shinRight.getB2body(), 0, -50 / FallingMan.PPM,
                    0, 35 / FallingMan.PPM, shinRight);
            for (Body body : shinRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.12305069f, headPos.y - 3.1221619f, 0.050789133f);
            }
            shinRight.setBodyPartName("shinR");

            footLeft = new Foot(world, gameScreen, 7, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("footL"));

            createBodyJoints(shinLeft.getB2body(), footLeft.getB2body(), 0, -35 / FallingMan.PPM,
                    0, 7.5f / FallingMan.PPM, footLeft);
            for (Body body : footLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.28530788f, headPos.y - 3.5434265f, 0.03767248f);
            }
            footLeft.setBodyPartName("footL");

            footRight = new Foot(world, gameScreen, 7, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("footR"));

            createBodyJoints(shinRight.getB2body(), footRight.getB2body(), 0, -35 / FallingMan.PPM,
                    0, 7.5f / FallingMan.PPM, footRight);
            for (Body body : footRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.09768391f, headPos.y - 3.5463333f, 0.10148369f);
            }
            footRight.setBodyPartName("footR");

            //footLeft.getB2body().applyLinearImpulse(new Vector2(-10, 0), footLeft.getB2body().getWorldCenter(), true);
            //footRight.getB2body().applyLinearImpulse(new Vector2(10, 0), footLeft.getB2body().getWorldCenter(), true);

            armLeft = new Arm(world, gameScreen, 2, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("armL"));

            createBodyJoints(belly.getB2body(), armLeft.getB2body(), belly.getWidth() / 2.8f / 1.6666f, belly.getWidth() / 3.05f,
                    0, 45 / FallingMan.PPM, armLeft);
            for (Body body : armLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.3755331f, headPos.y - 1.0629425f, 6.3533444f);
            }
            armLeft.setBodyPartName("armL");

            armRight = new Arm(world, gameScreen, 2, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("armR"));

            createBodyJoints(belly.getB2body(), armRight.getB2body(), -belly.getWidth() / 2.8f / 1.6666f, belly.getHeight() / 3.05f,
                    0, 45 / FallingMan.PPM, armRight);
            for (Body body : armRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.36735058f, headPos.y - 1.0676193f, 6.227114f);
            }
            armRight.setBodyPartName("armR");

            foreArmLeft = new ForeArm(world, gameScreen, 3, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("foreArmL"));
            createBodyJoints(armLeft.getB2body(), foreArmLeft.getB2body(), 0, -(45 / FallingMan.PPM),
                    0, 40 / FallingMan.PPM, foreArmLeft);
            for (Body body : foreArmLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.40441942f, headPos.y - 1.9146042f, 6.276788f);
            }
            foreArmLeft.setBodyPartName("foreArmL");

            foreArmRight = new ForeArm(world, gameScreen, 3, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("foreArmR"));
            createBodyJoints(armRight.getB2body(), foreArmRight.getB2body(), 0, -(45 / FallingMan.PPM),
                    0, 40 / FallingMan.PPM, foreArmRight);
            for (Body body : foreArmRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.39203787f, headPos.y - 1.917366f, -0.0034535923f);
            }
            foreArmRight.setBodyPartName("foreArmR");

            handLeft = new Hand(world, gameScreen, 4, LEFT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("handL"));

            createBodyJoints(foreArmLeft.getB2body(), handLeft.getB2body(), 0, -40 / FallingMan.PPM,
                    0, 21 / FallingMan.PPM, handLeft);
            for (Body body : handLeft.getB2bodies()) {
                body.setTransform(headPos.x + 0.40245438f, headPos.y - 2.3845978f, 6.2916613f);
            }
            handLeft.setBodyPartName("handL");

            handRight = new Hand(world, gameScreen, 4, RIGHT_BODY_PART, mapHeight, bodyPartsSpriteNumber.get("handR"));

            createBodyJoints(foreArmRight.getB2body(), handRight.getB2body(), 0, -40 / FallingMan.PPM,
                    0, 21 / FallingMan.PPM, handRight);
            for (Body body : handRight.getB2bodies()) {
                body.setTransform(headPos.x - 0.39327335f, headPos.y - 2.389923f, 0.0017546086f);
            }
            handRight.setBodyPartName("handR");




            bodyParts.add(footLeft);
            bodyParts.add(footRight);
            bodyParts.add(shinLeft);
            bodyParts.add(shinRight);
            bodyParts.add(thighLeft);
            bodyParts.add(thighRight);
            bodyParts.add(handLeft);
            bodyParts.add(handRight);
            bodyParts.add(foreArmLeft);
            bodyParts.add(foreArmRight);
            bodyParts.add(armLeft);
            bodyParts.add(armRight);
            bodyParts.add(belly);
        }
        //handLeft.getB2body().applyLinearImpulse(new Vector2(10, 0), handLeft.getB2body().getWorldCenter(), true);
        //handRight.getB2body().applyLinearImpulse(new Vector2(-10, 0), handRight.getB2body().getWorldCenter(), true);
    }

    public Belly getBelly() {
        return belly;
    }

    public Array<PlayerBodyPart> getBodyParts() {
        return bodyParts;
    }

    public boolean isHeadJointsExist() {
        return headJointsExist;
    }

    public void setRemoveHeadJoint(boolean removeHeadJoint) {
        this.removeHeadJoint = removeHeadJoint;
    }

    public Array<Body> getBodyPartsAll() {
        return bodyPartsAll;
    }

    public void restoreBodyParts(int mapHeight) {
        //if (!temp) {
            /*Gdx.app.log("head angle: ", String.valueOf(b2body.getAngle()));
            for (PlayerBodyPart bodyPart : bodyParts) {
                float yDiff;

                if (bodyPart.getB2body().getPosition().y < b2body.getPosition().y) {
                    yDiff = -Math.abs(Math.abs(bodyPart.getB2body().getPosition().y) - Math.abs(b2body.getPosition().y));
                } else {

                    yDiff = Math.abs(Math.abs(bodyPart.getB2body().getPosition().y) - Math.abs(b2body.getPosition().y));
                }

                float xDiff;

                if (bodyPart.getB2body().getPosition().x < b2body.getPosition().x) {
                    xDiff = -Math.abs(Math.abs(bodyPart.getB2body().getPosition().x) - Math.abs(b2body.getPosition().x));
                } else {

                    xDiff = Math.abs(Math.abs(bodyPart.getB2body().getPosition().x) - Math.abs(b2body.getPosition().x));
                }

                Gdx.app.log("class ", String.valueOf(bodyPart.getClass()));
                Gdx.app.log("x and y ", String.valueOf(xDiff + "  |  " + yDiff));
                Gdx.app.log("angle ", String.valueOf(bodyPart.getB2body().getAngle()));
            }*/


        for (Body body : bodyPartsAll) {
            world.destroyBody(body);
        }
        bodyPartsAll = new Array<>();
        bodyParts = new Array<>();

        createBodyParts(new Vector2(b2body.getPosition().x, b2body.getPosition().y), mapHeight);

        for (PlayerBodyPart bodyPart : bodyParts) {
            bodyPartsAll.addAll(bodyPart.getB2bodies());
        }
        for (Body body : bodyPartsAll) {
            //body.setTransform(b2body.getPosition().x, b2body.getPosition().y, b2body.getAngle());
        }
        //}
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setCurrentStateToTeleport(TeleportTarget teleportTarget, Teleport teleport) {

        setRegion(gameScreen.getDefaultAtlas().findRegion("teleportation_effect"), 0, 0, 1280, 1280);
        setPosition(teleport.getBody().getPosition().x - getWidth() / 2, teleport.getBody().getPosition().y - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setScale(8);
        for (PlayerBodyPart playerBodyPart : bodyParts) {
            playerBodyPart.setRegion(gameScreen.getDefaultAtlas().findRegion("blank160"), 0, 0, 160, 160);
        }
        createHeadJoint();
        currentState = CurrentState.TELEPORTING;
        changeTexturesToTeleportationEffect = true;
        beforeTeleportation = true;

        yDist = teleportTarget.getBody().getPosition().y - teleport.getBody().getPosition().y;
        xDist = teleportTarget.getBody().getPosition().x - teleport.getBody().getPosition().x;

        teleportTargetPos = teleportTarget.getBody().getPosition();
        /*b2body.setTransform(xDist, yDist, b2body.getAngle());

        for (Body body : bodyPartsAll) {
            body.setTransform(xDist, yDist, body.getAngle());
        }*/
        teleportationTimer = 0;
        createSecondStateHeadJoint = true;
        gameScreen.setStopRock(true);
    }

    public boolean isHunted() {
        if (currentState.equals(CurrentState.NORMAL)) {
            return true;
        } else {
            return false;
        }
    }

    public int getHeadSpriteNumber() {
        return bodyPartsSpriteNumber.get("head");
    }

    public void setTexture(int spriteNumber) {
        bodyPartsSpriteNumber.put("head", spriteNumber);
        setRegion(new TextureRegion(gameScreen.getPlayerAtlas().findRegion("player" + spriteNumber), 0, 0, 160, 160));
    }

    // checking if mouse position equals player position
    public boolean mouseOver(Vector2 mousePosition) {
        if (mousePosition.x > b2body.getPosition().x - 180 / FallingMan.PPM && mousePosition.x < b2body.getPosition().x + 180 / FallingMan.PPM
                && mousePosition.y > b2body.getPosition().y - 400 / FallingMan.PPM && mousePosition.y < b2body.getPosition().y + 150 / FallingMan.PPM)
            return true;
        else
            return false;
    }

    // checking if mouse position equals head position
    public boolean mouseOverHead(Vector2 mousePosition) {
        if (mousePosition.x > b2body.getPosition().x - 60 / FallingMan.PPM && mousePosition.x < b2body.getPosition().x + 60 / FallingMan.PPM
                && mousePosition.y > b2body.getPosition().y - 70 / FallingMan.PPM && mousePosition.y < b2body.getPosition().y + 70 / FallingMan.PPM)
            return true;
        else
            return false;
    }

    public Array<Spark> getSparks() {
        return sparks;
    }
}

