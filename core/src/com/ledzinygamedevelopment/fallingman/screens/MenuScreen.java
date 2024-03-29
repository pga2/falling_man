package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.sprites.Smoke;
import com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects.Cloud;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.InApPurchasesScreenButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SettingsScreenButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.ad.WatchAdButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.sprites.windows.RewardCalendarWindow;
import com.ledzinygamedevelopment.fallingman.tools.AdsController;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.Utils;
import com.ledzinygamedevelopment.fallingman.tools.GsClientUtils;
import com.ledzinygamedevelopment.fallingman.tools.PlayerVectors;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;
import com.ledzinygamedevelopment.fallingman.tools.TutorialHandler;
import com.ledzinygamedevelopment.fallingman.tools.WorldContactListener;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class MenuScreen implements GameScreen {

    private final GameAssetManager assetManager;
    private TutorialHandler tutorialHandler;
    private TextureAtlas playerAtlas;
    private TextureAtlas windowAtlas;
    private byte currentScreen;
    //private Array<Rock> rocks;
    private OrthographicCamera gameCam;
    private OrthographicCamera gameCamBehind0;
    private OrthographicCamera gameCamBehind1;
    private ExtendViewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private TiledMap mapBehind0;
    private TiledMap mapBehind1;
    private OrthogonalTiledMapRenderer renderer;
    private OrthogonalTiledMapRenderer rendererBehind0;
    private OrthogonalTiledMapRenderer rendererBehind1;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;
    private TextureAtlas defaultAtlas;
    //private final TextureAtlas bigRockAtlas;
    private FallingMan game;
    private Array<Body> worldBodies;
    private Array<Button> buttons;
    private boolean loadNewGame;
    private GoldAndHighScoresIcons goldAndHighScoresIcons;
    private GoldAndHighScoresBackground goldAndHighScoresBackground;
    private SaveData saveData;
    private boolean noButtonTouched;
    private Array<Cloud> clouds;
    private boolean changeScreen;
    private boolean firstTouch;
    private Vector2 lastTouchPos;
    private Vector2 previousTouchPos;
    private Array<Vector2> cloudsPositionForNextScreen;
    private Integer mapHeight;
    private float cloudSlowingDown;
    private float playerYBeforeLoop;
    private boolean newScreenJustOpened;
    private float firstOpenTimer;
    private BitmapFont font;
    private boolean fontScaleUp;
    private float fontScale;
    private float gameCamBehindPositionBack;
    private float gameCamBehindPositionFront;
    private float sunPos;
    private RayHandler rayHandler;
    private PointLight headLight;
    private boolean tutorialOn;
    private Array<RewardCalendarWindow> rewardCalendarWindows;
    private Array<OnePartRoll> flyingRolls;
    private Array<BigChest> bigChests;
    private Sprite logo;

    public MenuScreen(FallingMan game, Array<Vector2> cloudsPositionForNextScreen, float screenHeight, float gameCamBehindPositionBack, float gameCamBehindPositionFront, float sunPos, Color rendererColor, boolean newBackground) {
        this.game = game;
        currentScreen = FallingMan.CURRENT_SCREEN;

        assetManager = new GameAssetManager();
        assetManager.loadMenuScreen();
        assetManager.finishLoading();
        defaultAtlas = assetManager.getManager().get(assetManager.getMenuScreenDefault());
        windowAtlas = assetManager.getManager().get(assetManager.getPlayScreenWindow());
        //bigRockAtlas = assetManager.getManager().get(assetManager.getMenuScreenBigRock());
        playerAtlas = assetManager.getManager().get(assetManager.getPlayerSprite());
        font = assetManager.getManager().get(assetManager.getFont());
        gameCam = new OrthographicCamera();
        gameCamBehind0 = new OrthographicCamera();
        gameCamBehind1 = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/menu_map.tmx");
        mapBehind0 = mapLoader.load("maps/menu_map_behind.tmx");
        mapBehind1 = mapLoader.load("maps/menu_map_behind.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);
        rendererBehind0 = new OrthogonalTiledMapRenderer(mapBehind0, 1 / FallingMan.PPM);
        rendererBehind1 = new OrthogonalTiledMapRenderer(mapBehind1, 1 / FallingMan.PPM);
        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2f) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2f) / FallingMan.PPM, 0);
        gameCamBehind0.position.set((FallingMan.MIN_WORLD_WIDTH / 2f) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2f) / FallingMan.PPM, 0);
        gameCamBehind1.position.set((FallingMan.MIN_WORLD_WIDTH / 2f) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2f) / FallingMan.PPM, 0);

        world = new World(new Vector2(0, -3f), true);
        b2dr = new Box2DDebugRenderer();

        worldBodies = new Array<>();
        generateMapObjects();

        MapProperties mapProp = map.getProperties();
        saveData = new SaveData();
        saveData.notFirstAppUse();
        player = new Player(world, this, mapProp.get("height", Integer.class) * 32, saveData.getBodySpritesCurrentlyWear());
        world.setContactListener(new WorldContactListener(player, this));

        //rocks = new Array<>();
        /*for (int i = 0; i < 100; i++) {
            rocks.add(new Rock(this, world));
        }*/

        Array<Texture> rockTextures = new Array<>();
        for (String path : assetManager.getRockTexturesPaths()) {
            rockTextures.add((Texture) assetManager.getManager().get(path));
        }
        //rocks.add(new Rock(this, world, true, rockTextures));
        generateWindows();
        buttons = new Array<>();
        buttons.add(new InApPurchasesScreenButton(this, world, FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM - 256 / FallingMan.PPM - 64 / FallingMan.PPM, 0));
        buttons.add(new SettingsScreenButton(this, world, 64 / FallingMan.PPM, 0));
        //buttons.add(new PlayButton(this, world, (FallingMan.MIN_WORLD_WIDTH / 2 - 320) / FallingMan.PPM, player.b2body.getPosition().y + 200 / FallingMan.PPM, 640 / FallingMan.PPM, 224 / FallingMan.PPM));
        //buttons.add(new HighScoresButton(this, world, (FallingMan.MIN_WORLD_WIDTH / 2 - 320) / FallingMan.PPM, player.b2body.getPosition().y + 424 / FallingMan.PPM, 640 / FallingMan.PPM, 224 / FallingMan.PPM));
        //player.b2body.applyLinearImpulse(new Vector2(3, 0), player.b2body.getWorldCenter(), true);
        noButtonTouched = false;
        cloudSlowingDown = 0;
        clouds = new Array<>();

        mapHeight = mapProp.get("height", Integer.class);
        for (Vector2 pos : cloudsPositionForNextScreen) {
            Cloud cloud = new Cloud(this, 0, 0, true, FallingMan.ONE_ARMED_BANDIT_SCREEN);
            cloud.setPosition(pos.x, pos.y + mapHeight * 32 / FallingMan.PPM - screenHeight);
            clouds.add(cloud);
        }
        changeScreen = false;
        firstTouch = true;
        this.cloudsPositionForNextScreen = new Array<>();
        playerYBeforeLoop = 0;
        newScreenJustOpened = true;

        firstOpenTimer = 0;
        //font = new BitmapFont(Gdx.files.internal("test_font/FSM.fnt"), false);
        fontScale = 0.006f;
        fontScaleUp = true;
        if (newBackground) {
            this.gameCamBehindPositionBack = player.b2body.getPosition().y / 2;
            this.gameCamBehindPositionFront = player.b2body.getPosition().y / 2;
            this.sunPos = 35;
        } else {
            this.gameCamBehindPositionBack = gameCamBehindPositionBack;
            this.gameCamBehindPositionFront = gameCamBehindPositionFront;
            this.sunPos = sunPos;
            rendererBehind0.getBatch().setColor(rendererColor);
            rendererBehind1.getBatch().setColor(rendererColor);
        }

        //GsClientUtils.saveData(game.gsClient, 1);
        //GsClientUtils.loadData(game.gsClient, this);
        SaveData saveData = new SaveData();
        if (saveData.getSaveUpdated()) {
            saveData.setSaveUpdated(false);
            GsClientUtils.saveData(game.gsClient, saveData.getSaveCounter());
        }
        GsClientUtils.loadData(game.gsClient, this);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(1);
        headLight = new PointLight(rayHandler, 100, Color.BLACK, 4000 / FallingMan.PPM, player.b2body.getPosition().x, player.b2body.getPosition().y);
        headLight.attachToBody(player.b2body);
        headLight.setIgnoreAttachedBody(true);
        Filter filter = new Filter();
        filter.categoryBits = FallingMan.PLAYER_HEAD_BIT;
        filter.maskBits = FallingMan.WALL_INSIDE_TOWER | FallingMan.ROCK_BIT | FallingMan.STOP_WALKING_ENEMY_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.DEFAULT_BIT;
        headLight.setContactFilter(filter);
        //gameCam.zoom = 5;

        /*if (game.toastCreator != null) {
            game.toastCreator.makeToast("data: \n" + saveData.getTestString());
        }*/
        tutorialOn = saveData.getTutorial();
        if (tutorialOn) {
            tutorialHandler = new TutorialHandler(this, world, FallingMan.MIN_WORLD_HEIGHT / 2f / FallingMan.PPM);
        }
        rewardCalendarWindows = new Array<>();
        flyingRolls = new Array<>();
        GregorianCalendar lastTimeDailyReward = new GregorianCalendar();
        GregorianCalendar currentTime = new GregorianCalendar();
        currentTime.setTime(new Date());
        if (saveData.getDayDailyReward() == 0) {
            GregorianCalendar prefsCallendar = new GregorianCalendar();
            prefsCallendar.setTime(new Date());
            saveData.setDayDailyReward((long) Math.floor(prefsCallendar.getTimeInMillis() / 86400000f));
            saveData.setDayInRowDailyReward(1);
            lastTimeDailyReward.setTimeInMillis(saveData.getDayDailyReward());
            rewardCalendarWindows.add(new RewardCalendarWindow(this, world, saveData.getDaysInRowDailyReward()));
        } else {
            if (saveData.getDayDailyReward() == (long) Math.floor(currentTime.getTimeInMillis() / 86400000f)) {
                Gdx.app.log("", "d");
            } else if (saveData.getDayDailyReward() + 1 == (long) Math.floor(currentTime.getTimeInMillis() / 86400000f)) {
                saveData.setDayInRowDailyReward(saveData.getDaysInRowDailyReward() + 1);
                saveData.setDayDailyReward((long) Math.floor(currentTime.getTimeInMillis() / 86400000f));
                rewardCalendarWindows.add(new RewardCalendarWindow(this, world, saveData.getDaysInRowDailyReward()));
            } else {
                Gdx.app.log("days diff", String.valueOf((long) Math.floor(currentTime.getTimeInMillis() / 86400000f) - saveData.getDayDailyReward()));
                saveData.setDayInRowDailyReward(1);
                saveData.setDayDailyReward((long) Math.floor(currentTime.getTimeInMillis() / 86400000f));
                rewardCalendarWindows.add(new RewardCalendarWindow(this, world, saveData.getDaysInRowDailyReward()));
            }
        }
        bigChests = new Array<>();
        if (game.getAdsController() != null)
        game.getAdsController().showRewardedVideo(true);

        logo = new Sprite();

        int logoWidth = 1000;
        int logoHeight = 948;
        logo.setBounds(0, 0, logoWidth / FallingMan.PPM, logoHeight / FallingMan.PPM);
        logo.setRegion(defaultAtlas.findRegion("logo"), 0, 0, logoWidth, logoHeight);
        logo.setPosition(FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - logo.getWidth() / 2, player.b2body.getPosition().y + 300 / FallingMan.PPM);
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            if (rewardCalendarWindows.size > 0) {
                for (RewardCalendarWindow rewardCalendarWindow : rewardCalendarWindows) {
                    rewardCalendarWindow.getReward();
                }
                saveData.setSaveUpdated(false);
                GsClientUtils.saveData(game.gsClient, saveData.getSaveCounter());
            } else if (bigChests.size > 0) {
                for (BigChest bigChest : bigChests) {
                    bigChest.setBigChestWasTouchedBodyParteAfterStageThree(true);
                }
            } else if (!(flyingRolls.size > 0)){
                if (!newScreenJustOpened) {
                    if (!changeScreen) {
                        if (firstTouch) {
                            previousTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                            firstTouch = false;
                        }
                        noButtonTouched = true;

                        // check if buttons click
                        Vector2 mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                        for (Button button : buttons) {
                            if (button.mouseOver(mouseVector) && !button.isLocked()) {
                                button.touched();
                                button.setClicked(true);
                                noButtonTouched = false;
                            } else if (button.isClicked()) {
                                button.setClicked(false);
                                button.restoreNotClickedTexture();
                            }
                        }
                        //previousTouchPos.x = previousTouchPos.x + player.b2body.getLinearVelocity().x / FallingMan.PPM;
                        if (playerYBeforeLoop == 0) {
                            playerYBeforeLoop = player.getY();
                        }
                        float playerYAfterLoop = player.getY();
                        previousTouchPos.y = previousTouchPos.y + playerYAfterLoop - playerYBeforeLoop;
                        playerYBeforeLoop = player.getY();
                        lastTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                    }

                    if (lastTouchPos.y - previousTouchPos.y < -200 / FallingMan.PPM) {
                        if (!changeScreen) {
                            Random random = new Random();
                            //currentScreen = FallingMan.MENU_SCREEN;
                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 26; j++) {
                                    clouds.add(new Cloud(this, ((i * 640) - random.nextInt(200)) / FallingMan.PPM, (player.getY() + gamePort.getWorldHeight() / 2) + ((140 * j) - random.nextInt(21)) / FallingMan.PPM, false, FallingMan.ONE_ARMED_BANDIT_SCREEN));
                                }
                            }
                            for (Button button : buttons) {
                                button.restoreNotClickedTexture();
                                button.setClicked(false);
                            }
                            changeScreen = true;

                            if (saveData.getSounds()) {
                                game.swapScreenSound.play();
                            }
                        }
                    }
                }
            }
        } else {
            if (bigChests.size > 0) {
                for (BigChest bigChest : bigChests) {
                    if (bigChest.isBigChestWasTouchedBodyParteAfterStageThree()) {
                        bigChest.setTouchedWhenRemoveWonBodyPartAllowed(true);
                        bigChest.setBigChestWasTouchedBodyParteAfterStageThree(false);
                    }
                }
            }
            newScreenJustOpened = false;
            if (noButtonTouched && !changeScreen) {
                currentScreen = FallingMan.PLAY_SCREEN;
            }
            for (Button button : buttons) {
                if (button.isClicked() && !button.isLocked()) {
                    button.notTouched();
                    button.setClicked(false);
                }
            }
        }
    }

    public void update(float dt) {

        //game.getAdsController().showRewardedVideo();

        handleInput(dt);
        world.step(Gdx.graphics.getDeltaTime(), 8, 5);
        rayHandler.update();

        //checking if should generate new map
        if (player.b2body.getPosition().y < (FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM + 30 / FallingMan.PPM) {
            generateNewMap();
        }

        /*gameCamBehind.position.y = player.b2body.getPosition().y;
        gameCamBehind.update();
        renderer.setView(gameCam);*/


        if (player.b2body.getPosition().x < 0) {
            player.b2body.applyLinearImpulse(new Vector2(new Random().nextInt(6) / 300f * Utils.getDeltaTimeX1(), 0 * Utils.getDeltaTimeX1()), player.b2body.getWorldCenter(), true);
        } else if (player.b2body.getPosition().x > FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM) {
            player.b2body.applyLinearImpulse(new Vector2(new Random().nextInt(6) / -300f * Utils.getDeltaTimeX1(), 0 * Utils.getDeltaTimeX1()), player.b2body.getWorldCenter(), true);
        } else if (player.b2body.getPosition().x > FallingMan.MIN_WORLD_WIDTH * 5f / 8 / FallingMan.PPM) {
            player.b2body.applyLinearImpulse(new Vector2((new Random().nextInt(7) - 3.5f) / 300f * Utils.getDeltaTimeX1(), 0 * Utils.getDeltaTimeX1()), player.b2body.getWorldCenter(), true);
        } else if (player.b2body.getPosition().x < FallingMan.MIN_WORLD_WIDTH * 3f / 8 / FallingMan.PPM) {
            player.b2body.applyLinearImpulse(new Vector2((new Random().nextInt(7) - 2.5f) / 300f * Utils.getDeltaTimeX1(), 0 * Utils.getDeltaTimeX1()), player.b2body.getWorldCenter(), true);
        } else {
            player.b2body.applyLinearImpulse(new Vector2((new Random().nextInt(11) - 5) / 300f * Utils.getDeltaTimeX1(), 0 * Utils.getDeltaTimeX1()), player.b2body.getWorldCenter(), true);
        }

        player.update(dt);

        logo.setPosition(FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - logo.getWidth() / 2, player.b2body.getPosition().y + 300 / FallingMan.PPM);
        goldAndHighScoresBackground.update(dt, player.b2body.getPosition(), gamePort.getWorldHeight());
        goldAndHighScoresIcons.update(dt, player.b2body.getPosition(), gamePort.getWorldHeight());

        /*for (Rock rock : rocks) {
            rock.update(dt, player.b2body.getPosition(), false, true);
        }*/
        if (player.b2body.getLinearVelocity().y < -20) {
            player.b2body.setLinearVelocity(new Vector2(player.b2body.getLinearVelocity().x, -20));
        }
        for (Button button : buttons) {
            button.update(dt, new Vector2(button.getX(), player.b2body.getPosition().y - gamePort.getWorldHeight() / 2 + 64 / FallingMan.PPM));
        }

        Array<Cloud> cloudsToRemove = new Array<>();
        outerloop:
        for (Cloud cloud : clouds) {
            if (!cloud.isSecondScreen()) {
                if (cloud.getY() < player.getY() - gamePort.getWorldHeight() / 2) {
                    float firstCloudYPos = cloud.getY();
                    for (Cloud cloudGetPos : clouds) {
                        if (!cloudGetPos.isSecondScreen()) {
                            cloudsPositionForNextScreen.add(new Vector2(cloudGetPos.getX(), cloudGetPos.getY() - firstCloudYPos));
                        }
                    }
                    currentScreen = cloud.getScreen();
                    break outerloop;
                }
                cloud.update(dt, 0, (player.b2body.getLinearVelocity().y - 120) / FallingMan.PPM * 60 * Gdx.graphics.getDeltaTime());
            } else if (cloud.getY() > player.getY() + gamePort.getWorldHeight() * 2) {
                cloudsToRemove.add(cloud);
            } else {
                if (Gdx.graphics.getDeltaTime() < 0.01666) {
                    cloud.update(dt, 0, (player.b2body.getLinearVelocity().y + 120) / FallingMan.PPM * 60 * Gdx.graphics.getDeltaTime());
                } else {
                    cloud.update(dt, 0, (player.b2body.getLinearVelocity().y + 120) / FallingMan.PPM);
                }
            }
        }
        clouds.removeAll(cloudsToRemove, false);

        for (RewardCalendarWindow rewardCalendarWindow : rewardCalendarWindows) {
            if (rewardCalendarWindow.isToRemoveAnimationEnded()) {
                rewardCalendarWindows = new Array<>();
            } else {
                rewardCalendarWindow.update(dt, player.b2body.getPosition().y);
            }
        }

        Array<OnePartRoll> flyingRollsToRemove = new Array<>();
        for (OnePartRoll onePartRoll : flyingRolls) {
            if (onePartRoll.isToRemove()) {
                switch (onePartRoll.getCurrentTextureNumber()) {
                    case 1:
                        saveData.addGold((int) onePartRoll.getAmount());
                        goldAndHighScoresIcons.setGold(saveData.getGold());
                        flyingRollsToRemove.add(onePartRoll);
                        goldAndHighScoresIcons.setGoldTextScale(1.5f);
                        break;
                    case 0:
                        saveData.addSpins((int) onePartRoll.getAmount());
                        flyingRollsToRemove.add(onePartRoll);
                        goldAndHighScoresIcons.setGoldTextScale(1.5f);
                        break;
                }
            } else {
                onePartRoll.flyingRollUpdate(dt, new Vector2(FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - onePartRoll.getWidth() / 2, goldAndHighScoresIcons.getY() + 350 / FallingMan.PPM), saveData);
            }
        }
        flyingRolls.removeAll(flyingRollsToRemove, false);

        for (BigChest bigChest : bigChests) {
            bigChest.update(dt);
        }

        if (tutorialOn) {
            tutorialHandler.update(dt, player.b2body.getPosition().y, gamePort.getWorldHeight());
        }

        firstOpenTimer += dt;
    }

    @Override
    public void render(float delta) {
        update(delta);

        //clear screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render map
        sunPos += FallingMan.SUN_SPEED * 60 * Gdx.graphics.getDeltaTime();
        gameCam.position.y = sunPos;
        //gameCam.position.y = 16.9f;
        gameCam.update();
        prepareDayAndNightCycle();
        rendererBehind0.setView(gameCam);
        //rendererBehind0.render(new int[]{0});
        rendererBehind0.render(new int[]{0, 1});
        gameCamBehindPositionBack += player.b2body.getLinearVelocity().y / 2 / FallingMan.PPM;
        if (gameCamBehindPositionBack < -(mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM / 2) {
            gameCamBehindPositionBack += (mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM;
        }
        gameCam.position.y = gameCamBehindPositionBack;
        gameCam.update();
        rendererBehind0.setView(gameCam);
        rendererBehind0.render(new int[]{2});
        gameCam.position.y = gameCamBehindPositionBack + (mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM;
        gameCam.update();
        rendererBehind1.setView(gameCam);
        rendererBehind1.render(new int[]{2});

        gameCamBehindPositionFront += player.b2body.getLinearVelocity().y / 1.5f / FallingMan.PPM;
        if (gameCamBehindPositionFront < -(mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM / 2) {
            gameCamBehindPositionFront += (mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM;
        }

        gameCam.position.y = gameCamBehindPositionFront;
        gameCam.update();
        rendererBehind0.setView(gameCam);
        rendererBehind0.render(new int[]{3});
        gameCam.position.y = gameCamBehindPositionFront + (mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM;
        gameCam.update();
        rendererBehind1.setView(gameCam);
        rendererBehind1.render(new int[]{3});
        gameCam.position.y = player.b2body.getPosition().y;
        gameCam.update();
        renderer.setView(gameCam);
        //renderer.render();


        game.batch.setProjectionMatrix(gameCam.combined);
        rayHandler.setCombinedMatrix(gameCam);
        //game.batch.setProjectionMatrix(gameCamBehind.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (PlayerBodyPart bodyPart : player.getBodyParts()) {
            bodyPart.draw(game.batch);
        }
        /*for (Rock rock : rocks) {
            rock.draw(game.batch);
        }*/
        //goldAndHighScoresBackground.draw(game.batch);
        //goldAndHighScoresIcons.draw(game.batch);
        for (Button button : buttons) {
            button.draw(game.batch);
        }

        logo.draw(game.batch);

        /*//preparing font
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(100 / 256f, 80 / 256f, 0 / 256f, 1);
        if (fontScaleUp) {
            if (fontScale < 0.0066f) {
                fontScale += 0.00004f * 60 * Gdx.graphics.getDeltaTime();
                font.getData().setScale(fontScale);
            } else {
                font.getData().setScale(fontScale);
                //fontScale += 0.00004f;
                fontScaleUp = false;
            }
        } else {
            if (fontScale > 0.0058f) {
                fontScale -= 0.00004f * 60 * Gdx.graphics.getDeltaTime();
                font.getData().setScale(fontScale);
            } else {
                fontScale -= 0.00004f * 60 * Gdx.graphics.getDeltaTime();
                font.getData().setScale(fontScale);
                fontScaleUp = true;
            }
        }
        GlyphLayout glyphLayout = new GlyphLayout(font, "SWIPE UP\nOR\nTOUCH THE SCREEN");
        font.draw(game.batch, "SWIPE UP\nOR\nTOUCH THE SCREEN", 720 / FallingMan.PPM - glyphLayout.width / 2, player.getY() + glyphLayout.height * 2 - 1200 / FallingMan.PPM, glyphLayout.width, Align.center, false);*/

        if (tutorialOn) {
            tutorialHandler.draw(game.batch);
        }

        for (RewardCalendarWindow rewardCalendarWindow : rewardCalendarWindows) {
            rewardCalendarWindow.draw(game.batch);
        }

        for (OnePartRoll onePartRoll : flyingRolls) {
            onePartRoll.draw(game.batch);
        }

        for (BigChest bigChest : bigChests) {
            bigChest.draw(game.batch, 0);
        }


        for (Cloud cloud : clouds) {
            cloud.draw(game.batch);
        }

        //render box2d debug renderer
        //b2dr.render(world, gameCam.combined);

        game.batch.end();

        //rayHandler.render();

        switch (currentScreen) {
            case FallingMan.ONE_ARMED_BANDIT_SCREEN:
                dispose();
                FallingMan.gameScreen = new OneArmedBanditScreen(game, cloudsPositionForNextScreen, player.getY(), false, gameCamBehindPositionBack, gameCamBehindPositionFront, sunPos, rendererBehind0.getBatch().getColor());
                FallingMan.currentScreen = FallingMan.ONE_ARMED_BANDIT_SCREEN;
                game.setScreen(FallingMan.gameScreen);
                break;
            case FallingMan.PLAY_SCREEN:
                PlayerVectors playerVectors = new PlayerVectors(player, true);

                MapProperties mapProp = map.getProperties();
                //Vector3 rockPos = new Vector3(rocks.get(0).getB2body().getPosition().x, rocks.get(0).getB2body().getPosition().y - player.b2body.getPosition().y, rocks.get(0).getB2body().getAngle());
                //float rockAnimationTimer = rocks.get(0).getAnimationTimer();
                playerVectors.calculatePlayerVectors(mapProp.get("height", Integer.class) * 32);
                dispose();
                FallingMan.gameScreen = new PlayScreen(game, playerVectors, /*rockPos, rockAnimationTimer,*/ gameCamBehindPositionBack, gameCamBehindPositionFront, sunPos, rendererBehind0.getBatch().getColor());
                FallingMan.currentScreen = FallingMan.PLAY_SCREEN;
                if (saveData.getMusic()) {
                    game.menuScreenMusic.stop();
                }
                game.setScreen(FallingMan.gameScreen);
                break;
            case FallingMan.IN_APP_PURCHASES_SCREEN:
                dispose();
                FallingMan.gameScreen = new InAppPurchasesScreen(game, cloudsPositionForNextScreen, player.getY(), false, gameCamBehindPositionBack, gameCamBehindPositionFront, sunPos, rendererBehind0.getBatch().getColor());
                FallingMan.currentScreen = FallingMan.IN_APP_PURCHASES_SCREEN;
                game.setScreen(FallingMan.gameScreen);
                break;
            case FallingMan.SETTINGS_SCREEN:
                dispose();
                FallingMan.gameScreen = new SettingsScreen(game, cloudsPositionForNextScreen, player.getY(), gameCamBehindPositionBack, gameCamBehindPositionFront, sunPos, rendererBehind0.getBatch().getColor());
                FallingMan.currentScreen = FallingMan.SETTINGS_SCREEN;
                game.setScreen(FallingMan.gameScreen);
                break;
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
        mapBehind0.dispose();
        mapBehind1.dispose();
        renderer.dispose();
        rendererBehind0.dispose();
        rendererBehind1.dispose();
        world.dispose();
        b2dr.dispose();
        assetManager.getManager().dispose();
    }

    @Override
    public TextureAtlas getDefaultAtlas() {
        return defaultAtlas;
    }


    @Override
    public Player getPlayer() {
        return player;
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

    @Override
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

    @Override
    public void addOnePartRolls(int numberOfOnePartRolls, int typeOfRoll) {

    }

    @Override
    public void addOnePartRolls(int numberOfOnePartRolls, int typeOfRoll, Vector2 pos, String
            transactionName) {

    }

    @Override
    public void addOnePartRolls(int typeOfRoll, Vector2 pos, String transactionName) {

    }

    @Override
    public void removeChest(BigChest bigChest) {
        bigChests.removeValue(bigChest, false);
    }

    @Override
    public SpinButton getSpinButton() {
        return null;
    }

    @Override
    public GameAssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public TextureAtlas getPlayerAtlas() {
        return playerAtlas;
    }

    @Override
    public GoldAndHighScoresIcons getGoldAndHighScoresIcons() {
        return goldAndHighScoresIcons;
    }

    @Override
    public HashMap<String, Boolean> getOwnedBodySprites() {
        return null;
    }

    @Override
    public AdsController getAdsController() {
        return game.getAdsController();
    }

    @Override
    public void watchAdButtonClicked() {

    }

    @Override
    public void watchAdButtonClicked(WatchAdButton watchAdButton) {

    }

    @Override
    public Array<Button> getButtons() {
        return buttons;
    }

    @Override
    public void setNewLife(boolean newLife) {

    }

    @Override
    public FallingMan getGame() {
        return game;
    }

    @Override
    public SaveData getSaveData() {
        return saveData;
    }

    @Override
    public void setSmokeToAddPos(Vector2 smokeToAddPos) {

    }

    @Override
    public Array<Smoke> getSmokes() {
        return null;
    }

    @Override
    public boolean isReadyToCreateSmoke() {
        return false;
    }

    @Override
    public void setAddSmoke(boolean addSmoke) {

    }

    @Override
    public TextureAtlas getWindowAtlas() {
        return windowAtlas;
    }

    @Override
    public void setGoldX2(boolean goldX2) {

    }

    public void generateNewMap() {

        //transforming player position to new map
        Vector2 playerPosPrevious = new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);

        MapProperties mapProp = map.getProperties();
        player.updateBodyParts(mapProp.get("height", Integer.class) * 32, true);

        //transforming rocks position to new map
        /*for (Rock rock : rocks) {
            rock.generateMapRockUpdate(playerPosPrevious, mapProp.get("height", Integer.class) * 32);
        }*/

        for (OnePartRoll onePartRoll : flyingRolls) {
            onePartRoll.setPosition(onePartRoll.getX(), onePartRoll.getY() + player.b2body.getPosition().y - playerPosPrevious.y);
            onePartRoll.setPosY(onePartRoll.getPosY() + player.b2body.getPosition().y - playerPosPrevious.y);
        }

        for (Cloud cloud : clouds) {
            float posYDiff = cloud.getY() - playerPosPrevious.y;
            cloud.setPosition(cloud.getX(), player.b2body.getPosition().y + posYDiff);
        }

    }

    public void generateMapObjects() {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //walls
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / FallingMan.PPM, (rect.getY() + rect.getHeight() / 2) / FallingMan.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / FallingMan.PPM, (rect.getHeight() / 2) / FallingMan.PPM);
            fdef.shape = shape;
            //
            body.createFixture(fdef);
            worldBodies.add(body);
        }
    }

    public void generateWindows() {
        goldAndHighScoresBackground = new GoldAndHighScoresBackground(this, world);
        goldAndHighScoresIcons = new GoldAndHighScoresIcons(this, world, saveData.getGold(), saveData.getHighScore());
    }

    private void prepareDayAndNightCycle() {
        if (sunPos > 54 && sunPos < 62) {
            rendererBehind0.getBatch().setColor(rendererBehind0.getBatch().getColor().r, (62 - sunPos) / 9 + 0.11111f, (62 - sunPos) / 16 + 0.5f, 1);
            if (sunPos > 59 && sunPos < 62) {
                rendererBehind0.getBatch().setColor((62 - sunPos) / 3.375f + 0.11111f, rendererBehind0.getBatch().getColor().g, rendererBehind0.getBatch().getColor().b, 1);
            }
            rendererBehind1.getBatch().setColor(rendererBehind0.getBatch().getColor());
            //rayHandler.setAmbientLight((62 - sunPos) / 2 + 0.5f);
        } else if (sunPos > 16.8 && sunPos < 24.8) {
            rendererBehind0.getBatch().setColor(rendererBehind0.getBatch().getColor().r, 1 - ((24.8f - sunPos) / 9) + 0.11111f, 1 - ((24.8f - sunPos) / 16) + 0.5f, 1);
            if (sunPos > 16.8 && sunPos < 19.8) {
                rendererBehind0.getBatch().setColor(1 - ((19.8f - sunPos) / 3.375f) + 0.11111f, rendererBehind0.getBatch().getColor().g, rendererBehind0.getBatch().getColor().b, 1);
            }
            rendererBehind1.getBatch().setColor(rendererBehind0.getBatch().getColor());
            //rayHandler.setAmbientLight(1 - ((62 - sunPos) / 2 + 0.5f));
        }
        if (sunPos > 99) {
            sunPos = FallingMan.MAX_WORLD_HEIGHT / 2f / FallingMan.PPM;
        }
    }

    public Array<Cloud> getClouds() {
        return clouds;
    }

    public boolean isChangeScreen() {
        return changeScreen;
    }

    public void setChangeScreen(boolean changeScreen) {
        this.changeScreen = changeScreen;
    }

    public ExtendViewport getGamePort() {
        return gamePort;
    }

    public Array<OnePartRoll> getFlyingRolls() {
        return flyingRolls;
    }

    public Array<BigChest> getBigChests() {
        return bigChests;
    }
}
