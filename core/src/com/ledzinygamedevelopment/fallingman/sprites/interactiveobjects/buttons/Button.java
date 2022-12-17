package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public abstract class Button extends Sprite {

    protected float yPosPlayerDiff;
    protected GameScreen gameScreen;
    protected World world;
    protected float posX;
    protected float posY;
    protected float width;
    protected float height;
    protected boolean clicked;
    protected boolean locked;
    protected boolean toRemove;

    public Button(GameScreen gameScreen, World world, float posX, float posY) {
        this.gameScreen = gameScreen;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        clicked = false;
        locked = false;
        toRemove = false;
    }

    public Button(GameScreen gameScreen, World world, float posX, float posY, float width, float height) {
        this.gameScreen = gameScreen;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        clicked = false;
        locked = false;
        toRemove = false;
    }

    public void update(float dt, Vector2 pos) {
        posX = pos.x;
        posY = pos.y;
        setPosition(pos.x, pos.y);
    }

    public void touched() {
        if (!clicked && gameScreen.getSaveData().getVibrations()) {
            Gdx.input.vibrate(50);
        }
    }

    public void notTouched() {
        if (clicked && gameScreen.getSaveData().getVibrations()) {
            Gdx.input.vibrate(50);
        }
    }

    public abstract void restoreNotClickedTexture();

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

    public float getyPosPlayerDiff() {
        return yPosPlayerDiff;
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }
}
