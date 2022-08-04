package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects.Cloud;
import com.ledzinygamedevelopment.fallingman.sprites.fallingobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.MicroPaymentButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

import java.util.HashMap;
import java.util.Random;

public class InAppPurchasesScreen implements GameScreen {

    private final GameAssetManager assetManager;
    private int mapHeight;
    private BitmapFont font;
    private byte currentScreen;
    private Array<Rock> rocks;
    private OrthographicCamera gameCam;
    private ExtendViewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
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

    public InAppPurchasesScreen(FallingMan game, Array<Vector2> cloudsPositionForNextScreen, float screenHeight) {
        this.game = game;
        currentScreen = FallingMan.CURRENT_SCREEN;

        assetManager = new GameAssetManager();
        assetManager.loadInAppPurchasesScreen();
        assetManager.getManager().finishLoading();
        //defaultAtlas = assetManager.getManager().get(assetManager.getMenuScreenDefault());
        defaultAtlas = assetManager.getManager().get(assetManager.getInAppPurchasesDefault());
        map = assetManager.getManager().get(assetManager.getInAppPurchasesMap());
        font = assetManager.getManager().get(assetManager.getFont());
        saveData = new SaveData();
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);

        mapLoader = new TmxMapLoader();
        //map = mapLoader.load("menu_map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);
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
            for (Button button : buttons) {
                if (button.mouseOver(gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())))) {
                    button.touched();
                } else if (button.isClicked()) {
                    button.setClicked(false);
                    button.restoreNotClickedTexture();
                }
            }
            lastTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            //menuScreen
            if (lastTouchPos.y - startTouchingPos.y > 200 / FallingMan.PPM) {
                if (!changeScreen) {
                    Random random = new Random();
                    //currentScreen = FallingMan.MENU_SCREEN;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 26; j++) {
                            clouds.add(new Cloud(this, ((i * 640) - random.nextInt(220)) / FallingMan.PPM, ((-150 * j) - random.nextInt(21)) / FallingMan.PPM, false, FallingMan.ONE_ARMED_BANDIT_SCREEN));
                        }
                    }
                    changeScreen = true;
                }
            }
        } else {
            if (firstTouch) {
                for (Button button : buttons) {
                    if (button.mouseOver(gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())))) {
                        button.notTouched();
                    }
                }

                firstTouch = false;
            }
        }

    }

    private void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 8, 5);

        if (gamePort.getWorldHeight() / gamePort.getWorldWidth() > 1.8888f) {
            goldAndHighScoresBackground.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), gamePort.getWorldHeight());
            goldAndHighScoresIcons.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), gamePort.getWorldHeight());
        } else {
            goldAndHighScoresBackground.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM + 150 / FallingMan.PPM);
            goldAndHighScoresIcons.update(dt, new Vector2(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2), FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM + 150 / FallingMan.PPM);

        }

        for (Button button : buttons) {
            if (button instanceof MicroPaymentButton) {
                ((MicroPaymentButton) button).update(dt);
            }
        }

        Array<OnePartRoll> flyingRollsToRemove = new Array<>();
        for (OnePartRoll flyingRoll : flyingRolls) {
            flyingRoll.flyingRollUpdate(dt, new Vector2(goldAndHighScoresIcons.getX() + goldAndHighScoresIcons.getWidth() / 2, goldAndHighScoresIcons.getY() + goldAndHighScoresIcons.getHeight() / 4 * 3), saveData);
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
        flyingRolls.removeAll(flyingRollsToRemove, false);

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
                    currentScreen = FallingMan.SHOP_SCREEN;
                    break outerloop;
                }
                cloud.update(dt, 0, 1.2f);
            } else if (cloud.getY() < -FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM) {
                cloudsToRemove.add(cloud);
            } else {
                cloud.update(dt, 0, -1.2f);
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
        renderer.render();


        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();

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
            case FallingMan.SHOP_SCREEN:
                dispose();
                FallingMan.gameScreen = new ShopScreen(game, cloudsPositionForNextScreen, gamePort.getWorldHeight(), true);
                FallingMan.currentScreen = FallingMan.SHOP_SCREEN;
                game.setScreen(FallingMan.gameScreen);
                break;
        }
    }

    private void generateButtons() {
        buttons.add(new MicroPaymentButton(this, world, 150 / FallingMan.PPM, 540 * 3 / FallingMan.PPM + 20 / FallingMan.PPM * 4, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.gold_10000));
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
        buttons.add(new MicroPaymentButton(this, world, 910 / FallingMan.PPM, 20 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM, FallingMan.spin_750));

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
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        assetManager.getManager().dispose();
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

}
