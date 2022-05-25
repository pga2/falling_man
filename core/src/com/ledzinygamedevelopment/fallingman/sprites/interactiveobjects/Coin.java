package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;

import java.util.LinkedList;

public class Coin extends InteractiveTileObject{

    public Coin(World world, TiledMap map, Rectangle bounds, int mapLayer) {
        super(world, map, bounds, mapLayer);
        fixture.setUserData(this);
        setCategoryFilter(FallingMan.COIN_BIT);
    }

    @Override
    public void touched() {
        setCategoryFilter(FallingMan.DESTROYED_BIT);
        for(TiledMapTileLayer.Cell cell : getCells()) {
            cell.setTile(null);
        }
    }
}
