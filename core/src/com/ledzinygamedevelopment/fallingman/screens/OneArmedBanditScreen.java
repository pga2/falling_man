package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects.Cloud;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OneArmBandit;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.Roll;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.SpinsAmountLine;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.SpinsBackground;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

public class OneArmedBanditScreen implements GameScreen {


    private final GameAssetManager assetManager;
    private byte currentScreen;
    private FallingMan game;
    private TextureAtlas defaultAtlas;
    private OrthographicCamera gameCam;
    private ExtendViewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Array<Button> buttons;
    private boolean firstTouch;
    private Vector2 previousTouchPos;
    private Vector2 lastTouchPos;
    private Array<Cloud> clouds;
    private Array<Vector2> cloudsPositionForNextScreen;
    private boolean changeScreen;
    private Integer mapHeight;

    private SaveData saveData;
    private Array<OneArmBandit> oneArmBandits;
    private Array<Roll> rolls;
    private SpinButton spinButton;
    private boolean startRolling;
    private float rollingTime;
    private Array<OnePartRoll> smallRolls;
    private boolean winOneArmedBandit;
    private float winOneArmedBanditTime;
    private boolean winOneArmedBanditScaleUp;
    private boolean createOneArmedBanditBundle;
    private int numberOfSpins;
    private float loseOneArmedBanditEndTime;
    private boolean loseOneArmedBandit;
    private Array<SpinsBackground> spinsBackgrounds;
    private Array<SpinsAmountLine> spinsAmountLines;
    private Array<OnePartRoll> flyingRolls;

    private BitmapFont font;
    private Date date;
    private GregorianCalendar calendarG;
    private boolean startCountingDateAgain;
    private int startCountingMinute;
    private int startCountingHour;
    private long minutesTillNextSpins;
    private Calendar minutesTillNextSpinsCallendar;

    private GoldAndHighScoresIcons goldAndHighScoresIcons;
    private GoldAndHighScoresBackground goldAndHighScoresBackground;
    private float goldAndHighScoresTextSize;
    private Array<BigChest> bigChests;
    private boolean newScreenJustOpened;
    private boolean changeToShopScreen;


    public OneArmedBanditScreen(FallingMan game, Array<Vector2> cloudsPositionForNextScreen, float screenHeight, boolean changeToShopScreen) {

        assetManager = new GameAssetManager();
        assetManager.loadOneArmedBandit();
        assetManager.getManager().finishLoading();
        defaultAtlas = assetManager.getManager().get(assetManager.getOneArmedBanditScreenDefault());
        font = assetManager.getManager().get(assetManager.getFont());
        this.game = game;
        this.changeToShopScreen = changeToShopScreen;
        currentScreen = FallingMan.CURRENT_SCREEN;
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("one_armed_bandit_maps/one_armed_bandit_map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);

        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2) / FallingMan.PPM, (gamePort.getWorldHeight() * FallingMan.PPM / 2) / FallingMan.PPM, 0);
        world = new World(new Vector2(0, -4f), true);
        b2dr = new Box2DDebugRenderer();
        buttons = new Array<>();
        previousTouchPos = new Vector2();
        lastTouchPos = new Vector2();
        firstTouch = true;
        saveData = new SaveData();

        rolls = new Array<>();
        oneArmBandits = new Array<>();
        buttons = new Array<>();
        smallRolls = new Array<>();
        createOneArmedBanditBundle = true;
        loseOneArmedBanditEndTime = 0;
        loseOneArmedBandit = false;

        spinsBackgrounds = new Array<>();
        spinsAmountLines = new Array<>();

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(238 / 256f, 188 / 256f, 29 / 256f, 1);
        font.getData().setScale(0.0045f);

