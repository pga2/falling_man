package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public abstract class Button extends Sprite {

    protected BodyDef bdef;
    protected Body b2body;
    protected FixtureDef fdef;
    protected PlayScreen playScreen;
    protected World world;
    protected float posX;
    protected float posY;
    protected float width;
    protected float height;
    protected boolean clicked;

    public Button(PlayScreen playScreen, World world, float posX, float posY, float width, float height) {
        this.playScreen = playScreen;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        clicked = false;
    }

    public abstract void touched();

    // checking if mouse position equals button position
    public boolean mouseOver(Vector2 mousePosition) {
        if(mousePosition.x > posX && mousePosition.x < posX + width
                && mousePosition.y > posY && mousePosition.y < posY + height)
            return true;
        else
            return false;
    }

    public boolean isClicked() {
        return clicked;
    }
}
