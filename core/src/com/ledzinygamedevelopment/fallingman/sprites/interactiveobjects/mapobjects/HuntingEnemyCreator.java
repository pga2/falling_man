package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.huntingspider.HuntingSpider;
import com.ledzinygamedevelopment.fallingman.tools.entities.B2dSteeringEntity;

public class HuntingEnemyCreator extends InteractiveTileObject {


    private final PlayScreen playScreen;

    public HuntingEnemyCreator(World world, TiledMap map, Rectangle bounds, int mapLayer, PlayScreen playScreen) {
        super(world, map, bounds, mapLayer);
        this.playScreen = playScreen;
        fixture.setUserData(this);
        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);
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
    public void touched() {
        setCategoryFilter(FallingMan.DESTROYED_BIT);
        for(TiledMapTileLayer.Cell cell : getCells()) {
            try {
                cell.setTile(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        playScreen.addHuntingSpiderAI(new Vector2(getBody().getPosition().x, getBody().getPosition().y + (FallingMan.MAX_WORLD_HEIGHT * 0.5f) / FallingMan.PPM + 128 / FallingMan.PPM));
    }
}
