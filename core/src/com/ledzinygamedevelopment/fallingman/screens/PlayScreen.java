package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.scenes.HUD;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.huntingspider.HuntingSpider;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.fallingobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.font.FontMapObject;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.SpiderWeb;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.ShowLeaderboardButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.ad.WatchAdButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coins.Spark;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OneArmBandit;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.Roll;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.sprites.windows.DefaultWindow;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.tools.AdsController;
import com.ledzinygamedevelopment.fallingman.tools.B2WorldCreator;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.GsClientUtils;
import com.ledzinygamedevelopment.fallingman.tools.PlayScreenTutorialHandler;
import com.ledzinygamedevelopment.fallingman.tools.PlayerVectors;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;
import com.ledzinygamedevelopment.fallingman.tools.WorldContactListener;
import com.ledzinygamedevelopment.fallingman.tools.entities.B2SteeringEntityContainer;
import com.ledzinygamedevelopment.fallingman.tools.entities.B2dSteeringEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class PlayScreen implements GameScreen {

    private boolean tutorialOn;
    private byte currentScreen;
    private SaveData saveData;
    private BitmapFont font;
    private HUD hud;
    private FallingMan game;
    private TextureAtlas defaultAtlas;
    private TextureAtlas windowAtlas;
    private TextureAtlas bigRockAtlas;
    private GameAssetManager assetManager;
    private TextureAtlas playerAtlas;

    private OrthographicCamera gameCam;
    private OrthographicCamera gameCamBehind0;
    private OrthographicCamera gameCamBehind1;
    private Viewport gamePort;

    //map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private TiledMap mapBehind0;
    private TiledMap mapBehind1;
    private OrthogonalTiledMapRenderer renderer;
    private OrthogonalTiledMapRenderer rendererBehind0;
    private OrthogonalTiledMapRenderer rendererBehind1;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //player
    private Player player;
    private boolean gameOver;

    //all bodies from map
    private B2WorldCreator b2WorldCreator;

    private Array<OneArmBandit> oneArmBandits;
    private Array<Roll> rolls;
    private SpinButton spinButton;
    private Array<Button> buttons;
    private boolean startRolling;
    private float rollingTime;
    private Array<OnePartRoll> smallRolls;
    private boolean winOneArmedBandit;
    private float winOneArmedBanditTime;
    private boolean winOneArmedBanditScaleUp;
    private boolean stopRock;
    private int numberOfSpins;
    private float loseOneArmedBanditEndTime;
    private boolean loseOneArmedBandit;

    //Rocks falling from sky
    private Array<Rock> rocks;

    private LinkedList<Float> allFPSData;

    private Array<DefaultWindow> defaultWindows;
    private boolean loadNewGame;
    private boolean loadMenu;

    private ArrayList<FontMapObject> fontMapObjects;
    private Array<Spark> sparks;

    private Array<HuntingSpider> huntingSpiders;
    private Array<B2SteeringEntityContainer> b2SteeringEntityContainers;
    private float gameCamBehindPositionBack;
    private boolean watchAdButtonClicked;
    private Vector2 mouseVector;
    private boolean gameOverScreenTouched;
    private boolean newLife;
    private long goldFromPreviousLife;
    private long distFromPreviousLife;
    private float gameCamBehindPositionFront;
    private boolean dontStopAtNotStraightWalls;
    private float sunPos;
    private RayHandler rayHandler;
    private PointLight headLight;
    private boolean turnOnLights;
    private PlayScreenTutorialHandler playScreenTutorialHandler;

    public PlayScreen(FallingMan game, PlayerVectors playerVectors, /*Vector3 rockPos, float rockAnimationTimer,*/ float gameCamBehindPositionBack, float gameCamBehindPositionFront, float sunPos, Color rendererColor) {
        assetManager = new GameAssetManager();
        assetManager.loadPlayScreen();
        assetManager.getManager().finishLoading();
        defaultAtlas = assetManager.getManager().get(assetManager.getPlayScreenDefault());
        windowAtlas = assetManager.getManager().get(assetManager.getPlayScreenWindow());
        bigRockAtlas = assetManager.getManager().get(assetManager.getPlayScreenBigRock());
        playerAtlas = assetManager.getManager().get(assetManager.getPlayerSprite());
        font = assetManager.getManager().get(assetManager.getFont());

        //atlas = new TextureAtlas("player.pack");
        this.game = game;
        this.gameCamBehindPositionBack = gameCamBehindPositionBack;
        this.gameCamBehindPositionFront = gameCamBehindPositionFront;
        currentScreen = FallingMan.CURRENT_SCREEN;
        gameCam = new OrthographicCamera();
        gameCamBehind0 = new OrthographicCamera();
        gameCamBehind1 = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("start_map0.tmx");
        mapBehind0 = mapLoader.load("menu_map_behind.tmx");
        mapBehind1 = mapLoader.load("menu_map_behind.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);
        rendererBehind0 = new OrthogonalTiledMapRenderer(mapBehind0, 1 / FallingMan.PPM);
        rendererBehind1 = new OrthogonalTiledMapRenderer(mapBehind1, 1 / FallingMan.PPM);
        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2f) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2f) / FallingMan.PPM, 0);
        gameCamBehind0.position.set((FallingMan.MIN_WORLD_WIDTH / 2f) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2f) / FallingMan.PPM, 0);
        gameCamBehind1.position.set((FallingMan.MIN_WORLD_WIDTH / 2f) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2f) / FallingMan.PPM, 0);

        world = new World(new Vector2(0, -3f), true);
        b2dr = new Box2DDebugRenderer();

        b2WorldCreator = new B2WorldCreator(this, world, map);
        saveData = new SaveData();
        //creating player
        MapProperties mapProp = map.getProperties();
        player = new Player(world, this, mapProp.get("height", Integer.class) * 32, saveData.getBodySpritesCurrentlyWear());
        playerVectors.setNewPlayerVectorsFromPreviusMap(player, mapProp.get("height", Integer.class) * 32);
        gameOver = false;
        world.setContactListener(new WorldContactListener(player, this));

        //createOneArmedBanditObjects();
        rolls = new Array<>();
        oneArmBandits = new Array<>();
        buttons = new Array<>();
        smallRolls = new Array<>();
        stopRock = false;
        loseOneArmedBanditEndTime = 0;
        loseOneArmedBandit = false;
        //Rocks falling from sky
        rocks = new Array<>();
        /*for (int i = 0; i < 50; i++) {
            rocks.add(new Rock(this, world));
        }*/
        Array<Texture> rockTextures = new Array<>();
        for (String path : assetManager.getRockTexturesPaths()) {
            rockTextures.add((Texture) assetManager.getManager().get(path));
        }
        Rock rock = new Rock(this, world, true, rockTextures);
        //rock.getB2body().setTransform(rockPos.x, rockPos.y + player.b2body.getPosition().y, rockPos.z);
        //rock.setAnimationTimer(rockAnimationTimer);

        rocks.add(rock);

        allFPSData = new LinkedList<>();
        //Gdx.app.log("roll y", String.valueOf(roll.getX()));
        //gameCam.zoom = 1.1f;
        hud = new HUD(game.batch, this);

        defaultWindows = new Array<>();
        loadNewGame = false;
        loadMenu = false;
        fontMapObjects = new ArrayList<>();
        sparks = new Array<>();
        huntingSpiders = new Array<>();
        b2SteeringEntityContainers = new Array<>();
        watchAdButtonClicked = false;
        gameOverScreenTouched = false;
        newLife = false;
        goldFromPreviousLife = 0;
        distFromPreviousLife = 0;
        //-gameCam.zoom = 2f;
        dontStopAtNotStraightWalls = true;
        this.sunPos = sunPos;
        rendererBehind0.getBatch().setColor(rendererColor);
        rendererBehind1.getBatch().setColor(rendererColor);
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(1);
        headLight = new PointLight(rayHandler, 400, Color.BLACK, 2000 / FallingMan.PPM, player.b2body.getPosition().x, player.b2body.getPosition().y);
        headLight.attachToBody(player.b2body);
        headLight.setIgnoreAttachedBody(true);
        Filter filter = new Filter();
        filter.categoryBits = FallingMan.PLAYER_HEAD_BIT;
        filter.maskBits = FallingMan.WALL_INSIDE_TOWER | FallingMan.ROCK_BIT | FallingMan.STOP_WALKING_ENEMY_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT | FallingMan.DEFAULT_BIT;
        headLight.setContactFilter(filter);
        //highPerformance = false;
        //lHandLight = new PointLight(rayHandler, 100, Color.BLACK, 3200 / FallingMan.PPM, player.b2body.getPosition().x, player.b2body.getPosition().y);
        //rHandLight = new PointLight(rayHandler, 100, Color.BLACK, 3200 / FallingMan.PPM, player.b2body.getPosition().x, player.b2body.getPosition().y);
        //player.createHeadJoint();
        //player.b2body.applyLinearImpulse(new Vector2(100f, 0f), player.b2body.getWorldCenter(), true);
        turnOnLights = false;
        tutorialOn = saveData.getTutorial();
        if (tutorialOn) {
            playScreenTutorialHandler = new PlayScreenTutorialHandler(this, world, player.b2body.getPosition().y);
            player.createHeadJoint();
            saveData.addOneToTutorialCounter();
        }

        //vibrationTimer = 0;
    }

    public TextureAtlas getDefaultAtlas() {
        return defaultAtlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (!hud.isGameOverStage()) {
            //pc
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -5) {
                player.getBelly().getB2body().applyLinearImpulse(new Vector2(-0.05f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 5) {
                player.getBelly().getB2body().applyLinearImpulse(new Vector2(0.05f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                player.getBelly().getB2body().applyLinearImpulse(new Vector2(0f, 0.05f), player.getBelly().getB2body().getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                //player.restoreBodyParts();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.M)) {
                gameCam.zoom += 0.01f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.N)) {
                gameCam.zoom -= 0.01f;
            }

            //mobile
            if (Gdx.input.isTouched()) {
                if (tutorialOn) {
                    if (playScreenTutorialHandler.isTutorialOn()) {
                        player.setRemoveHeadJoint(true);
                        playScreenTutorialHandler.setTutorialOn(false);
                    }
                }
                // check if buttons click
                Vector2 mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                for (Button button : buttons) {
                    if (button.mouseOver(mouseVector) && !button.isLocked()) {
                        button.touched();
                        button.setClicked(true);
                    } else {
                        button.restoreNotClickedTexture();
                    }
                }


                //moving player
                if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2f) {
                    player.getBelly().getB2body().applyLinearImpulse(new Vector2(-0.05f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
                } else {
                    player.getBelly().getB2body().applyLinearImpulse(new Vector2(0.05f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
                }
            } else {
                for (Button button : buttons) {
                    if (button.isClicked() && !button.isLocked()) {
                        //player.setRemoveHeadJointsAndButton(true);
                        if (button.getClass().equals(SpinButton.class)) {
                            setStartRolling(true);
                            for (Roll roll : rolls) {
                                roll.setRollingCurrently(true);
                            }
                        }
                        button.notTouched();
                        //removeButton(button);
                        button.setClicked(false);
                    }
                }
            }
        } else {
            if (Gdx.input.isTouched()) {
                gameOverScreenTouched = true;
                mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                for (Button button : buttons) {
                    if (button.mouseOver(mouseVector) && !button.isLocked()) {
                        button.touched();
                        button.setClicked(true);
                    } else {
                        button.restoreNotClickedTexture();
                    }
                }
            } else {
                if (gameOverScreenTouched) {
                    boolean noButtonTouched = true;
                    for (Button button : buttons) {
                        if (button.isClicked() && !button.isLocked() && button.mouseOver(mouseVector)) {
                            //player.setRemoveHeadJointsAndButton(true);
                            button.notTouched();
                            button.setClicked(false);
                            noButtonTouched = false;
                        }
                    }
                    if (noButtonTouched) {
                        for (DefaultWindow defaultWindow : defaultWindows) {
                            if (defaultWindow.getTypeOfWindowText() == FallingMan.GAME_OVER_WINDOW && defaultWindow.isTapExist()) {
                                currentScreen = FallingMan.MENU_SCREEN;
                            }
                        }
                    }
                    gameOverScreenTouched = false;
                }
            }
        }
    }

    public void update(float dt) {
        handleInput(dt);
            world.step(1 / 60f, 8, 5);
            if (dontStopAtNotStraightWalls && player.b2body.getPosition().y > 63) {
                if (player.b2body.getLinearVelocity().y > -5) {
                    player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, -5);
                }
            }
            if (player.b2body.getPosition().y < (FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM + 30 / FallingMan.PPM) {
                generateNewMap();
            }

            Array<InteractiveObjectInterface> drawableInteractiveObjectToRemove = new Array<>();
            for (InteractiveObjectInterface interactiveTileObject : b2WorldCreator.getInteractiveTileObjects()) {
                interactiveTileObject.update(dt);
                if (interactiveTileObject.isTouched()) {
                    interactiveTileObject.touched();
                    if (!(interactiveTileObject instanceof SpiderWeb))
                        interactiveTileObject.setTouched(false);
                    if (interactiveTileObject.isToRemove()) {
                        drawableInteractiveObjectToRemove.add(interactiveTileObject);
                    }
                }
            }
            b2WorldCreator.getInteractiveTileObjects().removeAll(drawableInteractiveObjectToRemove, false);
        /*if (startRolling) {
            spinButton.setLocked(true);
            startRolling(dt);
        } else if (winOneArmedBandit) {
            winOneArmedBandit(dt);
        } else if (loseOneArmedBandit) {
            loseOneArmedBanditEndTime += dt;
            if (loseOneArmedBanditEndTime > 0.5f) {
                removeOneArmedBanditbundle();
                loseOneArmedBanditEndTime = 0;
                loseOneArmedBandit = false;
            }
        }*/
            Array<Spark> sparksToRemove = new Array<>();
            for (int i = 0; i < sparks.size; i++) {
                Spark spark = sparks.get(i);
                spark.update(dt);
                if (spark.isRemoveSpark()) {
                    sparksToRemove.add(spark);
                }
            }
            for (Spark spark : player.getSparks()) {
                spark.update(dt);
                if (spark.isRemoveSpark()) {
                    sparksToRemove.add(spark);
                }
            }
            sparks.removeAll(sparksToRemove, false);
            player.getSparks().removeAll(sparksToRemove, false);

            player.update(dt);


            MapProperties mapProp = map.getProperties();
            hud.update(dt, player.b2body.getPosition().y, mapProp.get("height", Integer.class) * 32);
            Vector2 playerPos = new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);

            for (Rock rock : rocks) {
                if (defaultWindows.size > 0) {
                    if (rock.getB2body().getPosition().y < player.b2body.getPosition().y + 1500 / FallingMan.PPM && rock.getB2body().getLinearVelocity().y < 5) {
                        rock.getB2body().applyLinearImpulse(new Vector2(0, 3000), rock.getB2body().getWorldCenter(), true);
                    }
                    rock.update(dt, playerPos, true, false);
                } else if (tutorialOn && playScreenTutorialHandler.isTutorialOn()) {
                    rock.update(dt, playerPos, true, false);
                } else {
                    rock.update(dt, playerPos, false, false);
                }
            }

            for (DefaultWindow defaultWindow : defaultWindows) {
                defaultWindow.update(dt, hud, player.b2body.getPosition());
            }

            for (B2SteeringEntityContainer b2SteeringEntityContainer : b2SteeringEntityContainers) {
                if (player.isHunted()) {
                    b2SteeringEntityContainer.getEntity().update(dt);
                    //b2SteeringEntityContainer.getEntity().getBody().setGravityScale(0);
                } else {
                    b2SteeringEntityContainer.getEntity().getBody().setLinearVelocity(0, 0);
                }
            }
            Array<HuntingSpider> huntingSpidersToRemove = new Array<>();
            Array<B2SteeringEntityContainer> b2SteeringEntityContainersToRemove = new Array<>();
            for (HuntingSpider huntingSpider : huntingSpiders) {
                huntingSpider.update(dt);
                if (huntingSpider.isToRemove()) {
                    huntingSpidersToRemove.add(huntingSpider);
                    for (B2SteeringEntityContainer b2SteeringEntityContainer : b2SteeringEntityContainers) {
                        if (b2SteeringEntityContainer.getEntity().getBody().equals(huntingSpider.getBody())) {
                            b2SteeringEntityContainersToRemove.add(b2SteeringEntityContainer);
                        }
                    }
                    world.destroyBody(huntingSpider.getBody());
                }
            }
            b2SteeringEntityContainers.removeAll(b2SteeringEntityContainersToRemove, false);
            huntingSpiders.removeAll(huntingSpidersToRemove, false);

            //oneArmBandit.update(dt);
        /*for (PlayerBodyPart playerBodyPart : player.getBodyParts()) {
            if (playerBodyPart.getBodyPartName().equals("handL")) {
                lHandLight.setPosition(playerBodyPart.getB2body().getPosition().x, playerBodyPart.getB2body().getPosition().y);

            } else if (playerBodyPart.getBodyPartName().equals("handR")) {
                //rHandLight.setIgnoreAttachedBody();
                rHandLight.setPosition(playerBodyPart.getB2body().getPosition().x, playerBodyPart.getB2body().getPosition().y);

            }
        }*/



            if (gameOver) {
                if (defaultWindows.size == 0) {
                    goldFromPreviousLife = hud.getGold();
                    distFromPreviousLife = hud.getWholeDistance();
                    defaultWindows.add(new DefaultWindow(this, world, FallingMan.GAME_OVER_WINDOW));
                    player.createHeadJoint();
                /*for (Body bodyPart : player.getBodyPartsAll()) {
                    //world.destroyBody(player.b2body);
                    world.destroyBody(bodyPart);
                }*/
                /*for (Rock rock : rocks) {
                    Filter filter = new Filter();

                    filter.categoryBits = FallingMan.ROCK_BIT;
                    filter.maskBits = FallingMan.ROCK_BIT | FallingMan.DEFAULT_BIT;
                    rock.getFixture().setFilterData(filter);
                }*/
                    hud.newStageGameOver();
                    //buttons.add(new PlayAgainButton(this, world, 224 / FallingMan.PPM, player.b2body.getPosition().y - 850 / FallingMan.PPM, 992 / FallingMan.PPM, 480 / FallingMan.PPM));
                    //buttons.add(new MenuButton(this, world, 224 / FallingMan.PPM, player.b2body.getPosition().y - 370 / FallingMan.PPM, 992 / FallingMan.PPM, 480 / FallingMan.PPM));
                }
                gameOver = false;
                //dispose();
                //game.setScreen(new PlayScreen(game));
            }
            if (newLife) {
                defaultWindows = new Array<>();
                player.setRemoveHeadJoint(true);
                hud.getStage().dispose();
                hud = new HUD(game.batch, this);
                hud.setGold(goldFromPreviousLife);
                hud.setWholeDistance(distFromPreviousLife);
                Array<Button> buttonsToRemove = new Array<>();
                for (Button button : buttons) {
                    if (button instanceof WatchAdButton || button instanceof ShowLeaderboardButton) {
                        buttonsToRemove.add(button);
                    }
                }
                float posX = player.getBelly().getB2body().getPosition().x;
                float posY = player.getBelly().getB2body().getPosition().y;
                for (int i = 0; i < 50; i++) {
                    player.getSparks().add(new Spark(this, posX, posY, (byte) 2));
                }
                buttons.removeAll(buttonsToRemove, false);
                newLife = false;
            }
        if (tutorialOn) {
            playScreenTutorialHandler.update(dt, player.b2body.getPosition().y, gamePort.getWorldHeight());
        }

        Gdx.app.log("FPS: ", String.valueOf(Gdx.graphics.getFramesPerSecond()));
    }

    @Override
    public void render(float delta) {
        update(delta);

        //clear screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render map
        sunPos += FallingMan.SUN_SPEED;
        gameCam.position.y = sunPos;
        //gameCam.position.y = 16.9f;
        gameCam.update();
        prepareDayAndNightCycle();

        rendererBehind0.setView(gameCam);
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
        renderer.render();


        game.batch.setProjectionMatrix(gameCam.combined);
        rayHandler.setCombinedMatrix(gameCam);
        game.batch.begin();
        for (Spark spark : sparks) {
            spark.draw(game.batch);
        }
        for (InteractiveObjectInterface drawableInteractiveObject : b2WorldCreator.getInteractiveTileObjects()) {
            drawableInteractiveObject.draw(game.batch);
        }
        player.draw(game.batch);
        for (PlayerBodyPart bodyPart : player.getBodyParts()) {
            bodyPart.draw(game.batch);
        }
        for (Spark spark : player.getSparks()) {
            spark.draw(game.batch);
        }

        for (HuntingSpider huntingSpider : huntingSpiders) {
            huntingSpider.draw(game.batch);
        }

        for (Rock rock : rocks) {
            rock.draw(game.batch);
        }

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(238 / 256f, 188 / 256f, 29 / 256f, 1);
        font.getData().setScale(0.007f);
        for (FontMapObject fontMapObject : fontMapObjects) {
            GlyphLayout glyphLayout = new GlyphLayout(font, fontMapObject.getText());
            font.setColor(fontMapObject.getColor());
            font.draw(game.batch, fontMapObject.getText(), fontMapObject.getPosX() - glyphLayout.width / 2, fontMapObject.getPosY() + glyphLayout.height * 1.6f);
        }
        if (tutorialOn) {
            playScreenTutorialHandler.draw(game.batch);
        }

//        font.draw(game.batch, "dziala dziala dziala dziala dziala dziala dziala dziala dziala dziala", 0, 8000 / FallingMan.PPM);
//        font.draw(game.batch, "2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 ", 0, 8100 / FallingMan.PPM);

        game.batch.end();

        if (turnOnLights) {
            rayHandler.updateAndRender();
        }

        game.batch.begin();

        for (DefaultWindow window : defaultWindows) {
            window.draw(game.batch);
        }
        for (Button button : buttons) {
            button.draw(game.batch);
        }
        game.batch.end();

        //render box2d debug renderer
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.draw();

        switch (currentScreen) {
            /*case FallingMan.PLAY_SCREEN:
                dispose();
                game.setScreen(new PlayScreen(game, new PlayerVectors(player, false), null, null));
                break;*/
            case FallingMan.MENU_SCREEN:
                saveData.addGold(hud.getGold());
                saveData.setHighScore(hud.getWholeDistance());
                dispose();
                FallingMan.gameScreen = new MenuScreen(game, new Array<Vector2>(), gamePort.getWorldHeight(), gameCamBehindPositionBack, gameCamBehindPositionFront, sunPos, rendererBehind0.getBatch().getColor(), false);
                FallingMan.currentScreen = FallingMan.MENU_SCREEN;
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
        rayHandler.dispose();
        hud.getStage().dispose();
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

    public void generateNewMap() {

        dontStopAtNotStraightWalls = false;

        //generating new map
        String mapName = "untitled" + new Random().nextInt(3) + ".tmx";
        //String mapName = "untitled2.tmx";
        for (Body body : b2WorldCreator.getB2bodies()) {
            world.destroyBody(body);
        }

        map.dispose();
        map = mapLoader.load(mapName);
        b2WorldCreator = new B2WorldCreator(this, world, map);
        renderer.dispose();
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);

        //transforming player position to new map
        Vector2 playerPosPrevious = new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);
        MapProperties mapProp = map.getProperties();
        player.updateBodyParts(mapProp.get("height", Integer.class) * 32, false);

        for (Rock rock : rocks) {
            rock.generateMapRockUpdate(playerPosPrevious, mapProp.get("height", Integer.class) * 32);
        }
        for (HuntingSpider huntingSpider : huntingSpiders) {
            huntingSpider.generateMapSpiderUpdate(playerPosPrevious, mapProp.get("height", Integer.class) * 32);
        }
        hud.setWholeDistance(hud.getWholeDistance() + 1);

        GsClientUtils.distanceAchievementUnlocker(game.gsClient, hud.getWholeDistance());
        fontMapObjects = new ArrayList<>();

    }

    /*public void startRolling(float dt) {
        Random random = new Random();
        for (int i = 0; i < rolls.size; i++) {
            Roll roll = rolls.get(i);
            if (roll.isRollingCurrently()) {
                if (roll.getY() > player.b2body.getPosition().y - 1424 / FallingMan.PPM) {

                    roll.setPosition(roll.getX(), roll.getY() - (random.nextInt(20) + 40) / FallingMan.PPM);
                } else {

                    if (roll.getCurrentTextureNumber() == 3) {
                        roll.setCurrentTextureNumber(0);
                        roll.setNewRegion(roll.getCurrentTextureNumber());
                        roll.setPosition(roll.getPosX(), roll.getPosY());
                    } else {
                        roll.setCurrentTextureNumber(roll.getCurrentTextureNumber() + 1);
                        roll.setNewRegion(roll.getCurrentTextureNumber());
                        roll.setPosition(roll.getPosX(), roll.getPosY());
                    }
                    if (rollingTime > 2 + i * 0.5f) {
                        if (i == 2) {
                            int figureNumber = rolls.get(0).getCurrentTextureNumber();
                            boolean allFiguresTheSame = true;
                            for (Roll rollFigureCheck : rolls) {
                                if (figureNumber != rollFigureCheck.getCurrentTextureNumber()) {
                                    allFiguresTheSame = false;
                                }
                            }
                            if (allFiguresTheSame) {
                                removeButton(spinButton);
                                winOneArmedBandit = true;
                                smallRolls.add(new OnePartRoll(this, 300 / FallingMan.PPM, player.b2body.getPosition().y - 1040 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                smallRolls.add(new OnePartRoll(this, 620 / FallingMan.PPM, player.b2body.getPosition().y - 1040 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                smallRolls.add(new OnePartRoll(this, 950 / FallingMan.PPM, player.b2body.getPosition().y - 1040 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                rolls = new Array<>();
                                hud.setGold(hud.getGold() + (long) new Random().nextInt(4000) + 8000);
                            } else {
                                spinButton.setLocked(false);
                                if (--numberOfSpins <= 0) {
                                    loseOneArmedBandit = true;
                                    removeButton(spinButton);
                                }
                            }
                            rollingTime = 0;
                            startRolling = false;


                        }
                        roll.setRollingCurrently(false);
                    }
                }
            }
        }
        rollingTime += dt;
    }

    public void winOneArmedBandit(float dt) {
        for (OnePartRoll smallRoll : smallRolls) {
            if (winOneArmedBanditTime > 10) {
                removeOneArmedBanditbundle();
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
                winOneArmedBanditTime += dt;
            }
        }
    }*/

    public void removeOneArmedBanditbundle() {
        winOneArmedBanditTime = 0;
        oneArmBandits = new Array<>();
        player.setRemoveHeadJoint(true);
        smallRolls = new Array<>();
        rolls = new Array<>();
        stopRock = false;
        startRolling = false;
    }

    public void createOneArmedBanditObjects() {
        oneArmBandits = new Array<>();
        oneArmBandits.add(new OneArmBandit(this, world, player.b2body.getPosition().y - 1464 / FallingMan.PPM));
        buttons = new Array<>();
        spinButton = new SpinButton(this, world, 448 / FallingMan.PPM, player.b2body.getPosition().y - 704 / FallingMan.PPM, 544 / FallingMan.PPM, 192 / FallingMan.PPM);
        buttons.add(spinButton);
        startRolling = false;
        rollingTime = 0;
        Roll roll1 = new Roll(this, world, 300 / FallingMan.PPM, player.b2body.getPosition().y - 1040 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM);
        Roll roll2 = new Roll(this, world, 620 / FallingMan.PPM, player.b2body.getPosition().y - 1040 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM);
        Roll roll3 = new Roll(this, world, 950 / FallingMan.PPM, player.b2body.getPosition().y - 1040 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM);
        rolls = new Array<>();
        rolls.add(roll1);
        rolls.add(roll2);
        rolls.add(roll3);
        winOneArmedBandit = false;
        winOneArmedBanditTime = 0;
        winOneArmedBanditScaleUp = true;

        smallRolls = new Array<>();
        player.createHeadJoint();
    }

    public B2WorldCreator getB2WorldCreator() {
        return b2WorldCreator;
    }

    public void removeButton(Button button) {
        buttons.removeValue(button, false);
    }

    public void setStartRolling(boolean startRolling) {
        this.startRolling = startRolling;
    }

    public Player getPlayer() {
        return player;
    }

    public void setStopRock(boolean stopRock) {
        this.stopRock = stopRock;
    }

    @Override
    public GoldAndHighScoresBackground getGoldAndHighScoresBackground() {
        return null;
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
    public void addOnePartRolls(int numberOfOnePartRolls, int typeOfRoll, Vector2 pos, String transactionName) {

    }

    @Override
    public void removeChest(BigChest bigChest) {

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
        return getGoldAndHighScoresIcons();
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
        if (game.getAdsController() != null) {
            game.getAdsController().showRewardedVideo(false);
            for (DefaultWindow window : defaultWindows) {
                if (window.isWatchAdToGetSecondLifeReady()) {
                    //...
                }
            }
            watchAdButtonClicked = true;
        }
    }

    public HUD getHud() {
        return hud;
    }

    public void setNumberOfSpins(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Array<DefaultWindow> getDefaultWindows() {
        return defaultWindows;
    }

    public World getWorld() {
        return world;
    }

    public FallingMan getGame() {
        return game;
    }

    public void setLoadNewGame(boolean loadNewGame) {
        this.loadNewGame = loadNewGame;
    }

    public void setLoadMenu(boolean loadMenu) {
        this.loadMenu = loadMenu;
    }

    public Array<Button> getButtons() {
        return buttons;
    }

    @Override
    public void setNewLife(boolean newLife) {
        this.newLife = newLife;
    }

    public ArrayList<FontMapObject> getFontMapObjects() {
        return fontMapObjects;
    }

    public Array<Spark> getSparks() {
        return sparks;
    }

    public SaveData getSaveData() {
        return saveData;
    }

    public TextureAtlas getWindowAtlas() {
        return windowAtlas;
    }

    public TextureAtlas getBigRockAtlas() {
        return bigRockAtlas;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getMapHeight() {
        MapProperties mapProp = map.getProperties();
        return mapProp.get("height", Integer.class) * 32;
    }

    public Array<HuntingSpider> getHuntingSpiders() {
        return huntingSpiders;
    }

    public void addHuntingSpiderAI(Vector2 bodyPos) {
        HuntingSpider huntingSpider = new HuntingSpider(world, this, bodyPos.x, bodyPos.y);
        huntingSpiders.add(huntingSpider);


        B2dSteeringEntity entity = new B2dSteeringEntity(huntingSpider.getBody(), 0.1f);

        B2dSteeringEntity target = new B2dSteeringEntity(player.b2body, 0.1f);

        b2SteeringEntityContainers.add(new B2SteeringEntityContainer(entity, target));

        Arrive<Vector2> arriveSB = new Arrive<Vector2>(entity, target)
                .setArrivalTolerance(1f)
                .setDecelerationRadius(1);
        entity.setBehavior(arriveSB);
    }

    public boolean isNewLife() {
        return newLife;
    }

    private void prepareDayAndNightCycle() {
        if (sunPos > 54 && sunPos < 62) {
            rendererBehind0.getBatch().setColor(rendererBehind0.getBatch().getColor().r, (62 - sunPos) / 9 + 0.11111f, (62 - sunPos) / 16 + 0.5f, 1);
            if (sunPos > 59 && sunPos < 62) {
                rendererBehind0.getBatch().setColor((62 - sunPos) / 3.375f + 0.11111f, rendererBehind0.getBatch().getColor().g, rendererBehind0.getBatch().getColor().b, 1);
            }
            rendererBehind1.getBatch().setColor(rendererBehind0.getBatch().getColor());
            rayHandler.setAmbientLight((62 - sunPos) / 8 / 1.5f + 0.3333f);
            if (sunPos > 58) {
                turnOnLights = true;
            } else {
                turnOnLights = false;
            }
        } else if (sunPos > 16.8 && sunPos < 24.8) {
            rendererBehind0.getBatch().setColor(rendererBehind0.getBatch().getColor().r, 1 - ((24.8f - sunPos) / 9) + 0.11111f, 1 - ((24.8f - sunPos) / 16) + 0.5f, 1);
            if (sunPos > 16.8 && sunPos < 19.8) {
                rendererBehind0.getBatch().setColor(1 - ((19.8f - sunPos) / 3.375f) + 0.11111f, rendererBehind0.getBatch().getColor().g, rendererBehind0.getBatch().getColor().b, 1);
            }
            rendererBehind1.getBatch().setColor(rendererBehind0.getBatch().getColor());
            rayHandler.setAmbientLight(((sunPos - 16.8f) / 8 / 1.5f) + 0.3333f);
            if (sunPos > 20.8f) {
                turnOnLights = false;
            } else {
                turnOnLights = true;
            }
        } else if (sunPos <= 16.8 || sunPos >= 62) {
            rendererBehind0.getBatch().setColor(0.11111f, 0.11111f, 0.5f, 1);
            rendererBehind1.getBatch().setColor(rendererBehind0.getBatch().getColor());
            rayHandler.setAmbientLight(0.3333f);
            turnOnLights = true;
        } else {
            rayHandler.setAmbientLight(1);
            turnOnLights = false;
        }
        if (sunPos > 99) {
            sunPos = FallingMan.MAX_WORLD_HEIGHT / 2f / FallingMan.PPM;
        }
    }

}
