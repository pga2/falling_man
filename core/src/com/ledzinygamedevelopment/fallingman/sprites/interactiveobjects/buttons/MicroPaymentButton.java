package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;

public class MicroPaymentButton extends Button{

    private final String typeOfPurchase;
    private String tempText;
    private Information skuInfo;
    private BitmapFont font;
    private OnePartRoll onePartRoll;
    private float basicScale;
    private boolean scaleUp;
    private float currentScale;
    private Information skuBasicInfo;

    public MicroPaymentButton(GameScreen gameScreen, World world, float posX, float posY, float width, float height, String typeOfButton) {
        super(gameScreen, world, posX, posY, width, height);
        this.typeOfPurchase = typeOfButton;
        setBounds(0, 0, width, height);
        setRegion(gameScreen.getDefaultAtlas().findRegion("buy_coins_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        setPosition(posX, posY);

        tempText = "";
        font = gameScreen.getAssetManager().getManager().get(gameScreen.getAssetManager().getFont());
        if (FallingMan.purchaseManager != null) {
            skuInfo = FallingMan.purchaseManager.getInformation(typeOfButton);
            skuBasicInfo = FallingMan.purchaseManager.getInformation(FallingMan.gold_10000);
        }
        if (FallingMan.purchaseManager == null) {
            tempText = "unavailable";
        } else {
            tempText = skuInfo.getLocalName() == null || skuInfo.getLocalName().equals("") ? skuInfo.getLocalPricing() == null || skuInfo.getLocalPricing().equals("") ? "unavailable" : skuInfo.getLocalPricing() : skuInfo.getLocalPricing();
        }
        if (typeOfButton.startsWith("gold")) {
            onePartRoll = new OnePartRoll(gameScreen, getX() + getWidth() / 2 - 192 / 2f / FallingMan.PPM, getY() + 190 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 2);
        } else if (typeOfButton.startsWith("spin")){
            onePartRoll = new OnePartRoll(gameScreen, getX() + getWidth() / 2 - 192 / 2f / FallingMan.PPM, getY() + 190 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 0);
        }

        font.getData().setScale(0.003f);
        if (!tempText.equals("unavailable")) {
            GlyphLayout glyphLayout = new GlyphLayout(font, skuInfo.getLocalDescription().replace(" gold", "\ngold"));
            font.getData().setScale(glyphLayout.width > getWidth() ? (getWidth() - 20 / FallingMan.PPM) / glyphLayout.width * 0.003f : 0.003f, font.getData().scaleY);
            basicScale = font.getData().scaleX;
            currentScale = font.getData().scaleX;
            scaleUp = true;
        }
    }

    public void update(float dt) {

    }

    @Override
    public void touched() {
        super.touched();
        setRegion(gameScreen.getDefaultAtlas().findRegion("buy_coins_button_clicked"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
        clicked = true;
    }

    @Override
    public void notTouched() {
        super.notTouched();
        if (clicked) {
            setRegion(gameScreen.getDefaultAtlas().findRegion("buy_coins_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
            if (skuInfo == null || skuInfo.equals(Information.UNAVAILABLE)) {
                Gdx.app.log("this item is not anaveible", "true");
            } else {
                FallingMan.purchaseManager.purchase(typeOfPurchase);
            }
            clicked = false;
        }
    }

    @Override
    public void restoreNotClickedTexture() {
        setRegion(gameScreen.getDefaultAtlas().findRegion("buy_coins_button"), 0, 0, (int) (width * FallingMan.PPM), (int) (height * FallingMan.PPM));
    }

    @Override
    public void draw(Batch batch, float dt) {
        super.draw(batch);
        onePartRoll.draw(batch);
        if (!tempText.equals("unavailable")) {
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            font.setUseIntegerPositions(false);
            font.setColor(100 / 256f, 80 / 256f, 0 / 256f, 1);

            font.getData().setScale(0.0025f);
            GlyphLayout glyphLayout = new GlyphLayout(font, skuInfo.getPriceAsDouble() + skuInfo.getPriceCurrencyCode());
            font.getData().setScale(glyphLayout.width > getWidth() ? (getWidth() - 100 / FallingMan.PPM) / glyphLayout.width * 0.0025f : 0.0025f, font.getData().scaleY);
            font.draw(batch, skuInfo.getPriceAsDouble() + skuInfo.getPriceCurrencyCode(), getX(), getY() + glyphLayout.height + 40 / FallingMan.PPM, getWidth(), Align.center, false);

            if (skuInfo.getPriceAsDouble() != null) {
                int extraCoins = 0;
                if (typeOfPurchase.startsWith("gold") && skuBasicInfo.getPriceAsDouble() != null) {
                    extraCoins = (int) Math.round((Integer.parseInt(typeOfPurchase.replaceAll("\\D+", "")) / (10000f * (skuInfo.getPriceAsDouble() / skuBasicInfo.getPriceAsDouble()))) * 100) - 100;
                } else if (typeOfPurchase.startsWith("spin") && skuBasicInfo.getPriceAsDouble() != null) {
                    extraCoins = (int) Math.round((Integer.parseInt(typeOfPurchase.replaceAll("\\D+", "")) / (250f * (skuInfo.getPriceAsDouble() / skuBasicInfo.getPriceAsDouble()))) * 100) - 100;
                }
                if (extraCoins != 0) {
                    font.getData().setScale(0.0024f);
                    glyphLayout = new GlyphLayout(font, extraCoins + "% more");
                    font.draw(batch, extraCoins + "% more", getX(), getY() + getHeight() - 355 / FallingMan.PPM, getWidth(), Align.center, false);
                }
            }


            font.getData().setScale(currentScale);
            if (scaleUp) {
                if (font.getData().scaleX > basicScale * 1.05f) {
                    scaleUp = false;
                } else {
                    currentScale = currentScale + (basicScale * 0.004f) * 60 * dt;
                    font.getData().setScale(currentScale);
                }
            } else {
                if (font.getData().scaleX < basicScale * 0.95f) {
                    scaleUp = true;
                } else {
                    currentScale = currentScale - (basicScale * 0.005f) * 60 * dt;
                    font.getData().setScale(currentScale);
                }
            }
            glyphLayout = new GlyphLayout(font, skuInfo.getLocalDescription().replace(" gold", "\ngold").replace(" spins", "\nspins"));

            font.draw(batch, skuInfo.getLocalDescription().replace(" gold", "\ngold").replace(" spins", "\nspins"), getX(), getY() + getHeight() - 80 / FallingMan.PPM + glyphLayout.height / 2, getWidth(), Align.center, false);
        } else {
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            font.setUseIntegerPositions(false);
            font.setColor(10 / 256f, 8 / 256f, 0 / 256f, 1);

            font.getData().setScale(0.003f);
            GlyphLayout glyphLayout = new GlyphLayout(font, tempText);
            font.draw(batch, tempText, getX(), getY() + glyphLayout.height + 400 / FallingMan.PPM, getWidth(), Align.center, false);
        }
    }
}
