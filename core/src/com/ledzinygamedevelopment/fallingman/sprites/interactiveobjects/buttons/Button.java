package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;

public abstract class Button extends Sprite {

    protected BodyDef bdef;
    protected Body b2body;
    protected FixtureDef fdef;
    protected Rectangle bounds;
    protected World world;
    protected TiledMap map;
    private int mapLayer;

    public Button(Rectangle bounds, World world, TiledMap map, int mapLayer) {
        this.bounds = bounds;
        this.world = world;
        this.map = map;
        this.mapLayer = mapLayer;


        defineButton(bounds);
    }

    public abstract void defineButton(Rectangle bounds);

    public void removeCells() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(mapLayer);
        Array<TiledMapTileLayer.Cell> cells = new Array<>();

        int sizeWidth = (int) bounds.getWidth() / FallingMan.CELL_SIZE / 2;
        int maxSizeWidth = (bounds.getWidth() / FallingMan.CELL_SIZE) % 2 == 0 ? sizeWidth - 1 : sizeWidth;
        int sizeHeight = (int) bounds.getHeight() / FallingMan.CELL_SIZE / 2;
        int maxSizeHeight = (bounds.getHeight() / FallingMan.CELL_SIZE) % 2 == 0 ? sizeHeight - 1 : sizeHeight;

        for(int i = -sizeWidth; i <= maxSizeWidth; i++) {
            for(int j = -sizeHeight; j <= maxSizeHeight; j++) {
                cells.add(layer.getCell((int) (((b2body.getPosition().x * FallingMan.PPM) + (i * FallingMan.CELL_SIZE)) / FallingMan.CELL_SIZE),
                        (int) (((b2body.getPosition().y * FallingMan.PPM) + (j * FallingMan.CELL_SIZE)) / FallingMan.CELL_SIZE)));
            }
        }

        for(TiledMapTileLayer.Cell cell : cells) {
            try {
                cell.setTile(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
/*
    // checking if mouse position equals button position
    public boolean mouseOver(Vector2 mousePosition) {
        if(mousePosition.x > posX - width / 2 * scale + width / 2 && mousePosition.x < posX + width / 2 * scale + width / 2
                && mousePosition.y > posY - height / 2 * scale + height / 2 && mousePosition.y < posY + height / 2 * scale + height / 2)
            return true;
        else
            return false;
    }*/

    public abstract Body getB2body();

}
