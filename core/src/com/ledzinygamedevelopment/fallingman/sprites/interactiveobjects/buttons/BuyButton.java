package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.tools.Prices;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

public class BuyButton extends Button{
    private BitmapFont font;
    private int spriteNumber;
    private String bodyPartName;
    private SaveData saveData;
    private Prices prices;
    private boolean notEnoughGold;
    private long price;
    private float notEnoughGoldTimer;
    private boolean drawAlreadySetSprite;
    private float drawAlreadySetSpriteTimer;

    public enum TYPE_OF_BUTTON {BUY, SET};
    private TYPE_OF_BUTTON type_of_button;
    private boolean regionChange;
    private boolean boughtOrSetNew;

    public BuyButton(GameScreen gameScreen, World world, float posX, float posY, int spriteNumber, String bodyPartName, SaveData saveData, Prices prices) {
        super(gameScreen, world, posX, posY);
        this.spriteNumber = spriteNumber;
        this.bodyPartName = bodyPartName;
        this.saveData = saveData;
        this.prices = prices;
        font = gameScreen.getAssetManager().getManager().get(gameScreen.getAssetManager().getFont());
        setBounds(0, 0, 544 / FallingMan.PPM, 192 / FallingMan.PPM);
        setRegion(gameScreen.getDefaultAtlas().findRegion("buy_button"), 0, 0, 544, 192);
        setPosition(posX, posY);
        width = 544 / FallingMan.PPM;
        height = 192 / FallingMan.PPM;
        Gdx.app.log("spriteNumber", String.valueOf(spriteNumber));
        Gdx.app.log("spriteName ", String.valueOf(bodyPartName));
        price = prices.getPricesMap().get("pricePlayer" + spriteNumber + bodyPartName);
        notEnoughGold = false;
        type_of_button = TYPE_OF_BUTTON.BUY;
        regionChange = true;
        for (String key : gameScreen.getOwnedBodySprites().keySet()) {
            if ((spriteNumber + bodyPartName).equals(key.substring(5)) && gameScreen.getOwnedBodySprites().get(key)) {
                type_of_button = TYPE_OF_BUTTON.SET;
            }
        }
        drawAlreadySetSprite = false;
        boughtOrSetNew = false;
    }

    @Override
    public void touched() {
        super.touched();
        if (!clicked) {
            switch (type_of_button) {
                case SET:
                    setRegion(gameScreen.getDefaultAtlas().findRegion("set_sprite_button_clicked"), 0, 0, 544, 192);
                    break;
                case BUY:
                    setRegion(gameScreen.getDefaultAtlas().findRegion("buy_button_clicked"), 0, 0, 544, 192);
                    break;

            }

            clicked = true;
        }
    }

    public void update(float dt) {
        super.update(dt, new Vector2(getX(), getY()));
        if (notEnoughGold) {
            notEnoughGoldTimer += dt;
            if (notEnoughGoldTimer >= 1f) {
                notEnoughGold = false;
            }
        }
        if (drawAlreadySetSprite) {
            drawAlreadySetSpriteTimer += dt;
            if (drawAlreadySetSpriteTimer >= 1f) {
                drawAlreadySetSprite = false;
            }
        }
        if (regionChange) {
            switch (type_of_button) {
                case SET:
                    setRegion(gameScreen.getDefaultAtlas().findRegion("select_button"), 0, 0, 544, 192);
                    break;
                case BUY:
                    setRegion(gameScreen.getDefaultAtlas().findRegion("buy_button"), 0, 0, 544, 192);
                    break;
            }
            regionChange = false;
        }
    }

    @Override
    public void notTouched() {
        super.notTouched();
        if (clicked) {
            switch (type_of_button) {
                case SET:
                    for (PlayerBodyPart playerBodyPart : gameScreen.getPlayer().getBodyParts()) {
                        if (playerBodyPart.getBodyPartName().equals(bodyPartName)) {
                            if (playerBodyPart.getSpriteNumber() == spriteNumber && !boughtOrSetNew) {
                                drawAlreadySetSprite = true;
                                drawAlreadySetSpriteTimer = 0;
                                setRegion(gameScreen.getDefaultAtlas().findRegion("select_button"), 0, 0, 544, 192);
                            } else {
                                boughtOrSetNew = true;
                                setRegion(gameScreen.getDefaultAtlas().findRegion("select_button"), 0, 0, 544, 192);
                                saveData.saveCurrentBodyPartSprite(bodyPartName, spriteNumber);
                                playerBodyPart.setTexture(spriteNumber);
                            }
                        }
                    }
                    if ("head".equals(bodyPartName)) {
                        if (gameScreen.getPlayer().getHeadSpriteNumber() == spriteNumber && !boughtOrSetNew) {
                            drawAlreadySetSprite = true;
                            drawAlreadySetSpriteTimer = 0;
                            setRegion(gameScreen.getDefaultAtlas().findRegion("select_button"), 0, 0, 544, 192);
                        } else {
                            boughtOrSetNew = true;
                            setRegion(gameScreen.getDefaultAtlas().findRegion("select_button"), 0, 0, 544, 192);
                            gameScreen.getPlayer().setTexture(spriteNumber);
                            saveData.saveCurrentBodyPartSprite(bodyPartName, spriteNumber);
                        }
                    }
                    if (saveData.getSounds()) {
                        gameScreen.getAssetManager().getSelectBodyPartSound().play();
                    }
                    break;
                case BUY:
                    setRegion(gameScreen.getDefaultAtlas().findRegion("buy_button"), 0, 0, 544, 192);
                    if (saveData.getGold() < price) {
                        Gdx.app.log("not enough gold", "price " + price);
                        notEnoughGold = true;
                        notEnoughGoldTimer = 0;
                    } else {
                        Gdx.app.log("bought", "pricePlayer" + spriteNumber + bodyPartName);
                        saveData.removeGold(price);
                        toRemove = true;
                        gameScreen.getGoldAndHighScoresIcons().setGold(saveData.getGold());
                        gameScreen.getOwnedBodySprites().put("owned" + spriteNumber + bodyPartName, true);
                        saveData.saveBodySpritesOwned(gameScreen.getOwnedBodySprites());
                        for (PlayerBodyPart playerBodyPart : gameScreen.getPlayer().getBodyParts()) {
                            if (playerBodyPart.getBodyPartName().equals(bodyPartName)) {
                                playerBodyPart.setTexture(spriteNumber);
                            }
                        }
                        if ("head".equals(bodyPartName)) {
                            gameScreen.getPlayer().setTexture(spriteNumber);
                        }
                        saveData.saveCurrentBodyPartSprite(bodyPartName, spriteNumber);
                        if (saveData.getSounds()) {
                            gameScreen.getAssetManager().getBuyBodyPartSound().play();
                        }
                    }
                    break;

            }
            clicked = false;
        }
        if (clicked) {
            Gdx.input.vibrate(50);
        }
        boughtOrSetNew = false;
        /*if (clicked) {
            switch (type_of_button) {
                case SET:
                    setRegion(gameScreen.getDefaultAtlas().findRegion("set_sprite_button"), 0, 0, 544, 192);
                    break;
                case BUY:
                    setRegion(gameScreen.getDefaultAtlas().findRegion("buy_button"), 0, 0, 544, 192);
                    break;
            }
        }*/
    }

