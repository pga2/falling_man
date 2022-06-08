package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

import java.util.Random;

public class Spark extends Sprite {
    private TextureRegion sparkTexture;
    private PlayScreen playScreen;
    private float speedX;
    private float speedY;
    private float existTime;
    private boolean removeSpark;

    public Spark(PlayScreen playScreen, float posX, float posY) {
        //super(playScreen.getAtlas().findRegion("spark"));
        this.playScreen = playScreen;

        sparkTexture = new TextureRegion(playScreen.getAtlas().findRegion("spark"), 0, 0, 32, 32);
        //sparkTexture = new TextureRegion(getTexture(), 1315, 2035, 32, 32);
        setBounds(0, 0, 32 / FallingMan.PPM, 32 / FallingMan.PPM);
        setRegion(sparkTexture);
        setPosition(posX - 16 / FallingMan.PPM, posY - 16 / FallingMan.PPM);
        setOrigin(getWidth() / 2, getHeight() / 2);
        Random random = new Random();
        setRotation(random.nextInt(91));
        speedX = (random.nextFloat() - 0.5f) / 5;
        speedY = (random.nextFloat() - 0.5f) / 5;
        removeSpark = false;
    }

    public void update(float dt) {
        existTime += dt;
        setPosition(getX() + speedX, getY() + speedY);
        if (!(existTime < 0.3f)) {
            setColor(new Color(getColor().r, getColor().g, getColor().b, getColor().a - 0.03f));
            if (getColor().a < 0.03f) {
                removeSpark = true;
            }
        }
    }

    public boolean isRemoveSpark() {
        return removeSpark;
    }
}
