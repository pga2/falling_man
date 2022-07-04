package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.teleports;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObject;

public class TeleportTarget extends InteractiveTileObject {
    private PlayScreen playScreen;
    private int id;

    public TeleportTarget(World world, TiledMap map, Rectangle bounds, int mapLayer, PlayScreen playScreen, int id) {
        super(world, map, bounds, mapLayer);
        this.playScreen = playScreen;
        this.id = id;
        fixture.setUserData(this);
        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);
    }

    @Override
    public void touched() {
        Gdx.app.log("teleport target", "");
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

    public int getId() {
        return id;
    }
}
