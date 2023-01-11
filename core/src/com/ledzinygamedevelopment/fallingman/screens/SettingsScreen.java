package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.sprites.Smoke;
import com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects.Cloud;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.fallingobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.MicroPaymentButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.ad.WatchAdButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.menu.ReturnMenuButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.settings.MusicButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.settings.RateUsButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.settings.SoundsButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.settings.TutorialButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.settings.VibrationsButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.tools.AdsController;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

import java.util.HashMap;
import java.util.Random;

public class SettingsScreen implements GameScreen{

    private final GameAssetManager assetManager;
    private int mapHeight;
    private BitmapFont font;
    private byte currentScreen;
    private Array<Rock> rocks;
    private OrthographicCamera gameCam;
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
    private TextureAtlas defaultAtlas;
    private FallingMan game;
    private Array<Body> worldBodies;
    private SaveData saveData;
    private Vector2 previusFrameTouchPos;
    private Vector2 startTouchingPos;
    private Vector2 lastTouchPos;
    private Array<Button> buttons;
    private boolean generateButtons;
    private boolean firstTouch;
    private GoldAndHighScoresIcons goldAndHighScoresIcons;
    private GoldAndHighScoresBackground goldAndHighScoresBackground;
    private Array<OnePartRoll> flyingRolls;
    private Array<Cloud> clouds;
    private Array<Vector2> cloudsPositionForNextScreen;
    private boolean changeScreen;
    private float gameCamBehindPositionBack;
    private float sunPos;
    private float gameCamBehindPositionFront;
    private Sprite background;

    public SettingsScreen(FallingMan game, Array<Vector2> cloudsPositionForNextScreen, float screenHeight, float gameCamBehindPositionBack, float gameCamBehindPositionFront, float sunPos, Color rendererColor) {
        this.game = game;
        currentScreen = FallingMan.CURRENT_SCREEN;

        assetManager = new GameAssetManager();
        assetManager.loadSettingsScreen();
        assetManager.finishLoading();
        //defaultAtlas = assetManager.getManager().get(assetManager.getMenuScreenDefault());
        defaultAtlas = assetManager.getManager().get(assetManager.getSettingsDefault());
        map = assetManager.getManager().get(assetManager.getInAppPurchasesMap());
        font = assetManager.getManager().get(assetManager.getFont());
        background = new Sprite();
        background.setRegion((Texture) assetManager.getManager().get(assetManager.getBackgroundImage()));
        background.setBounds(0, 0, 1440 / FallingMan.PPM, 3360 / FallingMan.PPM);
        background.setPosition(0, 0);
        saveData = new SaveData();
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);

        mapLoader = new TmxMapLoader();
        //map = mapLoader.load("menu_map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);
        mapBehind0 = mapLoader.load("maps/menu_map_behind.tmx");
        mapBehind1 = mapLoader.load("maps/menu_map_behind.tmx");
        rendererBehind0 = new OrthogonalTiledMapRenderer(mapBehind0, 1 / FallingMan.PPM);
        rendererBehind1 = new OrthogonalTiledMapRenderer(mapBehind1, 1 / FallingMan.PPM);
        gameCam.update();
        renderer.setView(gameCam);

        world = new World(new Vector2(0, -3f), true);
        b2dr = new Box2DDebugRenderer();

        worldBodies = new Array<>();


        clouds = new Array<>();
        MapProperties mapProp = map.getProperties();
        mapHeight = mapProp.get("height", Integer.class);
        for (Vector2 pos : cloudsPositionForNextScreen) {
            Cloud cloud = new Cloud(this, 0, 0, true, FallingMan.SHOP_SCREEN);
            cloud.setPosition(pos.x, pos.y);
            clouds.add(cloud);
        }

        this.cloudsPositionForNextScreen = new Array<>();

        goldAndHighScoresBackground = new GoldAndHighScoresBackground(this, world);
        goldAndHighScoresIcons = new GoldAndHighScoresIcons(this, world, saveData.getGold(), saveData.getHighScore());

