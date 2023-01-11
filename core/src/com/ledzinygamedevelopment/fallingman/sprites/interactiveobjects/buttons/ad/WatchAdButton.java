package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.ad;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;

public class WatchAdButton extends Button {

    //AdIcon leftAdIcon;
    //AdIcon rightAdIcon;

    public WatchAdButton(GameScreen gameScreen, World world, float posX, float posY) {
        super(gameScreen, world, posX, posY);

        width = 1024 / FallingMan.PPM;
        height = 256 / FallingMan.PPM;

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("watch_ad_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX - getWidth() / 2, posY - getHeight() / 2);
        this.posX = getX();
        this.posY = getY();
        //leftAdIcon = new AdIcon(gameScreen, getX() + 128 / FallingMan.PPM, getY() + getHeight() / 2);
        //rightAdIcon = new AdIcon(gameScreen, getX() + getWidth() - 128 / FallingMan.PPM, getY() + getHeight() / 2);
    }

    @Override
    public void touched() {
        super.touched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("watch_ad_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        super.notTouched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("watch_ad_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        if (clicked) {
            gameScreen.watchAdButtonClicked(this);
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("watch_ad_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        /*if (leftAdIcon.isScaleUp()) {
            if (leftAdIcon.getScaleX() < 1.12f) {
                leftAdIcon.setScale(leftAdIcon.getScaleX() + 0.03f * 60 * dt);
            } else {
                leftAdIcon.setScaleUp(false);
            }
        } else {
            if (leftAdIcon.getScaleX() > 0.85f) {
                leftAdIcon.setScale(leftAdIcon.getScaleX() - 0.03f * 60 * dt);
            } else {
                leftAdIcon.setScaleUp(true);
            }
        }
        rightAdIcon.setScale(leftAdIcon.getScaleX());
        leftAdIcon.draw(batch);
        rightAdIcon.draw(batch);*/
    }


}
