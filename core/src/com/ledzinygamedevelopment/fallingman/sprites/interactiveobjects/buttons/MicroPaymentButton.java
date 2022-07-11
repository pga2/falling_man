package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class MicroPaymentButton extends Button{
    public MicroPaymentButton(GameScreen gameScreen, World world, float posX, float posY, float width, float height) {
        super(gameScreen, world, posX, posY, width, height);

        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("buy_coins_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);
    }

    public void update(float dt) {

    }

    @Override
    public void touched() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("buy_coins_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        if (clicked) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("buy_coins_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            Information skuInfo = FallingMan.purchaseManager.getInformation(FallingMan.GOLD_100);
            if (skuInfo == null || skuInfo.equals(Information.UNAVAILABLE)) {
                // the item is not available...
                Gdx.app.log("this item is not anaveible", "true");
            } else {
                // enable a purchase button and set its price label
                //purchaseButton.setText(skuInfo.getLocalPricing());
                FallingMan.purchaseManager.purchase(FallingMan.GOLD_100);
            }
            clicked = false;
        }
    }
}
