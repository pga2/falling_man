package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

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
        switch (getCells().size) {
            case 9:
                playScreen.getSaveData().addSpins(3);
                break;
            case 4:
            default:
                playScreen.getSaveData().addSpins(1);
                break;
        }
        for(TiledMapTileLayer.Cell cell : getCells()) {
            try {

                cell.setTile(null);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        //playScreen.createOneArmedBanditObjects();
        setCategoryFilter(FallingMan.DESTROYED_BIT);
        //playScreen.setStopRock(true);
        touched = false;
    }

    @Override
    public void draw(Batch batch) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public boolean isToRemove() {
        return false;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {

    }

}
