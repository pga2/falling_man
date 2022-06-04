package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.teleports;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObject;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;

public class Teleport extends InteractiveTileObject {
    private PlayScreen playScreen;
    private int id;

    public Teleport(World world, TiledMap map, Rectangle bounds, int mapLayer, PlayScreen playScreen, int id) {
        super(world, map, bounds, mapLayer);
        this.playScreen = playScreen;
        this.id = id;
        fixture.setUserData(this);
        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);
    }

    @Override
    public void touched() {
        for (TeleportTarget teleportTarget : playScreen.getB2WorldCreator().getTeleportsTarget()) {
            if (teleportTarget.getId() == id) {
                playScreen.getPlayer().setCurrentStateToTeleport(teleportTarget, this);
            }
        }
        setCategoryFilter(FallingMan.DEFAULT_BIT);
        /*for (TeleportTarget teleportTarget : playScreen.getB2WorldCreator().getTeleportsTarget()) {
            if (teleportTarget.getId() == id) {
                playScreen.getPlayer().setCurrentStateToTeleport(teleportTarget);
                float yDist = teleportTarget.getBody().getPosition().y - body.getPosition().y;
                float xDist = teleportTarget.getBody().getPosition().x - body.getPosition().x;
                playScreen.getPlayer().b2body.setTransform(playScreen.getPlayer().b2body.getPosition().x + xDist, playScreen.getPlayer().b2body.getPosition().y + yDist, playScreen.getPlayer().b2body.getAngle());

                for (Body body : playScreen.getPlayer().getBodyPartsAll()) {
                    body.setTransform(body.getPosition().x + xDist, body.getPosition().y + yDist, body.getAngle());
                }
            }
        }*/
    }
}
