package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;

public class DeadMachine extends InteractiveTileObject{

    public DeadMachine(World world, TiledMap map, Rectangle bounds, int mapLayer) {
        super(world, map, bounds, mapLayer);
        fixture.setUserData(this);
        setCategoryFilter(FallingMan.DEAD_MACHINE_BIT);
    }

    @Override
    public void touched() {
        setCategoryFilter(FallingMan.DESTROYED_BIT);
    }

}
