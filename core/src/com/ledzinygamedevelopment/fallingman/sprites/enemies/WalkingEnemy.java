package com.ledzinygamedevelopment.fallingman.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;

public class WalkingEnemy extends Sprite implements InteractiveObjectInterface {

    private GameScreen gameScreen;
    private float posX;
    private float posY;
    private Body body;
    private World world;
    private int speed;
    private boolean touched;
    private boolean toRemove;
    private boolean changeDirection;
    private Fixture fixture;

    public WalkingEnemy(PlayScreen playScreen, World world, float posX, float posY, boolean right) {
        this.world = world;
        this.gameScreen = playScreen;
        this.posX = posX;
        this.posY = posY;
        setBounds(0, 0, 200 / FallingMan.PPM, 485 / FallingMan.PPM);
        setRegion(new TextureRegion(playScreen.getDefaultAtlas().findRegion("walking_enemy"), 0, 0, 200, 485));
        setPosition(posX, posY);
        setOrigin(getWidth() / 2, getHeight() / 2);
        touched = false;
        toRemove = false;
        changeDirection = false;
        defineBody();
        if (right)
            speed = 2;
        else
            speed = -2;
    }

    public void defineBody() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(posX, posY + 241.5f / FallingMan.PPM);
        body = world.createBody(bdef);
        shape.setAsBox(100 / FallingMan.PPM, 241.5f / FallingMan.PPM);
        fdef.shape = shape;
        //fdef.density = 1;
        fdef.friction = 1;
        fdef.filter.categoryBits = FallingMan.INTERACTIVE_TILE_OBJECT_BIT;
        fdef.filter.maskBits = FallingMan.DEFAULT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.WALL_INSIDE_TOWER | FallingMan.STOP_WALKING_ENEMY_BIT
                | FallingMan.PLAYER_HEAD_BIT | FallingMan.PLAYER_BELLY_BIT | FallingMan.PLAYER_ARM_BIT | FallingMan.PLAYER_FORE_ARM_BIT | FallingMan.PLAYER_HAND_BIT
                | FallingMan.PLAYER_THIGH_BIT | FallingMan.PLAYER_SHIN_BIT | FallingMan.PLAYER_FOOT_BIT;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
    }

    @Override
    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    @Override
    public void touched() {
        gameScreen.setGameOver(true);
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
    public void update(float dt) {
        if (body.getPosition().y < gameScreen.getPlayer().b2body.getPosition().y + (FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM + 242.5f / FallingMan.PPM
                && body.getPosition().y > gameScreen.getPlayer().b2body.getPosition().y - (FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM - 242.5f / FallingMan.PPM) {
            body.setAwake(true);
        } else if (body.getLinearVelocity().y >= 0) {
            body.setAwake(false);
        }
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(body.getAngle()));
        if (body.getLinearVelocity().x > -1 && body.getLinearVelocity().x < 1) {
            speed = -speed;
            changeDirection = false;
        }
        body.setLinearVelocity(body.getLinearVelocity().y >= -0.1 ? speed : 0, body.getLinearVelocity().y);
        body.applyLinearImpulse(new Vector2(0, -0.1f), body.getWorldCenter(), false);
        if (touched) {
            touched();
            toRemove = true;
        }
        //body.setTransform(new Vector2(body.getPosition().x, body.getPosition().y), 0);
    }

    @Override
    public boolean isToRemove() {
        setCategoryFilter(FallingMan.DESTROYED_BIT);
        return toRemove;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {
        //this.changeDirection = changeDirection;
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

}
