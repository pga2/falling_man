package com.ledzinygamedevelopment.fallingman.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

import java.util.Random;

public class Smoke extends Sprite {

    private PlayScreen playScreen;
    private float posX;
    private float posY;
    private Animation animation;
    private float animationTimer;
    private boolean toRemove;

    public Smoke(PlayScreen playScreen, float posX, float posY) {
        this.playScreen = playScreen;
        this.posX = posX;
        this.posY = posY;

        Array<TextureRegion> textureRegions = new Array<>();
        for (int i = 1; i < 9; i++) {
            textureRegions.add(new TextureRegion(playScreen.getDefaultAtlas().findRegion("smoke" + i), 0, 0, 100, 100));
        }
        animation = new Animation(0.0333f, textureRegions);
        animationTimer = 0.0001f;

        setBounds(0, 0, 100 / FallingMan.PPM, 100 / FallingMan.PPM);
        setRegion(new TextureRegion(playScreen.getDefaultAtlas().findRegion("smoke1"), 0, 0, 100, 100));
        setPosition(posX - 50 / FallingMan.PPM, posY - 50 / FallingMan.PPM);
        setOrigin(getWidth() / 2, getHeight() / 2);
        Random random = new Random();
        setRotation(random.nextInt(360) - 180);
        setScale(random.nextFloat() / 4 + 0.875f);
        setColor(getColor().r, getColor().g, getColor().b, 0.7f);
        //setScale(0.7f);
        toRemove = false;
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
        if (animation.isAnimationFinished(animationTimer)) {
            toRemove = true;
        }
    }

    private TextureRegion getFrame(float dt) {
        animationTimer += dt;
        return (TextureRegion) animation.getKeyFrame(animationTimer, false);
    }

    public boolean isToRemove() {
        return toRemove;
    }
}
