package com.ledzinygamedevelopment.fallingman.sprites.player;

import com.badlogic.gdx.Gdx;
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
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
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

    public World world;
    public Body b2body;
    private Body b2bodyNeck;
    private TextureRegion playerHeadTexture;
    private PlayScreen playScreen;

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
    private boolean removeHeadJointsAndButton;
    private RevoluteJoint headJoint;

    public Player(World world, PlayScreen playScreen) {
        super(playScreen.getAtlas().findRegion("player"));
        this.world = world;
        this.playScreen = playScreen;
        bodyPartsAll = new Array<>();
        definePlayer();
        playerHeadTexture = new TextureRegion(getTexture(), 1, 773, 160, 160);
        setBounds(0, 0, 160 / FallingMan.PPM, 160 / FallingMan.PPM);
        setRegion(playerHeadTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);

        createBodyParts();

        for(PlayerBodyPart sprite : bodyParts) {
            bodyPartsAll.add(sprite.getB2body());
        }
        headJointsExist = false;
        removeHeadJointsAndButton = false;
        //createHeadJoint();

        leftArmOutOfBody = false;
        rightArmOutOfBody = false;
        leftThighOutOfBody = false;
        rightThighOutOfBody = false;
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
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(b2body.getAngle()));
        //Gdx.app.log("pos x ", String.valueOf(b2body.getPosition().x));
        //Gdx.app.log("left arm angle", String.valueOf((armLeft.getB2body().getAngle() / 6.283185307179586476925286766559 * 360) % 360));
        //Gdx.app.log("right angle", String.valueOf((armRight.getB2body().getAngle() / 6.283185307179586476925286766559 * 360) % 360));

        for(PlayerBodyPart bodyPart : bodyParts) {
            bodyPart.update(dt);
        }

        if(removeHeadJointsAndButton) {
            if(headJointsExist) {
                world.destroyJoint(headJoint);
                headJointsExist = false;
                Gdx.app.log("kkk", "kkk");
            }
            //world.destroyBody(playScreen.getB2WorldCreator().getButton().getB2body());
            //world.destroyJoint(ropeHeadJoint);
            removeHeadJointsAndButton = false;
        }

        if(leftArmOutOfBody) {
            //setLeftArmOutOfBody();
            leftArmOutOfBody = false;
        }
        if(rightArmOutOfBody) {
            //setRightArmOutOfBody();
            rightArmOutOfBody = false;
        }
        if(leftThighOutOfBody) {
            //setLeftThighOutOfBody();
            leftThighOutOfBody = false;
        }
        if(rightThighOutOfBody) {
            //setRightThighOutOfBody();
            rightThighOutOfBody = false;
        }

    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50 / FallingMan.PPM);

        fdef.shape = shape;
        fdef.density = 0.7f;
        fdef.friction = 0.01f;
        fdef.restitution = 0.3f;
        fdef.filter.categoryBits = FallingMan.PLAYER_HEAD_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.DEAD_MACHINE_BIT | FallingMan.DEFAULT_BIT
                | FallingMan.PLAYER_BELLY_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    private void createBodyJoints(Body bodyA, Body bodyB, float aDistX, float aDistY, float bDistX, float bDistY) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.localAnchorA.x = aDistX;
        jointDef.localAnchorB.x = bDistX;
        jointDef.localAnchorA.y = aDistY;
        jointDef.localAnchorB.y = bDistY;
        jointDef.bodyA = bodyA;
        jointDef.bodyB = bodyB;
        jointDef.collideConnected = true;
        world.createJoint(jointDef);

        RopeJointDef ropeJointDef = new RopeJointDef();
        ropeJointDef.localAnchorA.x = aDistX;
        ropeJointDef.localAnchorB.x = bDistX;
        ropeJointDef.localAnchorA.y = aDistY;
        ropeJointDef.localAnchorB.y = bDistY;
        ropeJointDef.bodyA = bodyA;
        ropeJointDef.bodyB = bodyB;
        ropeJointDef.maxLength = 0;
        ropeJointDef.collideConnected = true;
        world.createJoint(ropeJointDef);
    }

    public void updateBodyParts() {
        //transforming player position to new map (unvisible body parts)
        float playerHeadPreviusX = b2body.getPosition().x;
        float playerHeadPreviusY = b2body.getPosition().y;
        b2body.setTransform(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM, b2body.getAngle());
        for(Body body : bodyPartsAll) {

            //calculating distance between body part and player
            float xDiff;
            float yDiff;
            if(body.getPosition().x < playerHeadPreviusX) {
                xDiff = -Math.abs(Math.abs(body.getPosition().x) - Math.abs(playerHeadPreviusX));
            } else {
                xDiff = Math.abs(Math.abs(body.getPosition().x) - Math.abs(playerHeadPreviusX));
            }
            if(body.getPosition().y < playerHeadPreviusY) {
                yDiff = -Math.abs(Math.abs(body.getPosition().y) - Math.abs(playerHeadPreviusY));
            } else {

                yDiff = Math.abs(Math.abs(body.getPosition().y) - Math.abs(playerHeadPreviusY));
            }

            float previosBodyAngle = b2body.getAngle();

            //Teleporting body part
            body.setTransform(body.getPosition().x + xDiff, 8640 / FallingMan.PPM + yDiff, previosBodyAngle);

            //checking pos o body part, if out of the walls, then transforming to pos inside walls
            if(body.getPosition().x < 0) {
                body.setTransform(96 / FallingMan.PPM, body.getPosition().y, previosBodyAngle);
            } else if (body.getPosition().x > 2560 / FallingMan.PPM) {
                body.setTransform((2560 - 96) / FallingMan.PPM, body.getPosition().y, previosBodyAngle);
            }
        }
    }

    private void createBodyParts() {


        //Creating Body
        bodyParts = new Array<>();
        belly = new Belly(world, playScreen, 1, 0);
        bodyParts.add(belly);
        createBodyJoints(b2body, belly.getB2body(), 0, -54f / FallingMan.PPM, 0, 60 / FallingMan.PPM);
        thighLeft = new Thigh(world, playScreen, 2, LEFT_BODY_PART);
        bodyParts.add(thighLeft);
        createBodyJoints(belly.getB2body(), thighLeft.getB2body(), belly.getWidth() / 5f / 1.6666f, (-belly.getHeight() - 24 / FallingMan.PPM) / 1.5f + 60 / FallingMan.PPM,
                0, 50 / FallingMan.PPM);
        thighRight = new Thigh(world, playScreen, 2, RIGHT_BODY_PART);
        bodyParts.add(thighRight);
        createBodyJoints(belly.getB2body(), thighRight.getB2body(), -belly.getWidth() / 5f / 1.6666f, (-belly.getHeight() - 24 / FallingMan.PPM) / 1.5f + 60 / FallingMan.PPM,
                0, 50 / FallingMan.PPM);
        shinLeft = new Shin(world, playScreen, 3, LEFT_BODY_PART);
        bodyParts.add(shinLeft);
        createBodyJoints(thighLeft.getB2body(), shinLeft.getB2body(), 0, (-thighLeft.getHeight() - 4 / FallingMan.PPM) / 1.6666f + 50 / FallingMan.PPM,
                0, 35 / FallingMan.PPM);
        shinRight = new Shin(world, playScreen, 3, RIGHT_BODY_PART);
        bodyParts.add(shinRight);
        createBodyJoints(thighRight.getB2body(), shinRight.getB2body(), 0, (-thighRight.getHeight() - 4 / FallingMan.PPM) / 1.6666f + 50 / FallingMan.PPM,
                0, 35 / FallingMan.PPM);
        footLeft = new Foot(world, playScreen, 4, LEFT_BODY_PART);
        bodyParts.add(footLeft);
        createBodyJoints(shinLeft.getB2body(), footLeft.getB2body(), 0, -shinLeft.getHeight() * 0.75f / 1.6666f + 35 / FallingMan.PPM,
                0, 11 / FallingMan.PPM);
        footRight = new Foot(world, playScreen, 4, RIGHT_BODY_PART);
        bodyParts.add(footRight);
        createBodyJoints(shinRight.getB2body(), footRight.getB2body(), 0, -shinRight.getHeight() * 0.75f / 1.6666f + 35 / FallingMan.PPM,
                0, 11 / FallingMan.PPM);
        footLeft.getB2body().applyLinearImpulse(new Vector2(-10, 0), footLeft.getB2body().getWorldCenter(), true);
        footRight.getB2body().applyLinearImpulse(new Vector2(10, 0), footLeft.getB2body().getWorldCenter(), true);
        armLeft = new Arm(world, playScreen, 2, LEFT_BODY_PART);
        bodyParts.add(armLeft);
        createBodyJoints(belly.getB2body(), armLeft.getB2body(), belly.getWidth() / 2.8f / 1.6666f, belly.getWidth() / 3.05f,
                0, 45 / FallingMan.PPM);
        armRight = new Arm(world, playScreen, 2, RIGHT_BODY_PART);
        bodyParts.add(armRight);
        createBodyJoints(belly.getB2body(), armRight.getB2body(), -belly.getWidth() / 2.8f / 1.6666f, belly.getHeight() / 3.05f,
                0, 45 / FallingMan.PPM);
        foreArmLeft = new ForeArm(world, playScreen, 3, LEFT_BODY_PART);
        bodyParts.add(foreArmLeft);
        createBodyJoints(armLeft.getB2body(), foreArmLeft.getB2body(), 0, -(armLeft.getHeight() + 4 / FallingMan.PPM) / 1.77f + 45 / FallingMan.PPM,
                0, 40 / FallingMan.PPM);
        foreArmRight = new ForeArm(world, playScreen, 3, RIGHT_BODY_PART);
        bodyParts.add(foreArmRight);
        createBodyJoints(armRight.getB2body(), foreArmRight.getB2body(), 0, (-armRight.getHeight() + 4 / FallingMan.PPM) / 1.77f + 45 / FallingMan.PPM,
                0, 40 / FallingMan.PPM);
        handLeft = new Hand(world, playScreen, 4, LEFT_BODY_PART);
        bodyParts.add(handLeft);
        createBodyJoints(foreArmLeft.getB2body(), handLeft.getB2body(), 0, (-foreArmLeft.getHeight() + 17 / FallingMan.PPM) / 1.6666f + 40 / FallingMan.PPM,
                0, -7 / FallingMan.PPM);
        handRight = new Hand(world, playScreen, 4, RIGHT_BODY_PART);
        bodyParts.add(handRight);
        createBodyJoints(foreArmRight.getB2body(), handRight.getB2body(), 0, (-foreArmRight.getHeight() + 17 / FallingMan.PPM) / 1.6666f + 40 / FallingMan.PPM,
                0, -7 / FallingMan.PPM);
        handLeft.getB2body().applyLinearImpulse(new Vector2(10, 0), handLeft.getB2body().getWorldCenter(), true);
        handRight.getB2body().applyLinearImpulse(new Vector2(-10, 0), handRight.getB2body().getWorldCenter(), true);
    }

    private void setLeftArmOutOfBody() {
        armLeft.getB2body().setTransform(b2body.getPosition().x + 1, b2body.getPosition().y, 0);
        foreArmLeft.getB2body().setTransform(b2body.getPosition().x + 2, b2body.getPosition().y, 0);
        handLeft.getB2body().setTransform(b2body.getPosition().x + 3, b2body.getPosition().y, 0);

    }
    private void setRightArmOutOfBody() {
        armRight.getB2body().setTransform(b2body.getPosition().x - 1, b2body.getPosition().y, 0);
        foreArmRight.getB2body().setTransform(b2body.getPosition().x - 2, b2body.getPosition().y, 0);
        handRight.getB2body().setTransform(b2body.getPosition().x - 3, b2body.getPosition().y, 0);
    }
    private void setLeftThighOutOfBody() {
        thighLeft.getB2body().setTransform(b2body.getPosition().x + 1, b2body.getPosition().y - 1, 0);
        shinLeft.getB2body().setTransform(b2body.getPosition().x + 2, b2body.getPosition().y - 2, 0);
        footLeft.getB2body().setTransform(b2body.getPosition().x + 3, b2body.getPosition().y - 3, 0);
    }
    private void setRightThighOutOfBody() {
        thighRight.getB2body().setTransform(b2body.getPosition().x - 1, b2body.getPosition().y - 1, 0);
        shinRight.getB2body().setTransform(b2body.getPosition().x - 2, b2body.getPosition().y - 2, 0);
        footRight.getB2body().setTransform(b2body.getPosition().x - 3, b2body.getPosition().y - 3, 0);
    }

    public void setLeftArmOutOfBodyBool(boolean leftArmOutOfBody) {
        this.leftArmOutOfBody = leftArmOutOfBody;
    }

    public void setRightArmOutOfBodyBool(boolean rightArmOutOfBody) {
        this.rightArmOutOfBody = rightArmOutOfBody;
    }

    public void setLeftThighOutOfBodyBool(boolean leftThighOutOfBody) {
        this.leftThighOutOfBody = leftThighOutOfBody;
    }

    public void setRightThighOutOfBodyBool(boolean rightThighOutOfBody) {
        this.rightThighOutOfBody = rightThighOutOfBody;
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

    public void setRemoveHeadJointsAndButton(boolean removeHeadJointsAndButton) {
        this.removeHeadJointsAndButton = removeHeadJointsAndButton;
    }
}

