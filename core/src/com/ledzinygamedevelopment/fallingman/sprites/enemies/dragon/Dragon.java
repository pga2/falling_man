package com.ledzinygamedevelopment.fallingman.sprites.enemies.dragon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;
import com.ledzinygamedevelopment.fallingman.tools.Utils;

public class Dragon extends Sprite implements InteractiveObjectInterface {

    //private final DragonFire dragonFire;
    private World world;
    private TiledMap map;
    private TiledMapTile tile;
    private Rectangle bounds;
    private int mapLayer;
    private Body body;
    private Fixture fixture;
    private FixtureDef fdef;
    private boolean touched;
    private Animation animation;
    private float animationTimer;
    private final boolean rightSideFire;

    private PlayScreen playScreen;
    private boolean toRemove;
    private Body fireBody;
    private Fixture fireFixture;
    private boolean fireExist;
    private FixtureDef fireFdef;

    private Skeleton skeleton;
    private AnimationState animationState;
    private boolean soundsPlayed;
    private long soundId;

    public Dragon(World world, Rectangle bounds, PlayScreen playScreen, boolean rightSideFire) {
        this.world = world;
        this.bounds = bounds;
        this.playScreen = playScreen;
        this.rightSideFire = rightSideFire;

        defineBody();

        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);

        setBounds(0, 0, 256 / FallingMan.PPM, 256 / FallingMan.PPM);
        //setRegion((Texture) playScreen.getAssetManager().getManager().get(playScreen.getAssetManager().getPlayScreenDragon()));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);



        animationTimer = 0;

        toRemove = false;
        fireExist = true;

        TextureAtlas dragonAtlas = playScreen.getDragonAtlas();
        SkeletonJson json = new SkeletonJson(dragonAtlas);
        json.setScale(1 / FallingMan.PPM);
        SkeletonData dragonSkeletonData = json.readSkeletonData(Gdx.files.internal("spine_animations/dragon.json"));

        AnimationStateData rockAnimationStateData = new AnimationStateData(dragonSkeletonData);
        skeleton = new Skeleton(dragonSkeletonData);
        skeleton.setPosition(body.getPosition().x, body.getPosition().y - getHeight() / 2);
        animationState = new AnimationState(rockAnimationStateData);
        animationState.setAnimation(0, "attack with fire", true);

        if (!rightSideFire) {
            setFlip(true, false);
            //dragonFire = new DragonFire(playScreen, getX() - 112 / FallingMan.PPM - getWidth() / 2, getY() + 47 / FallingMan.PPM);
            //dragonFire.setFlip(true, false);
            skeleton.setScaleX(-1);
        } else {
            //dragonFire = new DragonFire(playScreen, getX() + 112 / FallingMan.PPM + getWidth() * 3 / 2, getY() + 47 / FallingMan.PPM);
        }
    }

    @Override
    public void update(float dt) {

        if (playScreen.getSaveData().getSounds()) {
            float distToSound = 8.5f;
            if (Utils.getDistBetweenBodies(body, playScreen.getPlayer().b2body) < distToSound && !soundsPlayed) {
                playScreen.getAssetManager().getDragonSound().stop(soundId);
                soundId = playScreen.getAssetManager().getDragonSound().play();
                playScreen.getAssetManager().getDragonSound().setLooping(soundId, true);
                soundsPlayed = true;
            } else if (Utils.getDistBetweenBodies(body, playScreen.getPlayer().b2body) > distToSound + 2) {
                playScreen.getAssetManager().getDragonSound().stop(soundId);
                soundsPlayed = false;
            } else {
                playScreen.getAssetManager().getDragonSound().setVolume(soundId, 1 - Utils.getDistBetweenBodies(body, playScreen.getPlayer().b2body) / distToSound);
            }
            if (playScreen.getDefaultWindows().size > 0) {
                playScreen.getAssetManager().getDragonSound().setVolume(soundId, 0);
            }
        }

        animationTimer += dt;
        if (animationTimer < 3) {
            if (!fireExist) {
                setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT, fireFixture);
                fireExist = true;
                animationState.setAnimation(0, "attack with fire", true);
            }
        } else if (animationTimer < 6) {
            if (fireExist) {
                setCategoryFilter(FallingMan.DESTROYED_BIT, fireFixture);
                fireExist = false;
                animationState.setAnimation(0, "idle", true);
            }
        } else {
            animationTimer = 0;
        }
        animationState.update(dt);
        animationState.apply(skeleton);
    }

    @Override
    public boolean isToRemove() {
        return toRemove;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {

    }

    public void defineBody() {
        BodyDef bdef = new BodyDef();
        fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / FallingMan.PPM, (bounds.getY() + bounds.getHeight() / 2) / FallingMan.PPM);
        body = world.createBody(bdef);
        shape.setAsBox((bounds.getWidth() / 2)  / FallingMan.PPM, (bounds.getHeight() / 2)  / FallingMan.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;
        fixture = body.createFixture(fdef);
        touched = false;
        fixture.setUserData(this);
        defineFireBody();

    }

    public void defineFireBody() {
        BodyDef fireBdef = new BodyDef();
        fireFdef = new FixtureDef();

        PolygonShape fireShape = new PolygonShape();
        if (rightSideFire) {
            float[] shapeVertices = {112 / FallingMan.PPM, 30 / FallingMan.PPM,
                    112 / FallingMan.PPM, 50 / FallingMan.PPM,
                    624 / FallingMan.PPM, 106 / FallingMan.PPM,
                    624 / FallingMan.PPM, -86 / FallingMan.PPM};
            fireShape.set(shapeVertices);
        } else {
            float[] shapeVertices = {-112 / FallingMan.PPM, 30 / FallingMan.PPM,
                    -112 / FallingMan.PPM, 50 / FallingMan.PPM,
                    -624 / FallingMan.PPM, 106 / FallingMan.PPM,
                    -624 / FallingMan.PPM, -86 / FallingMan.PPM};
            fireShape.set(shapeVertices);
        }

        fireBdef.type = BodyDef.BodyType.StaticBody;
        fireBdef.position.set((bounds.getX() + bounds.getWidth() / 2) / FallingMan.PPM, (bounds.getY() + bounds.getHeight() / 2) / FallingMan.PPM);
        fireBody = world.createBody(fireBdef);
        fireFdef.shape = fireShape;
        fireFdef.isSensor = true;
        fireFixture = body.createFixture(fireFdef);
        fireFixture.setUserData(this);
        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT, fireFixture);
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public void setCategoryFilter(short filterBit, Fixture fixture) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    @Override
    public void setTouched(boolean touched) {
        this.touched = touched;
        if (touched) {
            toRemove = true;
            playScreen.getAssetManager().getDragonSound().stop(soundId);
        }
    }

    @Override
    public void touched() {
        setCategoryFilter(FallingMan.DESTROYED_BIT);
        Gdx.app.log("dziala", "smok");

        playScreen.setGameOver(true);
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
        //super.draw(batch);
        /*if (fireExist)
            dragonFire.draw(batch);*/
        skeleton.updateWorldTransform();

        playScreen.getSkeletonRenderer().draw(batch, skeleton);
    }


}