    @Override
    public void restoreNotClickedTexture() {
        switch (type_of_button) {
            case BUY:
                setRegion(gameScreen.getDefaultAtlas().findRegion("buy_button"), 0, 0, 544, 192);
                break;
            case SET:
                setRegion(gameScreen.getDefaultAtlas().findRegion("select_button"), 0, 0, 544, 192);
                break;
        }

    }

    public long getPrice() {
        return price;
    }

    public int getSpriteNumber() {
        return spriteNumber;
    }

    public void setSpriteNumber(int spriteNumber) {
        this.spriteNumber = spriteNumber;
        type_of_button = TYPE_OF_BUTTON.BUY;
        regionChange = true;
        for (String key : gameScreen.getOwnedBodySprites().keySet()) {
            if ((spriteNumber + bodyPartName).equals(key.substring(5)) && gameScreen.getOwnedBodySprites().get(key)) {
                type_of_button = TYPE_OF_BUTTON.SET;
            }
        }
        price = prices.getPricesMap().get("pricePlayer" + spriteNumber + bodyPartName);
    }

    public String getBodyPartName() {
        return bodyPartName;
    }

    public void setBodyPartName(String bodyPartName) {
        this.bodyPartName = bodyPartName;
        type_of_button = TYPE_OF_BUTTON.BUY;
        regionChange = true;
        for (String key : gameScreen.getOwnedBodySprites().keySet()) {
            if ((spriteNumber + bodyPartName).equals(key.substring(5)) && gameScreen.getOwnedBodySprites().get(key)) {
                type_of_button = TYPE_OF_BUTTON.SET;
            }
        }
        price = prices.getPricesMap().get("pricePlayer" + spriteNumber + bodyPartName);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if (notEnoughGold) {
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            font.setUseIntegerPositions(false);
            font.getData().setScale(0.0067f);
            if (notEnoughGoldTimer < 0.5f) {
                font.setColor(238/256f, 188/256f, 29/256f, 1);
                GlyphLayout glyphLayout = new GlyphLayout(font, "Not enough gold");
                font.draw(batch, "Not enough gold", FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - glyphLayout.width / 2, FallingMan.MIN_WORLD_HEIGHT / 2f / FallingMan.PPM + 840 / FallingMan.PPM);
            } else if (notEnoughGoldTimer < 1f){
                font.setColor(238/256f, 188/256f, 29/256f, 1f - (notEnoughGoldTimer - 0.5f) * 2);
                GlyphLayout glyphLayout = new GlyphLayout(font, "Not enough gold");
                font.draw(batch, "Not enough gold", FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - glyphLayout.width / 2, FallingMan.MIN_WORLD_HEIGHT / 2f / FallingMan.PPM + 840 / FallingMan.PPM);
            }
        }
        if (drawAlreadySetSprite) {
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            font.setUseIntegerPositions(false);
            font.getData().setScale(0.0067f);
            if (drawAlreadySetSpriteTimer < 0.5f) {
                font.setColor(238/256f, 188/256f, 29/256f, 1);
                GlyphLayout glyphLayout = new GlyphLayout(font, "Sprite already set");
                font.draw(batch, "Sprite already set", FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - glyphLayout.width / 2, FallingMan.MIN_WORLD_HEIGHT / 2f / FallingMan.PPM + 840 / FallingMan.PPM);
            } else if (drawAlreadySetSpriteTimer < 1f){
                font.setColor(238/256f, 188/256f, 29/256f, 1f - (drawAlreadySetSpriteTimer - 0.5f) * 2);
                GlyphLayout glyphLayout = new GlyphLayout(font, "Sprite already set");
                font.draw(batch, "Sprite already set", FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - glyphLayout.width / 2, FallingMan.MIN_WORLD_HEIGHT / 2f / FallingMan.PPM + 840 / FallingMan.PPM);
            }
        }
    }
}
