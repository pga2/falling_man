package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.Roll;

public class Spins extends InteractiveTileObject{
    private PlayScreen playScreen;

    public Spins(PlayScreen playScreen, World world, TiledMap map, Rectangle bounds, int mapLayer) {
        super(world, map, bounds, mapLayer);
        this.playScreen = playScreen;

        fixture.setUserData(this);
        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);
    }

    @Override
    public void touched() {
        for(TiledMapTileLayer.Cell cell : getCells()) {
            try {

                cell.setTile(null);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        playScreen.createOneArmedBanditObjects();
        setCategoryFilter(FallingMan.DESTROYED_BIT);
        touched = false;
    }
}
