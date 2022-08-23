package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coins;

import com.badlogic.gdx.Gdx;
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

    public Spark(PlayScreen playScreen, float posX, float posY, byte typeOfSpark) {
        this.playScreen = playScreen;
        if (typeOfSpark == 1) { //1 for gold spark
            sparkTexture = new TextureRegion(playScreen.getDefaultAtlas().findRegion("spark"), 0, 0, 32, 32);
            //sparkTexture = new TextureRegion(getTexture(), 1315, 2035, 32, 32);
            setBounds(0, 0, 32 / FallingMan.PPM, 32 / FallingMan.PPM);
            setPosition(posX - 16 / FallingMan.PPM, posY - 16 / FallingMan.PPM);

        } else if (typeOfSpark == 2) { //2 for new life spark
            sparkTexture = new TextureRegion(playScreen.getDefaultAtlas().findRegion("spark_new_life"), 0, 0, 64, 64);
            //sparkTexture = new TextureRegion(getTexture(), 1315, 2035, 32, 32);
            setBounds(0, 0, 64 / FallingMan.PPM, 64 / FallingMan.PPM);
            setPosition(posX - 32 / FallingMan.PPM, posY - 32 / FallingMan.PPM);
        } else if (typeOfSpark == 3) { //2 for new life spark
            sparkTexture = new TextureRegion(playScreen.getDefaultAtlas().findRegion("spark_hounting_enemy"), 0, 0, 64, 64);
            //sparkTexture = new TextureRegion(getTexture(), 1315, 2035, 32, 32);
            setBounds(0, 0, 64 / FallingMan.PPM, 64 / FallingMan.PPM);
            setPosition(posX - 32 / FallingMan.PPM, posY - 32 / FallingMan.PPM);
        } else if (typeOfSpark == 4) { //2 for new life spark
            sparkTexture = new TextureRegion(playScreen.getDefaultAtlas().findRegion("spark_spin"), 0, 0, 32, 32);
            //sparkTexture = new TextureRegion(getTexture(), 1315, 2035, 32, 32);
            setBounds(0, 0, 32 / FallingMan.PPM, 32 / FallingMan.PPM);
            setPosition(posX - 16 / FallingMan.PPM, posY - 16 / FallingMan.PPM);
        }
        setRegion(sparkTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
        Random random = new Random();
        setRotation(random.nextInt(91));
        speedX = (random.nextFloat() - 0.5f) / 5;
        speedY = (random.nextFloat() - 0.5f) / 5;
        removeSpark = false;
    }

    public void update(float dt) {
        existTime += dt;
        setPosition(getX() + speedX * 60 * Gdx.graphics.getDeltaTime(), getY() + speedY * 60 * Gdx.graphics.getDeltaTime());
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
