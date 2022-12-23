package com.ledzinygamedevelopment.fallingman.sprites.enemies.huntingspider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.dragon.DragonFire;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;
import com.ledzinygamedevelopment.fallingman.tools.Utils;

public class HuntingSpider implements InteractiveObjectInterface {

    private AnimationState animationState;
    private Skeleton skeleton;
    private World world;
    private Body body;
    private Fixture fixture;
    private FixtureDef fdef;
    private boolean touched;

    private PlayScreen playScreen;
    private float posX;
    private float posY;
    private boolean toRemove;
    private float aliveTimer;
    private boolean bodyExists;
    private boolean animationIdle;

    public HuntingSpider(World world, PlayScreen playScreen, float posX, float posY) {
        this.world = world;
        this.playScreen = playScreen;
        this.posX = posX;
        this.posY = posY;

        defineBody();

        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);

        /*setBounds(0, 0, 128 / FallingMan.PPM, 128 / FallingMan.PPM);
        setRegion(new TextureRegion(playScreen.getDefaultAtlas().findRegion("spider"), 0, 0, 128, 128));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setFlip(false, true);*/
        TextureAtlas bigRockAtlas = playScreen.getSpiderAtlas();
        SkeletonJson json = new SkeletonJson(bigRockAtlas);
        json.setScale(1 / FallingMan.PPM);
        SkeletonData rockSkeletonData = json.readSkeletonData(Gdx.files.internal("spine_animations/spider.json"));
        AnimationStateData rockAnimationStateData = new AnimationStateData(rockSkeletonData);
        skeleton = new Skeleton(rockSkeletonData);
        animationState = new AnimationState(rockAnimationStateData);
        animationState.setAnimation(0, "walk", true);
        animationIdle = false;

        aliveTimer = 0;

        toRemove = false;
        bodyExists = true;
    }

    public void defineBody() {
        BodyDef bdef = new BodyDef();
        fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(posX, posY);
        body = world.createBody(bdef);
        shape.setAsBox(64 / FallingMan.PPM, 64 / FallingMan.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;
        //fdef.filter.maskBits = FallingMan.WALL_INSIDE_TOWER | FallingMan.
        fixture = body.createFixture(fdef);
        touched = false;

        fixture.setUserData(this);
        body.setGravityScale(0);

    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    @Override
    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    @Override
    public void touched() {
        playScreen.setGameOver(true);
        toRemove = true;
    }

    @Override
    public boolean isTouched() {
        return false;
    }

    @Override
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public void draw(Batch batch) {

        skeleton.updateWorldTransform();

        playScreen.getSkeletonRenderer().draw(batch, skeleton);
    }

    @Override
    public void update(float dt) {



        //setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        //setRotation((float) Math.toDegrees(body.getAngle()));
        if (touched) {
            touched();
        }

        if (aliveTimer > 20) {
            toRemove = true;
        } else if (aliveTimer > 19 && bodyExists) {
            setCategoryFilter(FallingMan.DEFAULT_BIT);
            bodyExists = false;
        } else if (aliveTimer > 19) {
            //setColor(getColor().r, getColor().g, getColor().b, 20 - aliveTimer);
            skeleton.setColor(new Color(skeleton.getColor().r, skeleton.getColor().g, skeleton.getColor().b, 20 - aliveTimer));
        }
        if (body.getLinearVelocity().y < 0.01f && body.getLinearVelocity().y > -0.01f
                && body.getLinearVelocity().x < 0.01f && body.getLinearVelocity().x > -0.01f) {
            body.setAngularVelocity(0);
            if (!animationIdle) {
                animationState.setAnimation(0, "idle", true);
                animationIdle = true;
            }
        } else if (animationIdle) {
            animationState.setAnimation(0, "walk", true);
            animationIdle = false;
        }

        aliveTimer += dt;
        skeleton.setPosition(body.getPosition().x, body.getPosition().y - 64 / FallingMan.PPM);
        skeleton.getRootBone().setRotation((float) Math.toDegrees(body.getAngle()) - 180);
        if (animationIdle) {
            animationState.update(dt);
        } else {
            animationState.update(dt * Math.abs(body.getLinearVelocity().y * 0.25f) + dt * Math.abs(body.getLinearVelocity().x * 0.25f));
        }
        animationState.apply(skeleton);
    }

    public void generateMapSpiderUpdate(Vector2 playerPosPrevious, int mapHeight) {
        float yDiff;
        if (body.getPosition().y < playerPosPrevious.y) {
            yDiff = -Math.abs(Math.abs(body.getPosition().y) - Math.abs(playerPosPrevious.y));
        } else {
            yDiff = Math.abs(Math.abs(body.getPosition().y) - Math.abs(playerPosPrevious.y));
        }

        float previosBodyAngle = body.getAngle();

        //Teleporting body part
        body.setTransform(body.getPosition().x, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM + yDiff, previosBodyAngle);
    }

    @Override
    public boolean isToRemove() {
        return toRemove;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {

    }
}
