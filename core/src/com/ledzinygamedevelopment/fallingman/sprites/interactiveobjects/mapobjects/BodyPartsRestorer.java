package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

import java.util.Random;

public class BodyPartsRestorer extends InteractiveTileObject{
    private PlayScreen playScreen;

    public BodyPartsRestorer(World world, TiledMap map, Rectangle bounds, int mapLayer, PlayScreen playScreen) {
        super(world, map, bounds, mapLayer);
        this.playScreen = playScreen;
        fixture.setUserData(this);
        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);
    }

    @Override
    public void touched() {
        playScreen.getPlayer().restoreBodyParts();
        setCategoryFilter(FallingMan.DESTROYED_BIT);
        for(TiledMapTileLayer.Cell cell : getCells()) {
            cell.setTile(null);
        }
    }
}
