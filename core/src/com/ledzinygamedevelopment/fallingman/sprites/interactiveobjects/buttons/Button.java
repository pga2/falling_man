package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public abstract class Button extends Sprite {

    protected GameScreen gameScreen;
    protected World world;
    protected float posX;
    protected float posY;
    protected float width;
    protected float height;
    protected boolean clicked;
    protected boolean locked;

    public Button(GameScreen gameScreen, World world, float posX, float posY, float width, float height) {
        this.gameScreen = gameScreen;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        clicked = false;
        locked = false;
    }

    public void update(float dt, Vector2 pos) {
        posX = pos.x;
        posY = pos.y;
        setPosition(pos.x, pos.y);
    }

    public abstract void touched();

    public abstract void notTouched();

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

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
