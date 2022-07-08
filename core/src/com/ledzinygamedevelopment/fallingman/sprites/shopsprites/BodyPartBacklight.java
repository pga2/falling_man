package com.ledzinygamedevelopment.fallingman.sprites.shopsprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class BodyPartBacklight extends Sprite {
    private TextureRegion backlightTexture;
    private GameScreen gameScreen;
    private float speedX;
    private float speedY;
    private float existTime;
    private boolean removeSpark;

    public BodyPartBacklight(GameScreen gameScreen, float posX, float posY) {
        //super(playScreen.getAtlas().findRegion("spark"));
        this.gameScreen = gameScreen;

        backlightTexture = new TextureRegion(gameScreen.getDefaultAtlas().findRegion("body_part_backlight"), 0, 0, 256, 256);
        //sparkTexture = new TextureRegion(getTexture(), 1315, 2035, 32, 32);
        setBounds(0, 0, 256 / FallingMan.PPM, 256 / FallingMan.PPM);
        setRegion(backlightTexture);
        //setOrigin(getWidth() / 2, getHeight() / 2);
        setPosition(posX - getWidth() / 2, posY - getHeight() / 2);
        //Random random = new Random();
        //setRotation(random.nextInt(91));
    }

    public void update(float dt) {
        /*existTime += dt;
        setPosition(getX() + speedX, getY() + speedY);
        if (!(existTime < 0.3f)) {
            setColor(new Color(getColor().r, getColor().g, getColor().b, getColor().a - 0.03f));
            if (getColor().a < 0.03f) {
                removeSpark = true;
            }
        }*/
    }
}
