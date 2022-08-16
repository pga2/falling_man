package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.beams;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;

public class MovingBeam extends Sprite implements InteractiveObjectInterface {
    private boolean touched;
    private Fixture fixture;
    private World world;
    private Rectangle bounds;
    private final PlayScreen playScreen;
    private Body body;
    private Body headHolderBody;
    private RevoluteJoint headJoint;

    public MovingBeam(World world, Rectangle bounds, PlayScreen playScreen) {
        this.world = world;
        this.bounds = bounds;
        this.playScreen = playScreen;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / FallingMan.PPM, (bounds.getY() + bounds.getHeight() / 2) / FallingMan.PPM);
        body = world.createBody(bdef);
        shape.setAsBox((bounds.getWidth() / 2)  / FallingMan.PPM, (bounds.getHeight() / 2)  / FallingMan.PPM);
        fdef.shape = shape;
        fdef.density = bounds.getWidth() / 256 / 20;
        fdef.filter.categoryBits = FallingMan.WALL_INSIDE_TOWER;
        fixture = body.createFixture(fdef);
        touched = false;

        setBounds(0, 0, 256 / FallingMan.PPM, 32 / FallingMan.PPM);
        setRegion(new TextureRegion(playScreen.getDefaultAtlas().findRegion("moving_beam"), 0, 0, 256, 32));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setScale(bounds.getWidth() / 256, getScaleY());
        createJoint();
    }

    private void createJoint() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(body.getPosition().x, body.getPosition().y);
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
        headJointDef.bodyA = body;
        headJointDef.bodyB = headHolderBody;
        headJoint = (RevoluteJoint) world.createJoint(headJointDef);
    }

    @Override
    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    @Override
    public void touched() {

    }

    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void draw(Batch batch) {

        super.draw(batch);
    }

    @Override
    public void update(float dt) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(body.getAngle()));
    }

    @Override
    public boolean isToRemove() {
        return false;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {

    }

    public Body getHeadHolderBody() {
        return headHolderBody;
    }
}
