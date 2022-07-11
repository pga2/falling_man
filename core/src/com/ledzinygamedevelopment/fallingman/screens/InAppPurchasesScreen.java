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
import com.ledzinygamedevelopment.fallingman.sprites.fallingobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.MicroPaymentButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

import java.util.HashMap;

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
        //gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2) / FallingMan.PPM, (gamePort.getWorldHeight() * FallingMan.PPM / 2) / FallingMan.PPM, 0);
        gameCam.update();
        renderer.setView(gameCam);

        world = new World(new Vector2(0, -3f), true);
        b2dr = new Box2DDebugRenderer();

        worldBodies = new Array<>();

        MapProperties mapProp = map.getProperties();
        mapHeight = mapProp.get("height", Integer.class) * 32;

        buttons = new Array<>();
        generateButtons = true;
        firstTouch = false;
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
                }
            }
            lastTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
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

        for (Button button : buttons) {
            if (button instanceof MicroPaymentButton) {
                ((MicroPaymentButton) button).update(dt);
            }
        }

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

        for (Button button : buttons) {
            button.draw(game.batch);
        }

        game.batch.end();

        if (generateButtons) {
            generateButtons();
            generateButtons = false;
        }
    }

    private void generateButtons() {
        buttons.add(new MicroPaymentButton(this, world, 150 / FallingMan.PPM, gamePort.getWorldHeight() - 540 / FallingMan.PPM - 150 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM));
        buttons.add(new MicroPaymentButton(this, world, 530 / FallingMan.PPM, gamePort.getWorldHeight() - 540 / FallingMan.PPM - 150 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM));
        buttons.add(new MicroPaymentButton(this, world, 910 / FallingMan.PPM, gamePort.getWorldHeight() - 540 / FallingMan.PPM - 150 / FallingMan.PPM, 360 / FallingMan.PPM, 540 / FallingMan.PPM));

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
        return null;
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
        return null;
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
