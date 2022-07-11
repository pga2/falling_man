package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects.Cloud;
import com.ledzinygamedevelopment.fallingman.sprites.fallingobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.PlayerVectors;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;
import com.ledzinygamedevelopment.fallingman.tools.WorldContactListener;

import java.util.HashMap;
import java.util.Random;

public class MenuScreen implements GameScreen {

    private final GameAssetManager assetManager;
    private TextureAtlas playerAtlas;
    private byte currentScreen;
    private Array<Rock> rocks;
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
    private final TextureAtlas bigRockAtlas;
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
    private float gameCamBehindPosition;

    public MenuScreen(FallingMan game, Array<Vector2> cloudsPositionForNextScreen, float screenHeight) {
        this.game = game;
        currentScreen = FallingMan.CURRENT_SCREEN;

        assetManager = new GameAssetManager();
        assetManager.loadMenuScreen();
        assetManager.getManager().finishLoading();
        defaultAtlas = assetManager.getManager().get(assetManager.getMenuScreenDefault());
        bigRockAtlas = assetManager.getManager().get(assetManager.getMenuScreenBigRock());
        playerAtlas = assetManager.getManager().get(assetManager.getPlayerSprite());
        font = assetManager.getManager().get(assetManager.getFont());
        gameCam = new OrthographicCamera();
        gameCamBehind0 = new OrthographicCamera();
        gameCamBehind1 = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("menu_map.tmx");
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

        worldBodies = new Array<>();
        generateMapObjects();

        MapProperties mapProp = map.getProperties();
        saveData = new SaveData();
        saveData.notFirstAppUse();
        player = new Player(world, this, mapProp.get("height", Integer.class) * 32, saveData.getBodySpritesCurrentlyWear());
        world.setContactListener(new WorldContactListener(player, this));

        rocks = new Array<>();
        /*for (int i = 0; i < 100; i++) {
            rocks.add(new Rock(this, world));
        }*/

        Array<Texture> rockTextures = new Array<>();
        for (String path : assetManager.getRockTexturesPaths()) {
            rockTextures.add((Texture) assetManager.getManager().get(path));
        }
        rocks.add(new Rock(this, world, true, rockTextures));
        generateWindows();
        buttons = new Array<>();
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
        gameCamBehindPosition = player.b2body.getPosition().y / 2;

        //gameCam.zoom = 2;
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
                    noButtonTouched = true;

                    // check if buttons click
                    Vector2 mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                    for (Button button : buttons) {
                        if (button.mouseOver(mouseVector) && !button.isLocked()) {
                            button.touched();
                            button.setClicked(true);
                            noButtonTouched = false;
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
                        changeScreen = true;
                    }
                }
            }
        } else {
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
        handleInput(dt);
        world.step(1 / 60f, 8, 5);

        //checking if should generate new map
        if (player.b2body.getPosition().y < (FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM + 30 / FallingMan.PPM) {
            generateNewMap();
        }

        /*gameCamBehind.position.y = player.b2body.getPosition().y;
        gameCamBehind.update();
        renderer.setView(gameCam);*/



        player.b2body.applyLinearImpulse(new Vector2((new Random().nextInt(11) - 5) / 100f, 0), player.b2body.getWorldCenter(), true);

        player.update(dt);

        goldAndHighScoresBackground.update(dt, player.b2body.getPosition(), gamePort.getWorldHeight());
        goldAndHighScoresIcons.update(dt, player.b2body.getPosition(), gamePort.getWorldHeight());

        for (Rock rock : rocks) {
            rock.update(dt, player.b2body.getPosition(), false, true);
        }
        if (player.b2body.getLinearVelocity().y < -20) {
            player.b2body.setLinearVelocity(new Vector2(player.b2body.getLinearVelocity().x, -20));
        }
        for (Button button : buttons) {
            button.update(dt, new Vector2((FallingMan.MIN_WORLD_WIDTH / 2 - 320) / FallingMan.PPM, player.b2body.getPosition().y + button.getyPosPlayerDiff() / FallingMan.PPM));
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
                    currentScreen = FallingMan.ONE_ARMED_BANDIT_SCREEN;
                    break outerloop;
                }
                cloud.update(dt, 0, (player.b2body.getLinearVelocity().y - 120) / FallingMan.PPM);
            } else if (cloud.getY() > player.getY() + gamePort.getWorldHeight() * 2) {
                cloudsToRemove.add(cloud);
            } else {
                cloud.update(dt, 0, (player.b2body.getLinearVelocity().y + 120) / FallingMan.PPM);
            }
        }
        clouds.removeAll(cloudsToRemove, false);



        firstOpenTimer +=dt;
    }

    @Override
    public void render(float delta) {
        update(delta);

        //clear screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render map
        gameCam.position.y = FallingMan.MAX_WORLD_HEIGHT / 2f / FallingMan.PPM;
        gameCam.update();
        rendererBehind0.setView(gameCam);
        rendererBehind0.render(new int[]{0, 1});
        gameCamBehindPosition += player.b2body.getLinearVelocity().y / 2 / FallingMan.PPM;
        if (gameCamBehindPosition < -(mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM / 2) {
            gameCamBehindPosition += (mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM;
        }
        gameCam.position.y = gameCamBehindPosition;
        gameCam.update();
        rendererBehind0.setView(gameCam);
        rendererBehind0.render(new int[]{2});
        gameCam.position.y = gameCamBehindPosition + (mapBehind0.getProperties().get("height", Integer.class) * 32) / FallingMan.PPM;
        gameCam.update();
        rendererBehind1.setView(gameCam);
        rendererBehind1.render(new int[]{2});
        gameCam.position.y = player.b2body.getPosition().y;
        gameCam.update();
        renderer.setView(gameCam);
        renderer.render();

        //render box2d debug renderer
        //b2dr.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);
        //game.batch.setProjectionMatrix(gameCamBehind.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (PlayerBodyPart bodyPart : player.getBodyParts()) {
            bodyPart.draw(game.batch);
        }
        for (Rock rock : rocks) {
            rock.draw(game.batch);
        }
        goldAndHighScoresBackground.draw(game.batch);
        goldAndHighScoresIcons.draw(game.batch);
        for (Button button : buttons) {
            button.draw(game.batch);
        }



        //preparing font
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        font.setColor(100 / 256f, 80 / 256f, 0 / 256f, 1);
        if (fontScaleUp) {
            if (fontScale < 0.0066f) {
                fontScale += 0.00004f;
                font.getData().setScale(fontScale);
            } else {
                font.getData().setScale(fontScale);
                //fontScale += 0.00004f;
                fontScaleUp = false;
            }
        } else {
            if (fontScale > 0.0058f) {
                fontScale -= 0.00004f;
                font.getData().setScale(fontScale);
            } else {
                fontScale -= 0.00004f;
                font.getData().setScale(fontScale);
                fontScaleUp = true;
            }
        }
        GlyphLayout glyphLayout = new GlyphLayout(font, "SWIPE UP\nOR\nTOUCH THE SCREEN");
        font.draw(game.batch, "SWIPE UP\nOR\nTOUCH THE SCREEN", 720 / FallingMan.PPM - glyphLayout.width / 2, player.getY() + glyphLayout.height * 2, glyphLayout.width, Align.center, false);

        for (Cloud cloud : clouds) {
            cloud.draw(game.batch);
        }





        game.batch.end();


        switch (currentScreen) {
            case FallingMan.ONE_ARMED_BANDIT_SCREEN:
                dispose();
                game.setScreen(new OneArmedBanditScreen(game, cloudsPositionForNextScreen, player.getY(), false));
                break;
            case FallingMan.PLAY_SCREEN:
                PlayerVectors playerVectors = new PlayerVectors(player, true);

                MapProperties mapProp = map.getProperties();
                Vector3 rockPos = new Vector3(rocks.get(0).getB2body().getPosition().x, rocks.get(0).getB2body().getPosition().y - player.b2body.getPosition().y, rocks.get(0).getB2body().getAngle());
                float rockAnimationTimer = rocks.get(0).getAnimationTimer();
                playerVectors.calculatePlayerVectors(mapProp.get("height", Integer.class) * 32);
                dispose();
                game.setScreen(new PlayScreen(game, playerVectors, rockPos, rockAnimationTimer));
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
    public TextureAtlas getBigRockAtlas() {
        return bigRockAtlas;
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
        return goldAndHighScoresIcons;
    }

    @Override
    public HashMap<String, Boolean> getOwnedBodySprites() {
        return null;
    }

    public void generateNewMap() {

        //transforming player position to new map
        Vector2 playerPosPrevious = new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);

        MapProperties mapProp = map.getProperties();
        player.updateBodyParts(mapProp.get("height", Integer.class) * 32);

        //transforming rocks position to new map
        for (Rock rock : rocks) {
            rock.generateMapRockUpdate(playerPosPrevious, mapProp.get("height", Integer.class) * 32);
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
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / FallingMan.PPM, (rect.getY() + rect.getHeight() / 2) / FallingMan.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2)  / FallingMan.PPM, (rect.getHeight() / 2)  / FallingMan.PPM);
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
}
