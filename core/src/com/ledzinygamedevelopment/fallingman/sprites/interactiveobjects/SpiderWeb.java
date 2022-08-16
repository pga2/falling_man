package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObject;

public class SpiderWeb extends InteractiveTileObject {
    private boolean toRemove;
    private PlayScreen playScreen;
    private boolean tByBelly;

    public SpiderWeb(PlayScreen playScreen, World world, TiledMap map, Rectangle bounds, int mapLayer) {
        super(world, map, bounds, mapLayer);
        this.playScreen = playScreen;
        fixture.setUserData(this);
        //setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);
        defineFilter();
        toRemove = false;
        tByBelly = false;
    }

    @Override
    public void draw(Batch batch) {

    }

    @Override
    public void update(float dt) {
        if (touched) {
            /*if (playScreen.getPlayer().b2body.getLinearVelocity().x > 0.5f)
                playScreen.getPlayer().b2body.setLinearVelocity(0.5f, playScreen.getPlayer().b2body.getLinearVelocity().y);
            else if (playScreen.getPlayer().b2body.getLinearVelocity().x < -0.5f)
                playScreen.getPlayer().b2body.setLinearVelocity(-0.5f, playScreen.getPlayer().b2body.getLinearVelocity().y);
            if (playScreen.getPlayer().b2body.getLinearVelocity().y > 0.5f)
                playScreen.getPlayer().b2body.setLinearVelocity(playScreen.getPlayer().b2body.getLinearVelocity().x, 0.5f);
            else if (playScreen.getPlayer().b2body.getLinearVelocity().y < -0.5f)
                playScreen.getPlayer().b2body.setLinearVelocity(playScreen.getPlayer().b2body.getLinearVelocity().x, -0.5f);*/
            //playScreen.getPlayer().b2body.setLinearVelocity(playScreen.getPlayer().b2body.getLinearVelocity());
            //touched = false;
            if (playScreen.getPlayer().b2body.getLinearVelocity().x > 2f) {
                playScreen.getPlayer().b2body.setLinearVelocity(2f, playScreen.getPlayer().b2body.getLinearVelocity().y);
                for (Body body : playScreen.getPlayer().getBodyPartsAll()) {
                    body.setLinearVelocity(2f, body.getLinearVelocity().y);
                }
            } else if (playScreen.getPlayer().b2body.getLinearVelocity().x < -2f) {
                playScreen.getPlayer().b2body.setLinearVelocity(-2f, playScreen.getPlayer().b2body.getLinearVelocity().y);
                for (Body body : playScreen.getPlayer().getBodyPartsAll()) {
                    body.setLinearVelocity(-2f, body.getLinearVelocity().y);
                }
            }
            if (playScreen.getPlayer().b2body.getLinearVelocity().y > 2f) {
                playScreen.getPlayer().b2body.setLinearVelocity(playScreen.getPlayer().b2body.getLinearVelocity().x, 2f);
                for (Body body : playScreen.getPlayer().getBodyPartsAll()) {
                    body.setLinearVelocity(body.getLinearVelocity().x, 2f);
                }
            } else if (playScreen.getPlayer().b2body.getLinearVelocity().y < -2f) {
                playScreen.getPlayer().b2body.setLinearVelocity(playScreen.getPlayer().b2body.getLinearVelocity().x, -2f);
                for (Body body : playScreen.getPlayer().getBodyPartsAll()) {
                    body.setLinearVelocity(body.getLinearVelocity().x, -2f);
                }
            }
        }
    }

    @Override
    public boolean isToRemove() {
        return toRemove;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {

    }

    @Override
    public void touched() {

    }

    public void defineFilter() {
        Filter filter = new Filter();
        filter.categoryBits = FallingMan.INTERACTIVE_TILE_OBJECT_BIT;
        filter.maskBits = FallingMan.PLAYER_BELLY_BIT;
        fixture.setFilterData(filter);
    }
}
