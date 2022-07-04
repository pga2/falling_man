package com.ledzinygamedevelopment.fallingman.sprites.enemies.dragon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class DragonFire extends Sprite {

    public DragonFire(PlayScreen playScreen, float posX, float posY) {

        setBounds(0, 0, 512 / FallingMan.PPM, 192 / FallingMan.PPM);
        setRegion((Texture) playScreen.getAssetManager().getManager().get(playScreen.getAssetManager().getPlayScreenDragonFire()));
        setPosition(posX - getWidth() / 2, posY);
        //setOrigin(getWidth() / 2, getHeight() / 2);
    }
}
