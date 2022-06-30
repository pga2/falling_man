package com.ledzinygamedevelopment.fallingman.sprites.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
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

    public Player(World world, GameScreen gameScreen, int mapHeight) {
        //super(gameScreen.getAtlas().findRegion("player"));
        this.world = world;
        this.gameScreen = gameScreen;
        bodyPartsAll = new Array<>();
        definePlayer(mapHeight);
        playerHeadTexture = new TextureRegion(gameScreen.getDefaultAtlas().findRegion("player"), 0, 0, 160, 160);
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
                    headJointsExist = false;
                    removeHeadJoint = false;
                    teleportationTimer = 0;
                    b2body.setTransform(b2body.getPosition().x + xDist, b2body.getPosition().y + yDist, b2body.getAngle());
                    setPosition(teleportTargetPos.x - getWidth() / 2, teleportTargetPos.y - getHeight() / 2);
                    for (Body body : getBodyPartsAll()) {
                        body.setTransform(body.getPosition().x + xDist, body.getPosition().y + yDist, body.getAngle());
                    }

                    beforeTeleportation = false;
                } else if (createSecondStateHeadJoint){
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
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.DEAD_MACHINE_BIT | FallingMan.WALL_INSIDE_TOWER | FallingMan.ROCK_BIT
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

    public void updateBodyParts(int mapHeight) {
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
            if (body.getPosition().x < 0) {
                body.setTransform(96 / FallingMan.PPM, body.getPosition().y, previosBodyAngle);
            } else if (body.getPosition().x > 2560 / FallingMan.PPM) {
                body.setTransform((2560 - 96) / FallingMan.PPM, body.getPosition().y, previosBodyAngle);
            }
        }
    }

    private void createBodyParts(Vector2 headPos, int mapHeight) {

        b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y, -0.0005162548f);

        //Creating Body
        bodyParts = new Array<>();
        belly = new Belly(world, gameScreen, 1, 0, mapHeight);
        bodyParts.add(belly);
        createBodyJoints(b2body, belly.getB2body(), 0, -54f / FallingMan.PPM, 0, 60 / FallingMan.PPM, belly);
        for (Body body : belly.getB2bodies()) {
            body.setTransform(headPos.x + 0.004893303f, headPos.y - 1.1401596f, -25.125046f);
        }
        belly.setBodyPartName("belly");

        thighLeft = new Thigh(world, gameScreen, 2, LEFT_BODY_PART, mapHeight);
        bodyParts.add(thighLeft);
        createBodyJoints(belly.getB2body(), thighLeft.getB2body(), belly.getWidth() / 5f / 1.6666f, (-belly.getHeight() - 24 / FallingMan.PPM) / 1.5f + 60 / FallingMan.PPM,
                0, 50 / FallingMan.PPM, thighLeft);
        for (Body body : thighLeft.getB2bodies()) {
            body.setTransform(headPos.x + 0.22407818f, headPos.y - 2.266098f, 0.044655986f);
        }
        thighLeft.setBodyPartName("thighLeft");

        thighRight = new Thigh(world, gameScreen, 2, RIGHT_BODY_PART, mapHeight);
        bodyParts.add(thighRight);
        createBodyJoints(belly.getB2body(), thighRight.getB2body(), -belly.getWidth() / 5f / 1.6666f, (-belly.getHeight() - 24 / FallingMan.PPM) / 1.5f + 60 / FallingMan.PPM,
                0, 50 / FallingMan.PPM, thighRight);
        for (Body body : thighRight.getB2bodies()) {
            body.setTransform(headPos.x - 0.16154957f, headPos.y - 2.2691345f, 6.3245335f);
        }
        thighRight.setBodyPartName("thighRight");

        shinLeft = new Shin(world, gameScreen, 3, LEFT_BODY_PART, mapHeight);
        bodyParts.add(shinLeft);
        createBodyJoints(thighLeft.getB2body(), shinLeft.getB2body(), 0, -50 / FallingMan.PPM,
                0, 35 / FallingMan.PPM, shinLeft);
        for (Body body : shinLeft.getB2bodies()) {
            body.setTransform(headPos.x + 0.26449728f, headPos.y - 3.1189423f, 0.051409073f);
        }
        shinLeft.setBodyPartName("shinLeft");

        shinRight = new Shin(world, gameScreen, 3, RIGHT_BODY_PART, mapHeight);
        bodyParts.add(shinRight);
        createBodyJoints(thighRight.getB2body(), shinRight.getB2body(), 0, -50 / FallingMan.PPM,
                0, 35 / FallingMan.PPM, shinRight);
        for (Body body : shinRight.getB2bodies()) {
            body.setTransform(headPos.x - 0.12305069f, headPos.y - 3.1221619f, 0.050789133f);
        }
        shinRight.setBodyPartName("shinRight");

        footLeft = new Foot(world, gameScreen, 4, LEFT_BODY_PART, mapHeight);
        bodyParts.add(footLeft);
        createBodyJoints(shinLeft.getB2body(), footLeft.getB2body(), 0, -35 / FallingMan.PPM,
                0, 7.5f / FallingMan.PPM, footLeft);
        for (Body body : footLeft.getB2bodies()) {
            body.setTransform(headPos.x + 0.28530788f, headPos.y - 3.5434265f, 0.03767248f);
        }
        footLeft.setBodyPartName("footLeft");

        footRight = new Foot(world, gameScreen, 4, RIGHT_BODY_PART, mapHeight);
        bodyParts.add(footRight);
        createBodyJoints(shinRight.getB2body(), footRight.getB2body(), 0, -35 / FallingMan.PPM,
                0, 7.5f / FallingMan.PPM, footRight);
        for (Body body : footRight.getB2bodies()) {
            body.setTransform(headPos.x - 0.09768391f, headPos.y - 3.5463333f, 0.10148369f);
        }
        footRight.setBodyPartName("footRight");

        //footLeft.getB2body().applyLinearImpulse(new Vector2(-10, 0), footLeft.getB2body().getWorldCenter(), true);
        //footRight.getB2body().applyLinearImpulse(new Vector2(10, 0), footLeft.getB2body().getWorldCenter(), true);

        armLeft = new Arm(world, gameScreen, 2, LEFT_BODY_PART, mapHeight);
        bodyParts.add(armLeft);
        createBodyJoints(belly.getB2body(), armLeft.getB2body(), belly.getWidth() / 2.8f / 1.6666f, belly.getWidth() / 3.05f,
                0, 45 / FallingMan.PPM, armLeft);
        for (Body body : armLeft.getB2bodies()) {
            body.setTransform(headPos.x + 0.3755331f, headPos.y - 1.0629425f, 6.3533444f);
        }
        armLeft.setBodyPartName("armLeft");

        armRight = new Arm(world, gameScreen, 2, RIGHT_BODY_PART, mapHeight);
        bodyParts.add(armRight);
        createBodyJoints(belly.getB2body(), armRight.getB2body(), -belly.getWidth() / 2.8f / 1.6666f, belly.getHeight() / 3.05f,
                0, 45 / FallingMan.PPM, armRight);
        for (Body body : armRight.getB2bodies()) {
            body.setTransform(headPos.x - 0.36735058f, headPos.y - 1.0676193f, 6.227114f);
        }
        armRight.setBodyPartName("armRight");

        foreArmLeft = new ForeArm(world, gameScreen, 3, LEFT_BODY_PART, mapHeight);
        bodyParts.add(foreArmLeft);
        createBodyJoints(armLeft.getB2body(), foreArmLeft.getB2body(), 0, -(45 / FallingMan.PPM),
                0, 40 / FallingMan.PPM, foreArmLeft);
        for (Body body : foreArmLeft.getB2bodies()) {
            body.setTransform(headPos.x + 0.40441942f, headPos.y - 1.9146042f, 6.276788f);
        }
        foreArmLeft.setBodyPartName("foreArmLeft");

        foreArmRight = new ForeArm(world, gameScreen, 3, RIGHT_BODY_PART, mapHeight);
        bodyParts.add(foreArmRight);
        createBodyJoints(armRight.getB2body(), foreArmRight.getB2body(), 0, -(45 / FallingMan.PPM),
                0, 40 / FallingMan.PPM, foreArmRight);
        for (Body body : foreArmRight.getB2bodies()) {
            body.setTransform(headPos.x - 0.39203787f, headPos.y - 1.917366f, -0.0034535923f);
        }
        foreArmRight.setBodyPartName("foreArmRight");

        handLeft = new Hand(world, gameScreen, 4, LEFT_BODY_PART, mapHeight);
        bodyParts.add(handLeft);
        createBodyJoints(foreArmLeft.getB2body(), handLeft.getB2body(), 0, -40 / FallingMan.PPM,
                0, 7 / FallingMan.PPM, handLeft);
        for (Body body : handLeft.getB2bodies()) {
            body.setTransform(headPos.x + 0.40245438f, headPos.y - 2.3845978f, 6.2916613f);
        }
        handLeft.setBodyPartName("handLeft");

        handRight = new Hand(world, gameScreen, 4, RIGHT_BODY_PART, mapHeight);
        bodyParts.add(handRight);
        createBodyJoints(foreArmRight.getB2body(), handRight.getB2body(), 0, -40 / FallingMan.PPM,
                0, 7 / FallingMan.PPM, handRight);
        for (Body body : handRight.getB2bodies()) {
            body.setTransform(headPos.x - 0.39327335f, headPos.y - 2.389923f, 0.0017546086f);
        }
        handRight.setBodyPartName("handRight");

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

}

