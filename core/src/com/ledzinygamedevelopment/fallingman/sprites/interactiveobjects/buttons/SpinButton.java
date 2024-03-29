package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class SpinButton extends Button{

    private long soundId;

    public SpinButton(GameScreen gameScreen, World world, float posX, float posY, float width, float height) {
        super(gameScreen, world, posX, posY, width, height);

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("spin"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    @Override
    public void touched() {
        super.touched();
        if (!locked) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("spin_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            clicked = true;
        }
    }

    public void notTouched() {
        super.notTouched();
        if (!locked) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("spin"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));

            if (gameScreen.getSaveData().getSounds()) {
                soundId = gameScreen.getAssetManager().getSpinSound().play();
                gameScreen.getAssetManager().getSpinSound().setPitch(soundId, 1.8f);
            }
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("spin"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));

    }

    public long getSoundId() {
        return soundId;
    }

    //, 448, 7936, 544, 192
}
