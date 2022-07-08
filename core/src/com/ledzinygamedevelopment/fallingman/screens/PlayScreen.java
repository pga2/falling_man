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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.scenes.HUD;
import com.ledzinygamedevelopment.fallingman.sprites.windows.DefaultWindow;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.huntingspider.HuntingSpider;
import com.ledzinygamedevelopment.fallingman.sprites.fallingobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.font.FontMapObject;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coin.Spark;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OneArmBandit;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.Roll;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.tools.B2WorldCreator;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.PlayerVectors;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;
import com.ledzinygamedevelopment.fallingman.tools.WorldContactListener;
import com.ledzinygamedevelopment.fallingman.tools.entities.B2SteeringEntityContainer;
import com.ledzinygamedevelopment.fallingman.tools.entities.B2dSteeringEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class PlayScreen implements GameScreen {

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
    private Viewport gamePort;

    //map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

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

    public PlayScreen(FallingMan game, PlayerVectors playerVectors, Vector3 rockPos, float rockAnimationTimer) {
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
        currentScreen = FallingMan.CURRENT_SCREEN;
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("untitled2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);
        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2) / FallingMan.PPM, 0);

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
        rock.getB2body().setTransform(rockPos.x, rockPos.y + player.b2body.getPosition().y, rockPos.z);
        rock.setAnimationTimer(rockAnimationTimer);
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
        //gameCam.zoom = 0.3f;

        //player.createHeadJoint();
        //player.b2body.applyLinearImpulse(new Vector2(100f, 0f), player.b2body.getWorldCenter(), true);
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

                // check if buttons click
                Vector2 mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                for (Button button : buttons) {
                    if (button.mouseOver(mouseVector) && !button.isLocked()) {
                        button.touched();
                        button.setClicked(true);
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
            for (DefaultWindow defaultWindow : defaultWindows) {
                if (defaultWindow.getTypeOfWindowText() == FallingMan.GAME_OVER_WINDOW && Gdx.input.isTouched() && defaultWindow.isTapExist()) {
                    currentScreen = FallingMan.MENU_SCREEN;
                }
            }
        }
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 8, 5);

        if (player.b2body.getPosition().y < (FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM + 30 / FallingMan.PPM) {
            generateNewMap();
        }

        Array<InteractiveObjectInterface> drawableInteractiveObjectToRemove = new Array<>();
        for (InteractiveObjectInterface interactiveTileObject : b2WorldCreator.getInteractiveTileObjects()) {
            interactiveTileObject.update(dt);
            if (interactiveTileObject.isTouched()) {
                interactiveTileObject.touched();
                interactiveTileObject.setTouched(false);
                if (interactiveTileObject.isToRemove()) {
                    drawableInteractiveObjectToRemove.add(interactiveTileObject);
                }
            }
        }
        b2WorldCreator.getInteractiveTileObjects().removeAll(drawableInteractiveObjectToRemove, false);
        if (startRolling) {
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
        }

        for (int i = 0; i < sparks.size; i++) {
            Spark spark = sparks.get(i);
            spark.update(dt);
            if (spark.isRemoveSpark()) {
                sparks.removeIndex(i);
            }
        }
        player.update(dt);

        MapProperties mapProp = map.getProperties();
        hud.update(dt, player.b2body.getPosition().y, mapProp.get("height", Integer.class) * 32);
        Vector2 playerPos = new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);

        for (Rock rock : rocks) {
            rock.update(dt, playerPos, stopRock, false);
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
        gameCam.position.y = player.b2body.getPosition().y;

        gameCam.update();
        renderer.setView(gameCam);

        if (gameOver) {
            if (defaultWindows.size == 0) {
                saveData.addGold(hud.getGold());
                saveData.setHighScore(hud.getWholeDistance());
                defaultWindows.add(new DefaultWindow(this, world, FallingMan.GAME_OVER_WINDOW));
                player.createHeadJoint();
                /*for (Body bodyPart : player.getBodyPartsAll()) {
                    //world.destroyBody(player.b2body);
                    world.destroyBody(bodyPart);
                }*/
                for (Rock rock : rocks) {
                    Filter filter = new Filter();

                    filter.categoryBits = FallingMan.ROCK_BIT;
                    filter.maskBits = FallingMan.ROCK_BIT | FallingMan.DEFAULT_BIT;
                    rock.getFixture().setFilterData(filter);
                }
                hud.newStageGameOver();
                //buttons.add(new PlayAgainButton(this, world, 224 / FallingMan.PPM, player.b2body.getPosition().y - 850 / FallingMan.PPM, 992 / FallingMan.PPM, 480 / FallingMan.PPM));
                //buttons.add(new MenuButton(this, world, 224 / FallingMan.PPM, player.b2body.getPosition().y - 370 / FallingMan.PPM, 992 / FallingMan.PPM, 480 / FallingMan.PPM));
            }
            gameOver = false;
            //dispose();
            //game.setScreen(new PlayScreen(game));
        }

    }

    @Override
    public void render(float delta) {
        update(delta);

        //clear screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render map
        renderer.render();


        game.batch.setProjectionMatrix(gameCam.combined);
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

        for (HuntingSpider huntingSpider : huntingSpiders) {
            huntingSpider.draw(game.batch);
        }

        for (Rock rock : rocks) {
            rock.draw(game.batch);
        }

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(238/256f, 188/256f, 29/256f, 1);
        font.getData().setScale(0.007f);
        for (FontMapObject fontMapObject : fontMapObjects) {
            GlyphLayout glyphLayout = new GlyphLayout(font, fontMapObject.getText());
            font.setColor(new Color(174/255f, 132/255f, 26/255f, 1));
            font.draw(game.batch, fontMapObject.getText(), fontMapObject.getPosX() - glyphLayout.width / 2, fontMapObject.getPosY() + glyphLayout.height * 1.6f);
        }
        for (DefaultWindow window : defaultWindows) {
            window.draw(game.batch);
        }
        for (Button button : buttons) {
            button.draw(game.batch);
        }

//        font.draw(game.batch, "dziala dziala dziala dziala dziala dziala dziala dziala dziala dziala", 0, 8000 / FallingMan.PPM);
//        font.draw(game.batch, "2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 2115 ", 0, 8100 / FallingMan.PPM);

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
                dispose();
                game.setScreen(new MenuScreen(game, new Array<Vector2>(), gamePort.getWorldHeight()));
                break;
        }
        /*Gdx.app.log("FPS: ", String.valueOf(1 / delta));
        allFPSData.add(1 / delta);
        Long allFps = 0l;
        for(Float integer : allFPSData) {
            allFps += integer.longValue();
        }
        Gdx.app.log("average FPS", String.valueOf(allFps / allFPSData.size()));*/

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
        hud.getStage().dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        assetManager.getManager().dispose();
    }

    public void generateNewMap() {

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

        player.updateBodyParts(mapProp.get("height", Integer.class) * 32);

        for (Rock rock : rocks) {
            rock.generateMapRockUpdate(playerPosPrevious, mapProp.get("height", Integer.class) * 32);
        }
        for (HuntingSpider huntingSpider : huntingSpiders) {
            huntingSpider.generateMapSpiderUpdate(playerPosPrevious, mapProp.get("height", Integer.class) * 32);
        }
        hud.setWholeDistance(hud.getWholeDistance() + 1);
        fontMapObjects = new ArrayList<>();
    }

    public void startRolling(float dt) {
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
    }

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
    public void addCoinsFromChest(int numberOfCoins) {

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

    public HUD getHud() {
        return hud;
    }

    public void setNumberOfSpins(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

    public void setGameOver(boolean gameOver) {
        /*this.gameOver = gameOver;*/
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
}
