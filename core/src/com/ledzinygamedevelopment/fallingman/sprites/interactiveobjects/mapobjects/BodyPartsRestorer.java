package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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

import java.util.Random;

public class BodyPartsRestorer extends Sprite implements InteractiveObjectInterface {

    private PlayScreen playScreen;
    private Body body;
    private Fixture fixture;
    private FixtureDef fdef;
    private boolean touched;
    private boolean draw;



    public BodyPartsRestorer(World world, TiledMap map, Rectangle bounds, int mapLayer, PlayScreen playScreen) {
        this.playScreen = playScreen;
        //fixture.setUserData(this);
        //setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);

        //
        //animation = new Animation(0.1f, textureRegions);
        //animationTimer = 0.0001f;

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
        setRegion(new TextureRegion(playScreen.getDefaultAtlas().findRegion("body_restorer"), 0, 0, 64, 64));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);

        draw = true;
    }



    @Override
    public void touched() {
        if (playScreen.getSaveData().getSounds()) {
            playScreen.getAssetManager().getTouchReviveSound().play();
        }
        playScreen.getPlayer().restoreBodyParts(playScreen.getMapHeight());
        setCategoryFilter(FallingMan.DESTROYED_BIT);
        draw = false;
        /*for(TiledMapTileLayer.Cell cell : getCells()) {
            try {
                cell.setTile(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void update(float dt) {
        if (draw) {
            setRotation(getRotation() - 180 * dt);
        }
    }

    @Override
    public boolean isToRemove() {
        return false;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {

    }

    @Override
    public void draw(Batch batch) {
        if (draw) {
            super.draw(batch);
        }
    }

    @Override
    public void setTouched(boolean touched) {
        this.touched = touched;
    }


    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public Body getBody() {
        return body;
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
