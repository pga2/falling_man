package com.ledzinygamedevelopment.fallingman.sprites.font;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontMapObject {

    private float posX;
    private float posY;
    private String text;

    public FontMapObject(float posX, float posY, String text) {
        this.posX = posX;
        this.posY = posY;
        this.text = text;
    }

    public void update(float dt) {

    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public String getText() {
        return text;
    }
}
