package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.font.FontMapObject;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;

import java.util.Random;

public class Coin extends Sprite implements InteractiveObjectInterface {
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
    private boolean animationReversed;

    private PlayScreen playScreen;
    private boolean toRemove;
    private boolean flip;

    public Coin(World world, TiledMap map, Rectangle bounds, int mapLayer, PlayScreen playScreen) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;
        this.mapLayer = mapLayer;


        toRemove = false;

        Array<TextureRegion> textureRegions = new Array<>();
        for (int i = 1; i < 9; i++) {
            textureRegions.add(new TextureRegion(playScreen.getDefaultAtlas().findRegion("coin" + i), 0, 0, 64, 64));
        }

        animation = new Animation(0.07f, textureRegions);
        animationTimer = 0.0001f;

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
        this.playScreen = playScreen;
        fixture.setUserData(this);
        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);

        setBounds(0, 0, 64 / FallingMan.PPM, 64 / FallingMan.PPM);
        setRegion(new TextureRegion(playScreen.getDefaultAtlas().findRegion("coin1"), 0, 0, 64, 64));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);
        animationReversed = false;
        flip = false;
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
        setFlip(flip, false);
    }

    @Override
    public boolean isToRemove() {
        return toRemove;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {

    }

    private TextureRegion getFrame(float dt) {
        if (!animationReversed) {
            if (animationTimer + dt > animation.getAnimationDuration()) {
                animationReversed = true;
                flip = true;
            } else {
                animationTimer += dt;
                flip = false;
            }
        } else {
            if (animationTimer - dt < 0) {
                animationReversed = false;
                flip = false;
            } else {
                animationTimer -= dt;
                flip = true;
            }
        }
        //Gdx.app.log("index: ", String.valueOf(animation.getKeyFrameIndex(animationTimer)));
        return (TextureRegion) animation.getKeyFrame(animationTimer);
    }

    public void touched() {

        if (playScreen.getSaveData().getSounds()) {
            playScreen.getAssetManager().getTouchCoinSound().play();
        }
        Random random = new Random();
        int amountOfGold = random.nextInt(6) + 5;
        //int amountOfAllGold = 0;

        setCategoryFilter(FallingMan.DESTROYED_BIT);
        /*for(TiledMapTileLayer.Cell cell : getCells()) {
            cell.setTile(null);
            amountOfAllGold += amountOfGold;
            for (int i = 0; i < 6; i++) {
                playScreen.getSparks().add(new Spark(playScreen, body.getPosition().x, body.getPosition().y));
            }
        }*/
        for (int i = 0; i < 24; i++) {
            playScreen.getSparks().add(new Spark(playScreen, body.getPosition().x, body.getPosition().y, (byte) 5, false));
        }

        playScreen.getHud().setGold(playScreen.getHud().getGold() + amountOfGold);

        playScreen.getFontMapObjects().add(new FontMapObject(body.getPosition().x, body.getPosition().y, String.valueOf(amountOfGold), new Color(174 / 255f, 132 / 255f, 26 / 255f, 1)));
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public Body getBody() {
        return body;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
        if (touched) {
            toRemove = true;
        }
    }
}
