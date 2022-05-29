package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects;

import com.badlogic.gdx.Gdx;
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

import java.util.LinkedList;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected int mapLayer;
    protected Body body;
    protected Fixture fixture;
    protected FixtureDef fdef;
    protected boolean touched;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds, int mapLayer) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;
        this.mapLayer = mapLayer;

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
    }

    public abstract void touched();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public Array<TiledMapTileLayer.Cell> getCells() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(mapLayer);
        Array<TiledMapTileLayer.Cell> cells = new Array<>();

        int sizeWidth = (int) bounds.getWidth() / FallingMan.CELL_SIZE / 2;
        int maxSizeWidth = (bounds.getWidth() / FallingMan.CELL_SIZE) % 2 == 0 ? sizeWidth - 1 : sizeWidth;
        int sizeHeight = (int) bounds.getHeight() / FallingMan.CELL_SIZE / 2;
        int maxSizeHeight = (bounds.getHeight() / FallingMan.CELL_SIZE) % 2 == 0 ? sizeHeight - 1 : sizeHeight;
        Gdx.app.log("1 " , String.valueOf(sizeWidth));

        Gdx.app.log("2 " , String.valueOf(maxSizeWidth));

        Gdx.app.log("3 " , String.valueOf(sizeHeight));

        Gdx.app.log("4 " , String.valueOf(maxSizeHeight));

        for(int i = -sizeWidth; i <= maxSizeWidth; i++) {
            for(int j = -sizeHeight; j <= maxSizeHeight; j++) {
                cells.add(layer.getCell((int) (((body.getPosition().x * FallingMan.PPM) + (i * FallingMan.CELL_SIZE)) / FallingMan.CELL_SIZE),
                        (int) (((body.getPosition().y * FallingMan.PPM) + (j * FallingMan.CELL_SIZE)) / FallingMan.CELL_SIZE)));
            }
        }

        return cells;
    }

    public Body getBody() {
        return body;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }
}