        GregorianCalendar prefsCallendar = new GregorianCalendar();
        if (saveData.getMillis() == 0) {
            prefsCallendar.setTime(new Date());
            saveData.setMillis(prefsCallendar.getTimeInMillis());
        } else {
            prefsCallendar.setTimeInMillis(saveData.getMillis());
        }
        minutesTillNextSpinsCallendar = prefsCallendar;
        startCountingDateAgain = false;
        generateWindows();
        flyingRolls = new Array<>();
        goldAndHighScoresTextSize = 1;
        bigChests = new Array<>();
        clouds = new Array<>();
        MapProperties mapProp = map.getProperties();
        mapHeight = mapProp.get("height", Integer.class);
        for (Vector2 pos : cloudsPositionForNextScreen) {
            Cloud cloud = new Cloud(this, 0, 0, true, changeToShopScreen ? FallingMan.SHOP_SCREEN : FallingMan.MENU_SCREEN);
            cloud.setPosition(pos.x, pos.y);
            clouds.add(cloud);
        }
        this.cloudsPositionForNextScreen = new Array<>();
        changeScreen = false;
        newScreenJustOpened = true;
        changeToShopScreen = false;
        //gameCam.zoom = 5;
    }


    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            if (!newScreenJustOpened) {
                if (!changeScreen) {

                    if (firstTouch) {
                        previousTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                        firstTouch = false;
                    }
                    // check if buttons click
                    Vector2 mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                    for (Button button : buttons) {
                        if (button.mouseOver(mouseVector) && !button.isLocked()) {
                            button.touched();
                            button.setClicked(true);
                        }
                    }

                    lastTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                }
                if (!startRolling && !loseOneArmedBandit && !winOneArmedBandit && flyingRolls.size == 0) {
                    //menuScreen
                    if (lastTouchPos.y - previousTouchPos.y > 200 / FallingMan.PPM) {
                        if (!changeScreen) {
                            Random random = new Random();
                            //currentScreen = FallingMan.MENU_SCREEN;
                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 26; j++) {
                                    clouds.add(new Cloud(this, ((i * 640) - random.nextInt(220)) / FallingMan.PPM, ((-150 * j) - random.nextInt(21)) / FallingMan.PPM, false, FallingMan.MENU_SCREEN));
                                }
                            }
                            changeScreen = true;
                            changeToShopScreen = false;
                        }
                    }

                    //shopScreen
                    if (lastTouchPos.y - previousTouchPos.y < -200 / FallingMan.PPM) {
                        if (!changeScreen) {
                            Random random = new Random();
                            //currentScreen = FallingMan.MENU_SCREEN;
                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 26; j++) {
                                    clouds.add(new Cloud(this, ((i * 640) - random.nextInt(220)) / FallingMan.PPM, gamePort.getWorldHeight() + ((140 * j) - random.nextInt(21)) / FallingMan.PPM, false, FallingMan.SHOP_SCREEN));
                                }
                            }
                            changeScreen = true;
                            changeToShopScreen = true;
                        }
                    }
                }
            }
        } else {
            newScreenJustOpened = false;
            if (!changeScreen) {
                for (Button button : buttons) {
                    if (button.isClicked() && !button.isLocked()) {
                        if (button.getClass().equals(SpinButton.class)) {
                            setStartRolling(true);
                            saveData.removeSpin();
                            for (Roll roll : rolls) {
                                roll.setRollingCurrently(true);
                            }
                        }
                        button.notTouched();
                        button.setClicked(false);
                    }
                }
                firstTouch = true;
            }
        }
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 8, 5);

        if (startRolling) {
            spinButton.setLocked(true);
            startRolling(dt);
        } else if (winOneArmedBandit) {
            winOneArmedBandit(dt);
        } else if (loseOneArmedBandit) {
            loseOneArmedBanditEndTime += dt;
            if (loseOneArmedBanditEndTime > 0.5f) {
                //removeOneArmedBanditbundle();
                loseOneArmedBanditEndTime = 0;
                loseOneArmedBandit = false;
            }
        } else if (spinButton != null && saveData.getNumberOfSpins() > 0) {
            spinButton.setLocked(false);
        }

        date = new Date();
        calendarG = new GregorianCalendar();
        calendarG.setTime(date);
        /*if (calendarG.get(Calendar.MINUTE) + calendarG.get(Calendar.HOUR) * 60 > startCountingHour * 60 + startCountingMinute) {

        } else if (calendarG.get(Calendar.MONTH) == startCountingMinute && calendarG.get(Calendar.HOUR) == startCountingHour) {

        } else if (startCountingHour * 60 +startCountingMinute >= 1380) {
            int minutesAfterMidNight = 1440 + calendarG.compareTo()
        }*/
        minutesTillNextSpins = (calendarG.getTimeInMillis() - minutesTillNextSpinsCallendar.getTimeInMillis()) / 1000 / 60;
        if (minutesTillNextSpins >= 60) {
            for (int i = 0; i <= minutesTillNextSpins - 60; i += 60) {
                if (saveData.getNumberOfSpins() < 50) {
                    if (saveData.getNumberOfSpins() <= 45) {
                        saveData.addSpins(5);
                    } else {
                        saveData.addSpins(50 - saveData.getNumberOfSpins());
                    }
                }
                saveData.setMillis(saveData.getMillis() + 60 * 60 * 1000);
            }
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(saveData.getMillis());
            minutesTillNextSpinsCallendar = gregorianCalendar;
        }

        for (BigChest bigChest : bigChests) {
            bigChest.update(dt);
        }

        Array<Cloud> cloudsToRemove = new Array<>();
        outerloop:
        for (Cloud cloud : clouds) {
                if (!cloud.isSecondScreen()) {
                    if (cloud.getY() > FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM && cloud.getScreen() == FallingMan.MENU_SCREEN) {
                        for (Cloud cloudGetPos : clouds) {
                            if (!cloudGetPos.isSecondScreen()) {
                                cloudsPositionForNextScreen.add(new Vector2(cloudGetPos.getX(), cloudGetPos.getY()));
                            }
                        }
                        currentScreen = FallingMan.MENU_SCREEN;
                        break outerloop;
                    } else if (cloud.getY() < 0 && cloud.getScreen() == FallingMan.SHOP_SCREEN) {
                        for (Cloud cloudGetPos : clouds) {
                            if (!cloudGetPos.isSecondScreen()) {
                                cloudsPositionForNextScreen.add(new Vector2(cloudGetPos.getX(), cloudGetPos.getY()));
                            }
                        }
                        currentScreen = FallingMan.SHOP_SCREEN;
                        break outerloop;
                    }
                    if (cloud.getScreen() == FallingMan.MENU_SCREEN) {
                        cloud.update(dt, 0, 1.2f);
                    } else if (cloud.getScreen() == FallingMan.SHOP_SCREEN) {
                        cloud.update(dt, 0, -1.2f);
                    }
                } else if (cloud.getY() < -FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM && cloud.getScreen() == FallingMan.MENU_SCREEN) {
                    cloudsToRemove.add(cloud);
                } else if (cloud.getScreen() == FallingMan.MENU_SCREEN) {
                    cloud.update(dt, 0, -1.2f);
                } else if (cloud.getY() > FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM * 2) {
                    cloudsToRemove.add(cloud);
                } else {
                    cloud.update(dt, 0, 1.2f);
                }
        }
        clouds.removeAll(cloudsToRemove, false);

        goldAndHighScoresBackground.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), gamePort.getWorldHeight());
        goldAndHighScoresIcons.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), gamePort.getWorldHeight());
        Array<OnePartRoll> flytingRollsToRemove = new Array<>();
        for (OnePartRoll flyingRoll : flyingRolls) {
            if (flyingRoll.getCurrentTextureNumber() == 0) {
                flyingRoll.flyingRollUpdate(dt, new Vector2(FallingMan.MIN_WORLD_WIDTH / 2f / FallingMan.PPM, oneArmBandits.get(0).getY() + 103 / FallingMan.PPM), saveData);
            } else if (flyingRoll.getCurrentTextureNumber() == 1) {
                flyingRoll.flyingRollUpdate(dt, new Vector2(goldAndHighScoresIcons.getX() + goldAndHighScoresIcons.getWidth() / 2, goldAndHighScoresIcons.getY() + goldAndHighScoresIcons.getHeight() / 4 * 3), saveData);
            }
            if (flyingRoll.isToRemove()) {
                if (flyingRoll.getCurrentTextureNumber() == 0) {
                    saveData.addSpins(1);
                    flytingRollsToRemove.add(flyingRoll);
                } else if (flyingRoll.getCurrentTextureNumber() == 1) {
                    goldAndHighScoresIcons.setGoldTextScale(1.5f);
                    saveData.addGold(300);
                    goldAndHighScoresIcons.setGold(saveData.getGold());
                    flytingRollsToRemove.add(flyingRoll);
                } else {
                    flytingRollsToRemove.add(flyingRoll);
                }
            }
        }
        flyingRolls.removeAll(flytingRollsToRemove, false);
        for (SpinsAmountLine spinsAmountLine : spinsAmountLines) {
            spinsAmountLine.setScale((float) (Math.min(saveData.getNumberOfSpins(), 50)) * 1.0057f, 1);
        }
        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2) / FallingMan.PPM, (gamePort.getWorldHeight() * FallingMan.PPM / 2) / FallingMan.PPM, 0);
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        //clear screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render map
        renderer.render();

        //render box2d debug renderer
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();

        for (Roll roll : rolls) {

            roll.draw(game.batch);
        }
        for (OnePartRoll smallRoll : smallRolls) {
            smallRoll.draw(game.batch);
        }
        for (SpinsBackground spinsBackground : spinsBackgrounds) {
            spinsBackground.draw(game.batch);
        }
        for (SpinsAmountLine spinsAmountLine : spinsAmountLines) {
            spinsAmountLine.draw(game.batch);
        }
        for (OneArmBandit oneArmBandit : oneArmBandits) {
            oneArmBandit.draw(game.batch);
        }

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(238 / 256f, 188 / 256f, 29 / 256f, 1);
        font.getData().setScale(0.0045f);
        if (saveData.getNumberOfSpins() > 50) {
            int numberOfSpins = saveData.getNumberOfSpins();
            GlyphLayout glyphLayout = new GlyphLayout(font, "+" + (numberOfSpins - 50) + " spins");
            font.draw(game.batch, "+" + (numberOfSpins - 50) + " spins", 720 / FallingMan.PPM - glyphLayout.width / 2, gamePort.getWorldHeight() / 2 - 400 / FallingMan.PPM + glyphLayout.height * 1.3f);
            startCountingDateAgain = true;
        } else if (saveData.getNumberOfSpins() == 50) {
            GlyphLayout glyphLayout = new GlyphLayout(font, "FULL");
            font.draw(game.batch, "FULL", 720 / FallingMan.PPM - glyphLayout.width / 2, gamePort.getWorldHeight() / 2 - 400 / FallingMan.PPM + glyphLayout.height * 1.3f);
            startCountingDateAgain = true;
        } else {

            if (startCountingDateAgain) {
                date = new Date();
                minutesTillNextSpinsCallendar = new GregorianCalendar();
                minutesTillNextSpinsCallendar.setTime(date);
                startCountingDateAgain = false;
            }
            GlyphLayout glyphLayout = new GlyphLayout(font, "5 spins in: " + (60 - minutesTillNextSpins) + " min");
            font.draw(game.batch, String.valueOf("5 spins in: " + (60 - minutesTillNextSpins) + " min"), 720 / FallingMan.PPM - glyphLayout.width / 2, gamePort.getWorldHeight() / 2 - 400 / FallingMan.PPM + glyphLayout.height * 1.3f);
        }

        int spinsInAmountLineNumber = saveData.getNumberOfSpins() < 50 ? saveData.getNumberOfSpins() : 50;
        GlyphLayout glyphLayout1 = new GlyphLayout(font, spinsInAmountLineNumber + "/50");
        float spinsYPos = (spinsBackgrounds.size > 0 ? spinsBackgrounds.get(0).getY() : 0) + glyphLayout1.height * 1.4f;
        font.draw(game.batch, spinsInAmountLineNumber + "/50", 720 / FallingMan.PPM - glyphLayout1.width / 2, spinsYPos);

        for (Button button : buttons) {
            button.draw(game.batch);

        }

        goldAndHighScoresBackground.draw(game.batch);
        goldAndHighScoresIcons.draw(game.batch);


        for (BigChest bigChest : bigChests) {
            bigChest.draw(game.batch, gamePort.getWorldHeight());
        }
        for (OnePartRoll flyingRoll : flyingRolls) {
            flyingRoll.draw(game.batch);
        }

        for (Cloud cloud : clouds) {
            cloud.draw(game.batch);
        }

        game.batch.end();


        switch (currentScreen) {
            case FallingMan.MENU_SCREEN:
                dispose();
                FallingMan.gameScreen = new MenuScreen(game, cloudsPositionForNextScreen, gamePort.getWorldHeight());
                FallingMan.currentScreen = FallingMan.MENU_SCREEN;
                game.setScreen(FallingMan.gameScreen);
                break;
            case FallingMan.SHOP_SCREEN:
                dispose();
                FallingMan.gameScreen = new ShopScreen(game, cloudsPositionForNextScreen, gamePort.getWorldHeight(), false);
                FallingMan.currentScreen = FallingMan.SHOP_SCREEN;
                game.setScreen(FallingMan.gameScreen);
                break;
        }

        if (createOneArmedBanditBundle) {
            createOneArmedBanditObjects();
            createOneArmedBanditBundle = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        assetManager.getManager().dispose();
    }

    public void startRolling(float dt) {
        Random random = new Random();
        for (int i = 0; i < rolls.size; i++) {
            Roll roll = rolls.get(i);
            if (roll.isRollingCurrently()) {
                int nextRollPosDiffY;
                if (rollingTime > 1.6f) {
                    nextRollPosDiffY = Math.round((2.05f - (rollingTime < 2 ? rollingTime : 2f)) * 100);
                    //nextRollPosDiffY = Math.round(3.1f - rollingTime * 100);
                    rollingTime -= dt / 1.4f;
                } else {
                    nextRollPosDiffY = (random.nextInt(20) + 40);
                }
                if (roll.getY() - nextRollPosDiffY / FallingMan.PPM > gamePort.getWorldHeight() / 2 - 360 / FallingMan.PPM) {
                    roll.setPosition(roll.getX(), roll.getY() - nextRollPosDiffY / FallingMan.PPM);
                } else {
                    if (rollingTime > 1 + i * 0.5f) {
                        if (roll.getCurrentTextureNumber() == 3) {
                            roll.setCurrentTextureNumber(0);
                            roll.setNewRegion(roll.getCurrentTextureNumber());
                            roll.setPosition(roll.getX(), roll.getPosY());
                        } else {
                            roll.setCurrentTextureNumber(roll.getCurrentTextureNumber() + 1);
                            roll.setNewRegion(roll.getCurrentTextureNumber());
                            roll.setPosition(roll.getX(), roll.getPosY());
                        }
                        if ((roll.getCurrentTextureNumber() != 0 && roll.getCurrentTextureNumber() != 3) || i == 2 || random.nextBoolean()) {
                            if (i == 2) {
                                int figureNumber = rolls.get(0).getCurrentTextureNumber();
                                boolean allFiguresTheSame = true;
                                for (Roll rollFigureCheck : rolls) {
                                    if (figureNumber != rollFigureCheck.getCurrentTextureNumber()) {
                                        allFiguresTheSame = false;
                                    }
                                }
                                //temp
                                /*allFiguresTheSame = true;
                                for (Roll rollFigureCheck : rolls) {
                                    rollFigureCheck.setCurrentTextureNumber(3);
                                }*/
                                //temp end
                                if (allFiguresTheSame) {
                                    if (rolls.get(0).getCurrentTextureNumber() == 0 || rolls.get(0).getCurrentTextureNumber() == 1) {
                                        for (int j = 0; j < 3; j++) {
                                            OnePartRoll tempRoll = new OnePartRoll(this, 300 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, roll.getCurrentTextureNumber());
                                            tempRoll.startFlying();
                                            flyingRolls.add(tempRoll);
                                        }
                                        for (int j = 0; j < 4; j++) {
                                            OnePartRoll tempRoll = new OnePartRoll(this, 620 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, roll.getCurrentTextureNumber());
                                            tempRoll.startFlying();
                                            flyingRolls.add(tempRoll);

                                        }
                                        for (int j = 0; j < 3; j++) {
                                            OnePartRoll tempRoll = new OnePartRoll(this, 950 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, roll.getCurrentTextureNumber());
                                            tempRoll.startFlying();
                                            flyingRolls.add(tempRoll);

                                        }
                                    } else if (rolls.get(0).getCurrentTextureNumber() == 2) {
                                        for (int j = 0; j < 9; j++) {
                                            OnePartRoll tempRoll = new OnePartRoll(this, 300 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                                            tempRoll.startFlying();
                                            flyingRolls.add(tempRoll);
                                        }
                                        for (int j = 0; j < 12; j++) {
                                            OnePartRoll tempRoll = new OnePartRoll(this, 620 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                                            tempRoll.startFlying();
                                            flyingRolls.add(tempRoll);

                                        }
                                        for (int j = 0; j < 9; j++) {
                                            OnePartRoll tempRoll = new OnePartRoll(this, 950 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                                            tempRoll.startFlying();
                                            flyingRolls.add(tempRoll);

                                        }
                                    } else if (rolls.get(0).getCurrentTextureNumber() == 3) {
                                        bigChests.add(new BigChest(this, 720 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 120 / FallingMan.PPM));
                                    }
                                    spinButton.setLocked(true);
                                    winOneArmedBandit = true;
                                    smallRolls.add(new OnePartRoll(this, 300 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                    smallRolls.add(new OnePartRoll(this, 620 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                    smallRolls.add(new OnePartRoll(this, 950 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                    rolls = new Array<>();
                                } else {
                                    for (Roll roll1 : rolls) {
                                        if (roll1.getCurrentTextureNumber() == 0) {
                                            OnePartRoll tempRoll = new OnePartRoll(this, roll1.getPosX(), gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, roll1.getCurrentTextureNumber());
                                            tempRoll.startFlying();
                                            flyingRolls.add(tempRoll);
                                        }
                                    }
                                    for (Roll roll1 : rolls) {
                                        if (roll1.getCurrentTextureNumber() == 1) {
                                            OnePartRoll tempRoll = new OnePartRoll(this, roll1.getPosX(), gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, roll1.getCurrentTextureNumber());
                                            tempRoll.startFlying();
                                            flyingRolls.add(tempRoll);
                                        }
                                    }
                                    for (Roll roll1 : rolls) {
                                        if (roll1.getCurrentTextureNumber() == 2) {
                                            for (int j = 0; j < 3; j++) {
                                                OnePartRoll tempRoll = new OnePartRoll(this, roll1.getPosX(), gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, 1);
                                                tempRoll.startFlying();
                                                flyingRolls.add(tempRoll);
                                            }
                                        }
                                    }
                                    spinButton.setLocked(false);
                                    if (saveData.getNumberOfSpins() <= 0) {
                                        loseOneArmedBandit = true;
                                        //removeButton(spinButton);
                                        spinButton.setLocked(true);
                                    }
                                }
                                rollingTime = 0;
                                startRolling = false;


                            }
                            roll.setRollingCurrently(false);
                        } else {
                            rollingTime -= random.nextFloat() / 5;
                        }
                    } else if (roll.getCurrentTextureNumber() == 3) {
                        roll.setCurrentTextureNumber(0);
                        roll.setNewRegion(roll.getCurrentTextureNumber());
                        roll.setPosition(roll.getX(), roll.getPosY() - nextRollPosDiffY / FallingMan.PPM);
                    } else {
                        roll.setCurrentTextureNumber(roll.getCurrentTextureNumber() + 1);
                        roll.setNewRegion(roll.getCurrentTextureNumber());
                        roll.setPosition(roll.getX(), roll.getPosY() - nextRollPosDiffY / FallingMan.PPM);
                    }
                }
            }
        }
        rollingTime += dt;
    }

    public void winOneArmedBandit(float dt) {
        for (OnePartRoll smallRoll : smallRolls) {
            if (winOneArmedBanditTime > 3) {
                winOneArmedBanditTime = 0;
                winOneArmedBandit = false;
                break;
            } else {
                if (smallRoll.isWinOneArmedBanditScaleUp()) {
                    smallRoll.setScale(smallRoll.getScaleX() + 0.02f);
                    if (smallRoll.getScaleX() > 1.2) {
                        smallRoll.setWinOneArmedBanditScaleUp(false);
                    }
                } else {
                    smallRoll.setScale(smallRoll.getScaleX() - 0.02f);
                    if (smallRoll.getScaleX() < 1) {
                        smallRoll.setWinOneArmedBanditScaleUp(true);
                    }
                }
            }
        }
        if (!winOneArmedBandit) {
            int sizeOfSmallRolls = smallRolls.size;
            for (int i = 0; i < sizeOfSmallRolls; i++) {
                OnePartRoll smallRoll = smallRolls.get(0);
                if (!(saveData.getNumberOfSpins() <= 0) && bigChests.size == 0) {
                    spinButton.setLocked(false);
                }
                rolls.add(new Roll(this, world, smallRoll.getX(), gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM, smallRoll.getCurrentTextureNumber()));
                smallRolls.removeValue(smallRoll, false);
            }
        } else {
            winOneArmedBanditTime += dt;
        }
    }

    /*public void removeOneArmedBanditbundle() {
        winOneArmedBanditTime = 0;
        oneArmBandits = new Array<>();
        smallRolls = new Array<>();
        rolls = new Array<>();
        createOneArmedBanditBundle = false;
        startRolling = false;
    }*/

    public void createOneArmedBanditObjects() {
        oneArmBandits.add(new OneArmBandit(this, world, gamePort.getWorldHeight() / 2 - 400 / FallingMan.PPM));
        spinsBackgrounds.add(new SpinsBackground(this, world, oneArmBandits.get(0).getX() + 50 / FallingMan.PPM, oneArmBandits.get(0).getY() + 103 / FallingMan.PPM));
        spinsAmountLines.add(new SpinsAmountLine(this, world, oneArmBandits.get(0).getX() + 50 / FallingMan.PPM, oneArmBandits.get(0).getY() + 103 / FallingMan.PPM));
        spinsAmountLines.get(0).setScale((float) (Math.min(saveData.getNumberOfSpins(), 50)) * 1.0057f, 1);
        buttons = new Array<>();
        spinButton = new SpinButton(this, world, 448 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 360 / FallingMan.PPM, 544 / FallingMan.PPM, 192 / FallingMan.PPM);
        buttons.add(spinButton);
        startRolling = false;
        rollingTime = 0;
        Gdx.app.log("world height", String.valueOf(gamePort.getScreenHeight()));
        Roll roll1 = new Roll(this, world, 300 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM);
        Roll roll2 = new Roll(this, world, 620 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM);
        Roll roll3 = new Roll(this, world, 950 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM);
        rolls = new Array<>();
        rolls.add(roll1);
        rolls.add(roll2);
        rolls.add(roll3);
        winOneArmedBandit = false;
        winOneArmedBanditTime = 0;
        winOneArmedBanditScaleUp = true;

        smallRolls = new Array<>();

        if (saveData.getNumberOfSpins() <= 0) {
            spinButton.setLocked(true);
        }
    }

    public void generateWindows() {
        goldAndHighScoresBackground = new GoldAndHighScoresBackground(this, world);
        goldAndHighScoresIcons = new GoldAndHighScoresIcons(this, world, saveData.getGold(), saveData.getHighScore());
    }

    @Override
    public TextureAtlas getDefaultAtlas() {
        return defaultAtlas;
    }

    @Override
    public TextureAtlas getBigRockAtlas() {
        return null;
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public void setGameOver(boolean gameOver) {

    }

    @Override
    public void setLoadMenu(boolean loadMenu) {

    }

    @Override
    public void setStopRock(boolean stopRock) {

    }

    @Override
    public GoldAndHighScoresBackground getGoldAndHighScoresBackground() {
        return goldAndHighScoresBackground;
    }

    public void setGameScreen(GameScreen gameScreen) {
        dispose();
        game.setScreen(gameScreen);
    }

    @Override
    public FallingMan getFallingMan() {
        return game;
    }

    @Override
    public void setCurrentScreen(byte currentScreen) {
        this.currentScreen = currentScreen;
    }

    public void setStartRolling(boolean startRolling) {
        this.startRolling = startRolling;
    }

    public void removeButton(Button button) {
        buttons.removeValue(button, false);
    }

    public void setNumberOfSpins(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

    @Override
    public void addOnePartRolls(int numberOfOnePartRolls, int typeOfRoll) {
        for (int i = 0; i < numberOfOnePartRolls; i++) {
            OnePartRoll tempRoll = new OnePartRoll(this, 620 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 24 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, typeOfRoll);
            tempRoll.startFlying();
            flyingRolls.add(tempRoll);
            //bigChests = new Array<>();
        }
    }

    @Override
    public void addOnePartRolls(int numberOfOnePartRolls, int typeOfRoll, Vector2 pos, String transactionName) {

    }

    public void removeChest(BigChest bigChest) {
        bigChests.removeValue(bigChest, false);
    }

    public SpinButton getSpinButton() {
        return spinButton;
    }

    @Override
    public GameAssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public TextureAtlas getPlayerAtlas() {
        return null;
    }

    @Override
    public GoldAndHighScoresIcons getGoldAndHighScoresIcons() {
        return goldAndHighScoresIcons;
    }

    @Override
    public HashMap<String, Boolean> getOwnedBodySprites() {
        return null;
    }
}
