package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coin;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.font.FontMapObject;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObject;

import java.util.Random;

public class Coin extends InteractiveTileObject {

    private PlayScreen playScreen;

    public Coin(World world, TiledMap map, Rectangle bounds, int mapLayer, PlayScreen playScreen) {
        super(world, map, bounds, mapLayer);
        this.playScreen = playScreen;
        fixture.setUserData(this);
        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);
    }

    @Override
    public void touched() {
        Random random = new Random();
        int amountOfGold = random.nextInt(80) + 60;
        int amountOfAllGold = 0;

        setCategoryFilter(FallingMan.DESTROYED_BIT);
        for(TiledMapTileLayer.Cell cell : getCells()) {
            cell.setTile(null);
            playScreen.getHud().setGold(playScreen.getHud().getGold() + amountOfGold);
            amountOfAllGold += amountOfGold;
            for (int i = 0; i < 6; i++) {
                playScreen.getSparks().add(new Spark(playScreen, body.getPosition().x, body.getPosition().y));
            }
        }

        playScreen.getFontMapObjects().add(new FontMapObject(body.getPosition().x, body.getPosition().y, String.valueOf(amountOfAllGold)));
    }
}
