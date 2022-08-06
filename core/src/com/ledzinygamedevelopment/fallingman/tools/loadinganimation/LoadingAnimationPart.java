package com.ledzinygamedevelopment.fallingman.tools.loadinganimation;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

import java.util.Random;

public class LoadingAnimationPart extends Sprite {

    private final TextureRegion texture;
    private GameScreen gameScreen;

    private float rColor;
    private float gColor;
    private float bColor;
    private float aColor;

    public LoadingAnimationPart(GameScreen gameScreen, String partName, float posX, float posY) {
        this.gameScreen = gameScreen;

        rColor = 1;
        gColor = 1;
        bColor = 1;
        aColor = 1;

        //super(playScreen.getAtlas().findRegion("spark"));

        texture = new TextureRegion(gameScreen.getDefaultAtlas().findRegion("loading_animation" + partName), 0, 0, 96, 96);
        //sparkTexture = new TextureRegion(getTexture(), 1315, 2035, 32, 32);
        setBounds(0, 0, 96 / FallingMan.PPM, 96 / FallingMan.PPM);
        setRegion(texture);
        setPosition(posX - 48 / FallingMan.PPM, posY - 48 / FallingMan.PPM);
        setOrigin(getWidth() / 2, getHeight() / 2);

        setColor(rColor, gColor, bColor, aColor);
    }

    public void updateColor() {
        setColor(rColor, gColor, bColor, aColor);
    }

    public float getrColor() {
        return rColor;
    }

    public void setrColor(float rColor) {
        if (rColor > this.rColor) {
            this.gColor = this.gColor + (rColor - this.rColor) / 2;
            this.bColor = this.bColor + (rColor - this.rColor) / 2;
        } else {
            this.gColor = this.gColor - (this.rColor - rColor) / 2;
            this.bColor = this.bColor - (this.rColor - rColor) / 2;
        }
        this.rColor = rColor;
    }

    public float getgColor() {
        return gColor;
    }

    public void setgColor(float gColor) {
        this.gColor = gColor;
    }

    public float getbColor() {
        return bColor;
    }

    public void setbColor(float bColor) {
        this.bColor = bColor;
    }

    public float getaColor() {
        return aColor;
    }

    public void setaColor(float aColor) {
        this.aColor = aColor;
    }
}