        buttons = new Array<>();
        generateButtons = true;
        firstTouch = false;
        flyingRolls = new Array<>();
        this.sunPos = sunPos;
        rendererBehind0.getBatch().setColor(rendererColor);
        rendererBehind1.getBatch().setColor(rendererColor);

    }


    @Override
    public void show() {

    }

    private void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            if (!firstTouch) {
                startTouchingPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                firstTouch = true;
                lastTouchPos = null;
            }
            if (clouds.size == 0) {
                for (Button button : buttons) {
                    if (button.mouseOver(gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())))) {
                        button.touched();
                    } else if (button.isClicked()) {
                        button.setClicked(false);
                        button.restoreNotClickedTexture();
                    }
                }
            }
            lastTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            //menuScreen
            if (flyingRolls.size == 0) {
                if (lastTouchPos.y - startTouchingPos.y > 200 / FallingMan.PPM) {
                    if (!changeScreen) {
                        Random random = new Random();
                        //currentScreen = FallingMan.MENU_SCREEN;
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 26; j++) {
                                clouds.add(new Cloud(this, ((i * 640) - random.nextInt(220)) / FallingMan.PPM, ((-150 * j) - random.nextInt(21)) / FallingMan.PPM, false, FallingMan.IN_APP_PURCHASES_SCREEN));
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
        } else {
            if (firstTouch) {
                if (clouds.size == 0) {
                    for (Button button : buttons) {
                        if (button.mouseOver(gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())))) {
                            button.notTouched();
                        }
                    }
                }
                firstTouch = false;
            }
        }

    }

    private void update(float dt) {
        handleInput(dt);
        world.step(dt, 8, 5);

        if (gamePort.getWorldHeight() / gamePort.getWorldWidth() > 1.8888f) {
            goldAndHighScoresBackground.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), gamePort.getWorldHeight());
            goldAndHighScoresIcons.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), gamePort.getWorldHeight());
        } else {
            goldAndHighScoresBackground.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM + 150 / FallingMan.PPM);
            goldAndHighScoresIcons.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM + 150 / FallingMan.PPM);

        }

        for (Button button : buttons) {
            //button.update(dt, new Vector2(96 / FallingMan.PPM, goldAndHighScoresIcons.getY() - 96 / FallingMan.PPM));
        }

        /*Array<OnePartRoll> flyingRollsToRemove = new Array<>();
        for (OnePartRoll flyingRoll : flyingRolls) {
            flyingRoll.flyingRollUpdate(dt, new Vector2(goldAndHighScoresIcons.getX() + goldAndHighScoresIcons.getWidth() / 2, goldAndHighScoresIcons.getY() + goldAndHighScoresIcons.getHeight() / 4), saveData);
            if (flyingRoll.isToRemove()) {
                if (flyingRolls.size - flyingRollsToRemove.size == 100) {
                    if (flyingRoll.getCurrentTextureNumber() == 2) {
                        saveData.addGold(flyingRoll.getAmount());
                        goldAndHighScoresIcons.setGold(saveData.getGold());
                    } else if (flyingRoll.getCurrentTextureNumber() == 0) {
                        saveData.addSpins((int) flyingRoll.getAmount());
                    } else {
                        throw new NullPointerException("flying roll had incorrect amount: " + flyingRoll.getAmount());
                    }
                }
                flyingRollsToRemove.add(flyingRoll);
            }
        }
        flyingRolls.removeAll(flyingRollsToRemove, false);*/

        Array<Cloud> cloudsToRemove = new Array<>();
        outerloop:
        for (Cloud cloud : clouds) {
            if (!cloud.isSecondScreen()) {
                if (cloud.getY() > FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM) {
                    for (Cloud cloudGetPos : clouds) {
                        if (!cloudGetPos.isSecondScreen()) {
                            cloudsPositionForNextScreen.add(new Vector2(cloudGetPos.getX(), cloudGetPos.getY()));
                        }
                    }
                    if (cloud.getScreen() == FallingMan.IN_APP_PURCHASES_SCREEN)
                        currentScreen = FallingMan.IN_APP_PURCHASES_SCREEN;
                    else if (cloud.getScreen() == FallingMan.MENU_SCREEN)
                        currentScreen = FallingMan.MENU_SCREEN;
                    break outerloop;
                }
                cloud.update(dt, 0, 1.2f * 60 * dt);
            } else if (cloud.getY() < -FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM) {
                cloudsToRemove.add(cloud);
            } else {
                if (dt < 0.01666) {
                    cloud.update(dt, 0, -1.2f * 60 * dt);
                } else {
                    cloud.update(dt, 0, -1.2f);
                }
            }
        }
        clouds.removeAll(cloudsToRemove, false);

        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2f) / FallingMan.PPM, (gamePort.getWorldHeight() * FallingMan.PPM / 2) / FallingMan.PPM, 0);
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
        /*sunPos += FallingMan.SUN_SPEED * 60 * dt;
        gameCam.position.y = sunPos;
        //gameCam.position.y = 16.9f;
        gameCam.update();
        prepareDayAndNightCycle();
        rendererBehind0.setView(gameCam);
        rendererBehind0.render(new int[]{0, 1});
        Random random = new Random();
        gameCamBehindPositionBack -= random.nextFloat() / 200 + 0.005;
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

        gameCamBehindPositionFront -= random.nextFloat() / 100 + 0.01;
        if (gameCamBehindPositionFront < -(mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM / 2) {
            gameCamBehindPositionFront += (mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM;
        }

        //render box2d debug renderer
        //b2dr.render(world, gameCam.combined);
        gameCam.position.y = gameCamBehindPositionFront;
        gameCam.update();
        rendererBehind0.setView(gameCam);
        rendererBehind0.render(new int[]{3});
        gameCam.position.y = gameCamBehindPositionFront + (mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM;
        gameCam.update();
        rendererBehind1.setView(gameCam);
        rendererBehind1.render(new int[]{3});*/

        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2f) / FallingMan.PPM, (gamePort.getWorldHeight() * FallingMan.PPM / 2) / FallingMan.PPM, 0);
        gameCam.update();
        //renderer.render();


        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();
        background.draw(game.batch);

        goldAndHighScoresBackground.draw(game.batch);
        goldAndHighScoresIcons.draw(game.batch);

        for (Button button : buttons) {
            button.draw(game.batch);
        }

        for (OnePartRoll flyingRoll : flyingRolls) {
            flyingRoll.draw(game.batch);
        }

        for (Cloud cloud : clouds) {
            cloud.draw(game.batch);
        }

        game.batch.end();

        if (generateButtons) {
            generateButtons();
            generateButtons = false;
        }

        switch (currentScreen) {
            case FallingMan.IN_APP_PURCHASES_SCREEN:
                dispose();
                FallingMan.gameScreen = new InAppPurchasesScreen(game, cloudsPositionForNextScreen, gamePort.getWorldHeight(), true, gameCamBehindPositionBack, gameCamBehindPositionFront, sunPos, rendererBehind0.getBatch().getColor());
                FallingMan.currentScreen = FallingMan.IN_APP_PURCHASES_SCREEN;
                game.setScreen(FallingMan.gameScreen);
                break;
            case FallingMan.MENU_SCREEN:
                dispose();
                FallingMan.gameScreen = new MenuScreen(game, cloudsPositionForNextScreen, gamePort.getWorldHeight(), gameCamBehindPositionBack, gameCamBehindPositionFront, sunPos, rendererBehind0.getBatch().getColor(), false);
                FallingMan.currentScreen = FallingMan.MENU_SCREEN;
                game.setScreen(FallingMan.gameScreen);
                break;
        }
    }

    private void generateButtons() {
        buttons.add(new ReturnMenuButton(this, world, (83 / FallingMan.PPM) * 4 + (256 / FallingMan.PPM) * 3, goldAndHighScoresBackground.getY() - 15 / FallingMan.PPM));
        buttons.add(new SoundsButton(this, world, 72 / FallingMan.PPM, goldAndHighScoresIcons.getY() - 72 / FallingMan.PPM - 384 / FallingMan.PPM, 384 / FallingMan.PPM, 384 / FallingMan.PPM));
        buttons.add(new MusicButton(this, world, 72 / FallingMan.PPM * 2 + 384 / FallingMan.PPM, goldAndHighScoresIcons.getY() - 72 / FallingMan.PPM - 384 / FallingMan.PPM, 384 / FallingMan.PPM, 384 / FallingMan.PPM));
        buttons.add(new VibrationsButton(this, world, 72 / FallingMan.PPM * 3 + 384 / FallingMan.PPM * 2, goldAndHighScoresIcons.getY() - 72 / FallingMan.PPM - 384 / FallingMan.PPM, 384 / FallingMan.PPM, 384 / FallingMan.PPM));
        buttons.add(new TutorialButton(this, world, 72 / FallingMan.PPM, goldAndHighScoresIcons.getY() - 72 / FallingMan.PPM * 2 - 384 / FallingMan.PPM * 2, 1296 / FallingMan.PPM, 384 / FallingMan.PPM));
        buttons.add(new RateUsButton(this, world, 72 / FallingMan.PPM, goldAndHighScoresIcons.getY() - 72 / FallingMan.PPM * 3 - 384 / FallingMan.PPM * 3, 1296 / FallingMan.PPM, 384 / FallingMan.PPM));
        /*buttons.add(new MicroPaymentButton(this, world, 150 / FallingMan.PPM, 540 * 3 / FallingMan.PPM + 20 / FallingMan.PPM * 4, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.gold_10000));
        buttons.add(new MicroPaymentButton(this, world, 530 / FallingMan.PPM, 540 * 3 / FallingMan.PPM + 20 / FallingMan.PPM * 4, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.gold_24000));
        buttons.add(new MicroPaymentButton(this, world, 910 / FallingMan.PPM, 540 * 3 / FallingMan.PPM + 20 / FallingMan.PPM * 4, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.gold_54000));
        buttons.add(new MicroPaymentButton(this, world, 150 / FallingMan.PPM, 540 * 2 / FallingMan.PPM + 20 / FallingMan.PPM * 3, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.gold_120000));
        buttons.add(new MicroPaymentButton(this, world, 530 / FallingMan.PPM, 540 * 2 / FallingMan.PPM + 20 / FallingMan.PPM * 3, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.gold_280000));
        buttons.add(new MicroPaymentButton(this, world, 910 / FallingMan.PPM, 540 * 2 / FallingMan.PPM + 20 / FallingMan.PPM * 3, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.gold_750000));

        buttons.add(new MicroPaymentButton(this, world, 150 / FallingMan.PPM, 540 / FallingMan.PPM + 20 / FallingMan.PPM * 2, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.spin_10));
        buttons.add(new MicroPaymentButton(this, world, 530 / FallingMan.PPM, 540 / FallingMan.PPM + 20 / FallingMan.PPM * 2, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.spin_24));
        buttons.add(new MicroPaymentButton(this, world, 910 / FallingMan.PPM, 540 / FallingMan.PPM + 20 / FallingMan.PPM * 2, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.spin_54));
        buttons.add(new MicroPaymentButton(this, world, 150 / FallingMan.PPM, 20 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.spin_120));
        buttons.add(new MicroPaymentButton(this, world, 530 / FallingMan.PPM, 20 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.spin_280));
        buttons.add(new MicroPaymentButton(this, world, 910 / FallingMan.PPM, 20 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.spin_750));*/

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

    @Override
    public void setGameScreen(GameScreen gameScreen) {

    }

    @Override
    public FallingMan getFallingMan() {
        return null;
    }

    @Override
    public void setCurrentScreen(byte currentScreen) {

    }

    @Override
    public void addOnePartRolls(int numberOfOnePartRolls, int typeOfRoll) {

    }

    @Override
    public void addOnePartRolls(int numberOfOnePartRolls, int typeOfRoll, Vector2 pos, String transactionName) {
        for (int i = 0; i < numberOfOnePartRolls; i++) {
            OnePartRoll tempRoll = new OnePartRoll(this, pos.x, pos.y, 192 / FallingMan.PPM, 192 / FallingMan.PPM, typeOfRoll);
            tempRoll.startFlying();
            try {
                tempRoll.setAmount(Long.parseLong(transactionName.substring(5)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            flyingRolls.add(tempRoll);
            //bigChests = new Array<>();
        }
    }

    @Override
    public void addOnePartRolls(int typeOfRoll, Vector2 pos, String transactionName) {

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
        return null;
    }

    @Override
    public GoldAndHighScoresIcons getGoldAndHighScoresIcons() {
        return null;
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
        return null;
    }

    @Override
    public void setGoldX2(boolean goldX2) {

    }

    @Override
    public Array<Cloud> getClouds() {
        return clouds;
    }

    @Override
    public boolean isChangeScreen() {
        return changeScreen;
    }

    @Override
    public void setChangeScreen(boolean changeScreen) {
        this.changeScreen = changeScreen;
    }

    @Override
    public ExtendViewport getGamePort() {
        return gamePort;
    }
}
