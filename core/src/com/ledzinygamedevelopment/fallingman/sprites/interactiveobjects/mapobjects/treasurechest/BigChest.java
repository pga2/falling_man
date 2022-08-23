package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.MenuScreen;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;

import java.util.Random;

public class BigChest extends Sprite {
    private BitmapFont font;
    private boolean secondStage;
    private TextureRegion bigChestTexture;
    private float posX;
    private float posY;
    private GameScreen gameScreen;
    private float growingTime;
    private boolean firstStage;
    private boolean rightDirection;
    private boolean topDirection;
    private boolean sizeDown;
    private boolean touched;
    private float touchedTimer;
    private float thirdStageTimer;
    private boolean drawFont;
    private boolean fontScaleUp;
    private float fontScale;
    private boolean shouldChangePosAccordToPlayer;
    private float currentPlayerPos;


    public BigChest(GameScreen gameScreen, float posX, float posY) {
        this.gameScreen = gameScreen;

        bigChestTexture = new TextureRegion(gameScreen.getDefaultAtlas().findRegion("bigChest"), 0, 0, 960, 960);
        setBounds(0, 0, 960 / FallingMan.PPM, 960 / FallingMan.PPM);
        setRegion(bigChestTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setPosition(posX - getWidth() / 2, posY - getHeight() / 2);
        setScale(0.20f);

        font = gameScreen.getAssetManager().getManager().get(gameScreen.getAssetManager().getFont());
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(238 / 256f, 188 / 256f, 29 / 256f, 1);
        font.getData().setScale(0.015f);

        growingTime = 0;
        firstStage = true;
        secondStage = true;
        rightDirection = true;
        topDirection = true;
        sizeDown = true;
        touched = false;
        drawFont = false;
        fontScaleUp = true;
        touchedTimer = 0;
        fontScale = 0.015f;
        shouldChangePosAccordToPlayer = false;
    }

    public void update(float dt) {
        setPosition(getX(), getY() - currentPlayerPos + gameScreen.getPlayer().b2body.getPosition().y);
        posY = posY - currentPlayerPos + gameScreen.getPlayer().b2body.getPosition().y;
        currentPlayerPos = gameScreen.getPlayer().b2body.getPosition().y;
        if (growingTime + dt < 2.4f) {
            setScale(0.2f + growingTime / 3);
        } else if (firstStage) {
            firstStage = false;
            posX = getX();
            posY = getY();
            setScale(1);
        } else if (secondStage){
            drawFont = true;
            if (Gdx.input.isTouched() && touchedTimer < 2) {
                touched = true;
                Random random = new Random();
                if (rightDirection) {
                    if (getX() - posX < 1) {
                        setPosition(getX() + 0.2f, getY());
                    } else {
                        rightDirection = false;
                    }
                } else {
                    if (posX - getX() < 1) {
                        setPosition(getX() - 0.2f, getY());
                    } else {
                        rightDirection = true;
                    }
                }
                if (topDirection) {
                    if (getY() - posY < 1) {
                        setPosition(getX(), getY() + (random.nextFloat() / 4 + 0.2f));
                    } else {
                        topDirection = false;
                    }
                } else {
                    if (posY - getY() < 1) {
                        setPosition(getX(), getY() - (random.nextFloat() / 4 + 0.2f));
                    } else {
                        topDirection = true;
                    }
                }
                if (sizeDown) {
                    if (getScaleY() > 0.8f) {
                        setScale(getScaleX(), getScaleY() - 0.03f);
                    } else {
                        sizeDown = false;
                    }
                } else {
                    if (getScaleY() < 1.1f) {
                        setScale(getScaleX(), getScaleY() + 0.03f);
                    } else {
                        sizeDown = true;
                    }
                }
                touchedTimer += dt;
            } else if (Gdx.input.isTouched() && touched) {
                if (shouldChangePosAccordToPlayer) {
                    if (gameScreen instanceof MenuScreen) {
                        for (int i = 0; i < 100; i++) {
                            MenuScreen menuScreen = ((MenuScreen) gameScreen);

                            OnePartRoll tempRoll = new OnePartRoll(menuScreen,  getX() + getWidth() / 2, getY() + getHeight() / 2, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                            tempRoll.setAmount(160);
                            tempRoll.startFlying();
                            menuScreen.getFlyingRolls().add(tempRoll);
                        }
                    }
                } else {
                    gameScreen.addOnePartRolls(100, 1);
                }
                touched = false;
                secondStage = false;
                drawFont = false;
            }
        } else if (thirdStageTimer < 0.5f){
            setScale(1 - thirdStageTimer * 2);
            thirdStageTimer += dt;
        } else {
            if (shouldChangePosAccordToPlayer) {

            } else {
                gameScreen.getSpinButton().setLocked(false);
            }
            gameScreen.removeChest(this);
        }


            growingTime += dt;
    }

    @Override
    public void draw(Batch batch, float worldHeight) {
        super.draw(batch);

        font = gameScreen.getAssetManager().getManager().get(gameScreen.getAssetManager().getFont());
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(238 / 256f, 188 / 256f, 29 / 256f, 1);
        font.getData().setScale(0.015f);
        if (drawFont) {
            if (fontScaleUp) {
                if (fontScale < 0.018f) {
                    fontScale += 0.0005f;
                    font.getData().setScale(fontScale);
                } else {
                    font.getData().setScale(fontScale);
                    fontScaleUp = false;
                }
            } else {
                if (fontScale > 0.013f) {
                    fontScale -= 0.0005f;
                    font.getData().setScale(fontScale);
                } else {
                    font.getData().setScale(fontScale);
                    fontScaleUp = true;
                }
            }
            GlyphLayout glyphLayout = new GlyphLayout(font, "TOUCH!");
            font.draw(batch, "TOUCH!", 720 / FallingMan.PPM - glyphLayout.width / 2, worldHeight / 2 - 900 / FallingMan.PPM + glyphLayout.height);
        }
    }

    public void setShouldChangePosAccordToPlayer(boolean shouldChangePosAccordToPlayer) {
        this.shouldChangePosAccordToPlayer = shouldChangePosAccordToPlayer;
        currentPlayerPos = gameScreen.getPlayer().b2body.getPosition().y;
    }
}
