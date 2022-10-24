package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.screens.MenuScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coins.Spark;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.tools.RandomBodyPart;

import java.util.HashMap;
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
    private final int rewardTypeBodyPart = 2;
    private int rewardType;
    private Sprite bodyPart;
    private boolean drawBodyPart;
    private boolean drawChest;
    private boolean bodyPartScaleUp;
    private Array<Spark> sparks;
    private boolean touchedWhenRemoveWonBodyPartAllowed;
    private boolean wonBodyParteAfterStageThree;
    private boolean bigChestWasTouchedBodyParteAfterStageThree;
    private enum CURRENT_STATE {FIRST_STAGE_GROWING, FIRST_STAGE_END, SECOND_STAGE, THIRD_STAGE_GOLD, FOURTH_STAGE_GOLD, THIRD_STAGE_BODY_PART, FOURTH_STAGE_BODY_PART, FIFTH_STAGE_BODY_PART, SIXTH_STAGE_BODY_PART};
    private CURRENT_STATE currentState;
    private float removeBodyPartTimer;
    private float scaleWhenGettingIntoStageSixthBodyPart;


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
        drawBodyPart = false;
        drawChest = true;
        bodyPartScaleUp = true;
        sparks = new Array<>();
        touchedWhenRemoveWonBodyPartAllowed = false;
        wonBodyParteAfterStageThree = false;
        removeBodyPartTimer = 0;
        currentState = CURRENT_STATE.FIRST_STAGE_GROWING;
    }

    public void update(float dt) {
        if (gameScreen.getPlayer() != null) {
            setPosition(getX(), getY() - currentPlayerPos + gameScreen.getPlayer().b2body.getPosition().y);
            posY = posY - currentPlayerPos + gameScreen.getPlayer().b2body.getPosition().y;
            currentPlayerPos = gameScreen.getPlayer().b2body.getPosition().y;
        }
        switch (currentState) {
            case FIRST_STAGE_GROWING:
                if (growingTime + dt < 2.4f) {
                    setScale(0.2f + growingTime / 3);
                } else {
                    currentState = CURRENT_STATE.FIRST_STAGE_END;
                }
                break;
            case FIRST_STAGE_END:
                posX = getX();
                posY = getY();
                setScale(1);
                currentState = CURRENT_STATE.SECOND_STAGE;
                break;
            case SECOND_STAGE:
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
                    rewardType = new Random().nextInt(3);
                    rewardType = 2;
                    if (rewardType == rewardTypeBodyPart && !RandomBodyPart.getRandomBodyPart(gameScreen.getSaveData()).equals("allOwned")) {
                        String bodyPartNumberAndName = RandomBodyPart.getRandomBodyPart(gameScreen.getSaveData());
                        String bodyPartNumber = RandomBodyPart.extractNumber(bodyPartNumberAndName);
                        String bodyPartName = bodyPartNumberAndName.replace(bodyPartNumber, "");
                        HashMap<String, Boolean> bodySpritesOwned = gameScreen.getSaveData().getBodySpritesOwned();
                        bodySpritesOwned.put("owned" + bodyPartNumber + bodyPartName, true);
                        gameScreen.getSaveData().saveBodySpritesOwned(bodySpritesOwned);
                        bodyPart = new Sprite();
                        int texturePos = getSpritePosFromName(bodyPartName);
                        Gdx.app.log("body part number: ", bodyPartNumber);
                        Gdx.app.log("body part name: ", bodyPartName);
                        bodyPart.setRegion(new TextureRegion(gameScreen.getPlayerAtlas().findRegion("player" + bodyPartNumber), 160 * texturePos, 0, 160, 160));
                        bodyPart.setBounds(0, 0, 160 / FallingMan.PPM, 160 / FallingMan.PPM);
                        bodyPart.setOrigin(bodyPart.getWidth() / 2, bodyPart.getHeight() / 2);
                        drawBodyPart = true;
                        currentState = CURRENT_STATE.THIRD_STAGE_BODY_PART;
                    } else {
                        if (shouldChangePosAccordToPlayer) {
                            if (gameScreen instanceof MenuScreen) {
                                for (int i = 0; i < 100; i++) {
                                    MenuScreen menuScreen = ((MenuScreen) gameScreen);

                                    OnePartRoll tempRoll = new OnePartRoll(menuScreen, getX() + getWidth() / 2, getY() + getHeight() / 2, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                                    tempRoll.setAmount(160);
                                    tempRoll.startFlying();
                                    menuScreen.getFlyingRolls().add(tempRoll);
                                }
                            }
                        } else {
                            gameScreen.addOnePartRolls(100, 1);
                        }
                        currentState = CURRENT_STATE.THIRD_STAGE_GOLD;
                    }
                    touched = false;
                    secondStage = false;
                    drawFont = false;
                }
                break;
            case THIRD_STAGE_GOLD:
                if (thirdStageTimer < 0.5f) {
                    setScale(1 - thirdStageTimer * 2);
                    thirdStageTimer += dt;
                } else {
                    currentState = CURRENT_STATE.FOURTH_STAGE_GOLD;
                }
                break;
            case THIRD_STAGE_BODY_PART:
                if (!wonBodyParteAfterStageThree) {
                    if (thirdStageTimer < 1) {
                        setScale(1 - thirdStageTimer);
                        thirdStageTimer += dt;
                        bodyPart.setPosition(getX() + getWidth() / 2 - bodyPart.getWidth() / 2, getY() + getHeight() / 2 - bodyPart.getHeight() / 2);
                        if (bodyPartScaleUp) {
                            if (bodyPart.getScaleX() < 4) {
                                bodyPart.setScale(bodyPart.getScaleX() + 0.1f);
                            } else {
                                bodyPartScaleUp = false;
                            }
                        } else {
                            if (bodyPart.getScaleX() > 3) {
                                bodyPart.setScale(bodyPart.getScaleX() - 0.1f);
                            } else {
                                bodyPartScaleUp = true;
                            }
                        }
                        sparks.add(new Spark(gameScreen, bodyPart.getX() + bodyPart.getWidth() / 2, bodyPart.getY() + bodyPart.getHeight() / 2, (byte) 1));
                        sparks.add(new Spark(gameScreen, bodyPart.getX() + bodyPart.getWidth() / 2, bodyPart.getY() + bodyPart.getHeight() / 2, (byte) 1));
                        sparks.add(new Spark(gameScreen, bodyPart.getX() + bodyPart.getWidth() / 2, bodyPart.getY() + bodyPart.getHeight() / 2, (byte) 1));
                    } else {
                        drawChest = false;
                        wonBodyParteAfterStageThree = true;
                    }
                } else {
                    currentState = CURRENT_STATE.FOURTH_STAGE_BODY_PART;
                }
                break;
            case FOURTH_STAGE_BODY_PART:
                if (!touchedWhenRemoveWonBodyPartAllowed && wonBodyParteAfterStageThree) {
                    thirdStageTimer += dt;
                    bodyPart.setPosition(getX() + getWidth() / 2 - bodyPart.getWidth() / 2, getY() + getHeight() / 2 - bodyPart.getHeight() / 2);
                    if (bodyPartScaleUp) {
                        if (bodyPart.getScaleX() < 4) {
                            bodyPart.setScale(bodyPart.getScaleX() + 0.1f);
                        } else {
                            bodyPartScaleUp = false;
                        }
                    } else {
                        if (bodyPart.getScaleX() > 3) {
                            bodyPart.setScale(bodyPart.getScaleX() - 0.1f);
                        } else {
                            bodyPartScaleUp = true;
                        }
                    }
                    sparks.add(new Spark(gameScreen, bodyPart.getX() + bodyPart.getWidth() / 2, bodyPart.getY() + bodyPart.getHeight() / 2, (byte) 1));
                    sparks.add(new Spark(gameScreen, bodyPart.getX() + bodyPart.getWidth() / 2, bodyPart.getY() + bodyPart.getHeight() / 2, (byte) 1));
                    sparks.add(new Spark(gameScreen, bodyPart.getX() + bodyPart.getWidth() / 2, bodyPart.getY() + bodyPart.getHeight() / 2, (byte) 1));
                } else {
                    currentState = CURRENT_STATE.FIFTH_STAGE_BODY_PART;
                    scaleWhenGettingIntoStageSixthBodyPart = bodyPart.getScaleX();
                }
                break;
            case FIFTH_STAGE_BODY_PART:
                if (removeBodyPartTimer < 0.5) {
                    bodyPart.setScale(scaleWhenGettingIntoStageSixthBodyPart - removeBodyPartTimer * 2 * scaleWhenGettingIntoStageSixthBodyPart);
                    removeBodyPartTimer += dt;
                } else {
                    currentState = CURRENT_STATE.SIXTH_STAGE_BODY_PART;
                }
                break;
            case FOURTH_STAGE_GOLD:
            case SIXTH_STAGE_BODY_PART:
                if (shouldChangePosAccordToPlayer) {

                } else {
                    gameScreen.getSpinButton().setLocked(false);
                }
                gameScreen.removeChest(this);
                break;
        }

        Array<Spark> sparksToRemove = new Array<>();
        for (int i = 0; i < sparks.size; i++) {
            Spark spark = sparks.get(i);
            spark.update(dt);
            if (spark.isRemoveSpark()) {
                sparksToRemove.add(spark);
            }
        }
        sparks.removeAll(sparksToRemove, false);


            growingTime += dt;
    }

    @Override
    public void draw(Batch batch, float worldHeight) {
        if (drawChest) {
            super.draw(batch);
        }

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
            font.draw(batch, "TOUCH!", 720 / FallingMan.PPM - glyphLayout.width / 2, getY() - 650 / FallingMan.PPM + glyphLayout.height);
        }
        if (drawBodyPart) {
            for (Spark spark : sparks) {
                spark.draw(batch);
            }
            bodyPart.draw(batch);
        }
    }

    public void setShouldChangePosAccordToPlayer(boolean shouldChangePosAccordToPlayer) {
        this.shouldChangePosAccordToPlayer = shouldChangePosAccordToPlayer;
        currentPlayerPos = gameScreen.getPlayer().b2body.getPosition().y;
    }

    public boolean isTouchedWhenRemoveWonBodyPartAllowed() {
        return touchedWhenRemoveWonBodyPartAllowed;
    }

    public void setTouchedWhenRemoveWonBodyPartAllowed(boolean touchedWhenRemoveWonBodyPartAllowed) {
        if (wonBodyParteAfterStageThree) {
            this.touchedWhenRemoveWonBodyPartAllowed = touchedWhenRemoveWonBodyPartAllowed;
        }
    }

    public boolean isBigChestWasTouchedBodyParteAfterStageThree() {
        return bigChestWasTouchedBodyParteAfterStageThree;
    }

    public void setBigChestWasTouchedBodyParteAfterStageThree(boolean bigChestWasTouchedBodyParteAfterStageThree) {
        this.bigChestWasTouchedBodyParteAfterStageThree = bigChestWasTouchedBodyParteAfterStageThree;
    }

    public static int getSpritePosFromName(String name) {
        int spritePos;
        if (name.equals("head")) {
            spritePos = 0;
        } else if (name.equals("belly")) {
            spritePos = 1;

        } else if (name.equals("armL")) {
            spritePos = 2;

        } else if (name.equals("foreArmL")) {
            spritePos = 3;

        } else if (name.equals("handL")) {
            spritePos = 4;

        } else if (name.equals("armR")) {
            spritePos =2 ;

        } else if (name.equals("foreArmR")) {
            spritePos = 3;

        } else if (name.equals("handR")) {
            spritePos = 4;

        } else if (name.equals("thighL")) {
            spritePos = 5;

        } else if (name.equals("shinL")) {
            spritePos = 6;

        } else if (name.equals("footL")) {
            spritePos = 7;

        } else if (name.equals("thighR")) {
            spritePos = 5;

        } else if (name.equals("shinR")) {
            spritePos = 6;

        } else if (name.equals("footR")) {
            spritePos = 7;

        } else {
            spritePos = 0;

        }
        return spritePos;
    }
}
