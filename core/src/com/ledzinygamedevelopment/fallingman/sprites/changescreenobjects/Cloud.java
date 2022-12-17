package com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class Cloud extends Sprite {
    private TextureRegion cloudTexture;
    private boolean secondScreen;
    private byte screen;
    private GameScreen gameScreen;



    public Cloud(GameScreen gameScreen, float posX, float posY, boolean secondScreen, byte screen) {
        this.gameScreen = gameScreen;

        cloudTexture = new TextureRegion(gameScreen.getDefaultAtlas().findRegion("cloud"), 0, 0, 960, 384);
        this.secondScreen = secondScreen;
        this.screen = screen;
        setBounds(0, 0, 960 / FallingMan.PPM, 384 / FallingMan.PPM);
        setRegion(cloudTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setPosition(posX - getWidth() / 2, posY - getHeight() / 2);
        setFlip(false, random.nextBoolean());
    }

    public void update(float dt, float speedX, float speedY) {
        setPosition(getX() + speedX, getY() + speedY);
    }

    public boolean isSecondScreen() {
        return secondScreen;
    }

    public byte getScreen() {
        return screen;
    }
}
