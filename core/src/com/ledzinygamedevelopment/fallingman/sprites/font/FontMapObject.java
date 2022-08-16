package com.ledzinygamedevelopment.fallingman.sprites.font;

import com.badlogic.gdx.graphics.Color;

public class FontMapObject {

    private float posX;
    private float posY;
    private String text;
    private Color color;

    public FontMapObject(float posX, float posY, String text, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        this.color = color;
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

    public Color getColor() {
        return color;
    }
}
