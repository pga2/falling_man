package com.ledzinygamedevelopment.fallingman.sprites.enemies.dragon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;

public class Dragon extends Sprite implements InteractiveObjectInterface {

    private final DragonFire dragonFire;
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

    public Dragon(World world, Rectangle bounds, PlayScreen playScreen, boolean rightSideFire) {
        this.world = world;
        this.bounds = bounds;
        this.playScreen = playScreen;
        this.rightSideFire = rightSideFire;

        defineBody();

        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);

        setBounds(0, 0, 256 / FallingMan.PPM, 256 / FallingMan.PPM);
        setRegion((Texture) playScreen.getAssetManager().getManager().get(playScreen.getAssetManager().getPlayScreenDragon()));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);

        if (!rightSideFire) {
            setFlip(true, false);
            dragonFire = new DragonFire(playScreen, getX() - 112 / FallingMan.PPM - getWidth() / 2, getY() + 47 / FallingMan.PPM);
            dragonFire.setFlip(true, false);
        } else {
            dragonFire = new DragonFire(playScreen, getX() + 112 / FallingMan.PPM + getWidth() * 3 / 2, getY() + 47 / FallingMan.PPM);
        }

        animationTimer = 0;

        toRemove = false;
        fireExist = true;

    }

    @Override
    public void update(float dt) {
        animationTimer += dt;
        if (animationTimer < 3) {
            if (!fireExist) {
                setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT, fireFixture);
                fireExist = true;
            }
        } else if (animationTimer < 6) {
            if (fireExist) {
                setCategoryFilter(FallingMan.DESTROYED_BIT, fireFixture);
                fireExist = false;
            }
        } else {
            animationTimer = 0;
        }
    }

    @Override
    public boolean isToRemove() {
        return toRemove;
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
        super.draw(batch);
        if (fireExist)
            dragonFire.draw(batch);
    }


}
