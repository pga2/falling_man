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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.sprites.shopsprites.BodyPartBacklight;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.sprites.windows.PriceBackground;
import com.ledzinygamedevelopment.fallingman.sprites.changescreenobjects.Cloud;
import com.ledzinygamedevelopment.fallingman.sprites.fallingobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.BuyButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.Prices;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

import java.util.HashMap;
import java.util.Random;

public class ShopScreen implements GameScreen {

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
    private TextureAtlas playerAtlas;
    private FallingMan game;
    private boolean changeToShopInAppPurchasesScreen;
    private Array<Body> worldBodies;
    private Array<Player> players;
    private SaveData saveData;
    private Body bodyHoldPlayers;
    private Vector2 previusFrameTouchPos;
    private boolean createFirstPlayers;
    private Vector2 startTouchingPos;
    private Vector2 lastTouchPos;
    private boolean firstTouch;
    private boolean clicked;
    private Array<Player> playersInScreenMiddle;
    private Array<Button> buttons;
    private Prices prices;
    private GoldAndHighScoresIcons goldAndHighScoresIcons;
    private GoldAndHighScoresBackground goldAndHighScoresBackground;
    private PriceBackground priceBackground;
    private boolean drawPriceBackground;
    private HashMap<String, Boolean> ownedBodySprites;
    private Player currentPlayer;
    private Array<Cloud> clouds;
    private Array<Vector2> cloudsPositionForNextScreen;
    private boolean changeScreen;
    private Array<BodyPartBacklight> bodyPartBacklights;

    public ShopScreen(FallingMan game, Array<Vector2> cloudsPositionForNextScreen, float screenHeight, boolean changeToShopInAppPurchasesScreen) {
        this.game = game;
        this.changeToShopInAppPurchasesScreen = changeToShopInAppPurchasesScreen;
        currentScreen = FallingMan.CURRENT_SCREEN;

        assetManager = new GameAssetManager();
        assetManager.loadShopScreen();
        assetManager.getManager().finishLoading();
        //defaultAtlas = assetManager.getManager().get(assetManager.getMenuScreenDefault());
        playerAtlas = assetManager.getManager().get(assetManager.getPlayerSprite());
        defaultAtlas = assetManager.getManager().get(assetManager.getShopScreenDefault());
        map = assetManager.getManager().get(assetManager.getShopScreenMap());
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

        generateMapObjects();
        players = new Array<>();


        createFirstPlayers = true;
        //players.get(0).b2body.setLinearVelocity(10, 0);
        //players.get(0).getBelly().getB2body().applyLinearImpulse(new Vector2(1f, 0f), players.get(0).getBelly().getB2body().getWorldCenter(), true);

        //gameCam.zoom = 1.1f;
        previusFrameTouchPos = new Vector2(-1, 0);
        createFirstPlayers();

        firstTouch = false;
        playersInScreenMiddle = new Array<>();
        buttons = new Array<>();
        prices = new Prices();

        generateWindows();
        ownedBodySprites = saveData.getBodySpritesOwned();
        currentPlayer = new Player(world, this, mapProp.get("height", Integer.class) * 32, saveData.getBodySpritesCurrentlyWear(), FallingMan.MAX_WORLD_WIDTH / 4f / FallingMan.PPM, 1500 / FallingMan.PPM, false);
        currentPlayer.createHeadJoint();

        clouds = new Array<>();
        for (Vector2 pos : cloudsPositionForNextScreen) {
            Cloud cloud = new Cloud(this, 0, 0, true, changeToShopInAppPurchasesScreen ? FallingMan.IN_APP_PURCHASES_SCREEN : FallingMan.ONE_ARMED_BANDIT_SCREEN);
            cloud.setPosition(pos.x, pos.y);
            clouds.add(cloud);
        }
        this.cloudsPositionForNextScreen = new Array<>();
        changeScreen = false;

        bodyPartBacklights = new Array<>();
    }

    @Override
    public void show() {

    }

    private void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            if (!firstTouch) {
                startTouchingPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                firstTouch = true;
                clicked = true;
                lastTouchPos = null;
            }
            if (gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())).y < 550 / FallingMan.PPM) {
                for (Player player : players) {
                    if (previusFrameTouchPos.x >= 0) {
                        player.b2body.setLinearVelocity((gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())).x - previusFrameTouchPos.x) * 60, 0);
                    }
                }

                previusFrameTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            }
            if (lastTouchPos != null && !(Math.abs(lastTouchPos.x - startTouchingPos.x) < 4 / FallingMan.PPM) && !(Math.abs(lastTouchPos.y - startTouchingPos.y) < 4 / FallingMan.PPM)) {
                clicked = false;
            }
            for (Button button : buttons) {
                if (button.mouseOver(gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())))) {
                    button.touched();
                }
            }
            lastTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            //InAppPurchases
            if (lastTouchPos.y - startTouchingPos.y < -200 / FallingMan.PPM) {
                if (!changeScreen) {
                    Random random = new Random();
                    //currentScreen = FallingMan.MENU_SCREEN;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 26; j++) {
                            clouds.add(new Cloud(this, ((i * 640) - random.nextInt(220)) / FallingMan.PPM, gamePort.getWorldHeight() + ((140 * j) - random.nextInt(21)) / FallingMan.PPM, false, FallingMan.IN_APP_PURCHASES_SCREEN));
                        }
                    }
                    changeScreen = true;
                    changeToShopInAppPurchasesScreen = true;
                }
            }
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
                firstTouch = false;
                if (clicked) {
                    for (Player player : players) {
                        if (player.mouseOver(lastTouchPos)) {
                            if (playersInScreenMiddle.size > 0) {
                                for (Player player1 : playersInScreenMiddle) {
                                    world.destroyBody(player1.b2body);
                                    for (Body body : player1.getBodyPartsAll()) {
                                        world.destroyBody(body);
                                    }
                                }
                                buttons = new Array<>();
                                drawPriceBackground = false;
                                playersInScreenMiddle = new Array<>();
                            }
                            bodyPartBacklights = new Array<>();
                            playersInScreenMiddle.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(player.getHeadSpriteNumber()), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, mapHeight / FallingMan.PPM / 2, true));
                        }
                    }
                    boolean touchedOutsideButtons = true;
                    for (Player player : playersInScreenMiddle) {
                        for (int i = 0; i < player.getBodyParts().size; i++) {
                            PlayerBodyPart playerBodyPart = player.getBodyParts().get(i);
                            if (playerBodyPart.mouseOver(lastTouchPos)) {
                                boolean addBuyButton = true;
                                for (Button button : buttons) {
                                    if (button instanceof BuyButton) {
                                        ((BuyButton) button).setBodyPartName(playerBodyPart.getBodyPartName());
                                        ((BuyButton) button).setSpriteNumber(playerBodyPart.getSpriteNumber());
                                        priceBackground.setPrice(((BuyButton) button).getPrice());
                                        bodyPartBacklights = new Array<>();
                                        bodyPartBacklights.add(new BodyPartBacklight(this, playerBodyPart.getX() + playerBodyPart.getWidth() / 2, playerBodyPart.getY() + playerBodyPart.getHeight() / 2));
                                        for (PlayerBodyPart playerBodyPart1 : player.getBodyParts()) {
                                            if (playerBodyPart != playerBodyPart1) {
                                                playerBodyPart1.setColor(playerBodyPart1.getColor().r, playerBodyPart1.getColor().g, playerBodyPart1.getColor().b, 0.5f);
                                            } else {
                                                playerBodyPart1.setColor(playerBodyPart1.getColor().r, playerBodyPart1.getColor().g, playerBodyPart1.getColor().b, 1f);
                                            }
                                        }
                                        player.setColor(player.getColor().r, player.getColor().g, player.getColor().b, 0.5f);
                                        addBuyButton = false;
                                    }
                                }
                                if (addBuyButton) {
                                    BuyButton button = new BuyButton(this, world, FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - 544 / 2f / FallingMan.PPM + 330 / FallingMan.PPM, 600 / FallingMan.PPM, playerBodyPart.getSpriteNumber(), playerBodyPart.getBodyPartName(), saveData, prices);
                                    buttons.add(button);
                                    drawPriceBackground = true;
                                    priceBackground.setPrice(button.getPrice());
                                    bodyPartBacklights.add(new BodyPartBacklight(this, playerBodyPart.getX() + playerBodyPart.getWidth() / 2, playerBodyPart.getY() + playerBodyPart.getHeight() / 2));
                                    for (PlayerBodyPart playerBodyPart1 : player.getBodyParts()) {
                                        if (playerBodyPart != playerBodyPart1) {
                                            playerBodyPart1.setColor(playerBodyPart1.getColor().r, playerBodyPart1.getColor().g, playerBodyPart1.getColor().b, 0.5f);
                                        }
                                    }
                                    player.setColor(player.getColor().r, player.getColor().g, player.getColor().b, 0.5f);
                                } else {
                                    for (Button button : buttons) {
                                        if (button instanceof BuyButton) {
                                            ((BuyButton) button).setSpriteNumber(playerBodyPart.getSpriteNumber());
                                            ((BuyButton) button).setBodyPartName(playerBodyPart.getBodyPartName());
                                            priceBackground.setPrice(((BuyButton) button).getPrice());
                                        }
                                    }
                                }
                                touchedOutsideButtons = false;

                            }
                        }
                        if (player.mouseOverHead(lastTouchPos)) {
                            boolean addBuyButton = true;
                            for (Button button : buttons) {
                                if (button instanceof BuyButton) {
                                    addBuyButton = false;
                                }
                            }
                            if (addBuyButton) {
                                BuyButton button = new BuyButton(this, world, FallingMan.MAX_WORLD_WIDTH / 2f / FallingMan.PPM - 544 / 2f / FallingMan.PPM + 330 / FallingMan.PPM, 600 / FallingMan.PPM, player.getHeadSpriteNumber(), "head", saveData, prices);
                                buttons.add(button);
                                drawPriceBackground = true;
                                priceBackground.setPrice(button.getPrice());
                                bodyPartBacklights.add(new BodyPartBacklight(this, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2));
                                for (PlayerBodyPart playerBodyPart : player.getBodyParts()) {
                                    playerBodyPart.setColor(playerBodyPart.getColor().r, playerBodyPart.getColor().g, playerBodyPart.getColor().b, 0.5f);
                                }
                            } else {
                                for (Button button : buttons) {
                                    if (button instanceof BuyButton) {
                                        ((BuyButton) button).setSpriteNumber(player.getHeadSpriteNumber());
                                        ((BuyButton) button).setBodyPartName("head");
                                        priceBackground.setPrice(((BuyButton) button).getPrice());
                                    }
                                }
                                bodyPartBacklights = new Array<>();
                                bodyPartBacklights.add(new BodyPartBacklight(this, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2));
                                for (PlayerBodyPart playerBodyPart : player.getBodyParts()) {
                                    playerBodyPart.setColor(playerBodyPart.getColor().r, playerBodyPart.getColor().g, playerBodyPart.getColor().b, 0.5f);
                                }
                                player.setColor(player.getColor().r, player.getColor().g, player.getColor().b, 1);
                            }
                            touchedOutsideButtons = false;
                        }
                    }
                    if (lastTouchPos.y < 550 / FallingMan.PPM) {
                        touchedOutsideButtons = false;
                    }
                    for (Button button : buttons) {
                        /*if (button.isClicked()) {
                            button.notTouched();
                        }*/
                        if (button.mouseOver(lastTouchPos)) {
                            touchedOutsideButtons = false;
                        }
                    }
                    if (touchedOutsideButtons) {
                        for (Player player : playersInScreenMiddle) {
                            for (PlayerBodyPart playerBodyPart : player.getBodyParts()) {
                                playerBodyPart.setColor(playerBodyPart.getColor().r, playerBodyPart.getColor().g, playerBodyPart.getColor().b, 1);
                            }
                            player.setColor(player.getColor().r, player.getColor().g, player.getColor().b, 1);
                        }
                        bodyPartBacklights = new Array<>();
                        buttons = new Array<>();
                        drawPriceBackground = false;
                    }
                }
                for (Button button : buttons) {
                    if (button.isClicked()) {
                        button.notTouched();
                    }
                }
            }
            previusFrameTouchPos = new Vector2(-1, 0);
            for (Player player : players) {
                /*if (!player.isHeadJointsExist()) {
                    player.createHeadJoint();
                }*/
                player.b2body.setLinearVelocity(0, 0);
                //player.b2body.setAngularVelocity(0);
            }
            firstTouch = false;
        }
    }

    private void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 8, 5);

        boolean createPlayerOnRightSide = true;
        boolean createPlayerOnLeftSide = true;

        for (BodyPartBacklight bodyPartBacklight : bodyPartBacklights) {
            bodyPartBacklight.update(dt);
        }

        for (Player player : players) {
            if (player.b2body.getPosition().x < 0) {
                createPlayerOnLeftSide = false;
            }
            if (player.b2body.getPosition().x > FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM) {
                createPlayerOnRightSide = false;
            }
        }
        if (createPlayerOnRightSide) {
            createPlayerOnRightSide();
        } else if (createPlayerOnLeftSide) {
            createPlayerOnLeftSide();
        }


        for (Player player : players) {
            player.update(dt);
        }

        for (Player player : playersInScreenMiddle) {
            player.update(dt);
        }

        currentPlayer.update(dt);

        Array<Button> buttonsToRemove = new Array<>();
        for (Button button : buttons) {
            if (button.isToRemove()) {
                buttonsToRemove.add(button);
            } else if (button instanceof BuyButton) {
                ((BuyButton) button).update(dt);
            }
        }
        buttons.removeAll(buttonsToRemove, false);


        goldAndHighScoresBackground.update(dt, new Vector2(0, gamePort.getWorldHeight() / 2), gamePort.getWorldHeight());
        goldAndHighScoresIcons.update(dt, new Vector2(0, gamePort.getWorldHeight() / 2), gamePort.getWorldHeight());
        for (Player player : playersInScreenMiddle) {
            priceBackground.update(dt, new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y));
        }

        Array<Cloud> cloudsToRemove = new Array<>();
        outerloop:
        for (Cloud cloud : clouds) {
            if (!cloud.isSecondScreen()) {
                if (cloud.getY() > FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM && cloud.getScreen() == FallingMan.ONE_ARMED_BANDIT_SCREEN) {
                    for (Cloud cloudGetPos : clouds) {
                        if (!cloudGetPos.isSecondScreen()) {
                            cloudsPositionForNextScreen.add(new Vector2(cloudGetPos.getX(), cloudGetPos.getY()));
                        }
                    }
                    currentScreen = FallingMan.ONE_ARMED_BANDIT_SCREEN;
                    break outerloop;
                } else if (cloud.getY() < 0 && cloud.getScreen() == FallingMan.IN_APP_PURCHASES_SCREEN) {
                    for (Cloud cloudGetPos : clouds) {
                        if (!cloudGetPos.isSecondScreen()) {
                            cloudsPositionForNextScreen.add(new Vector2(cloudGetPos.getX(), cloudGetPos.getY()));
                        }
                    }
                    currentScreen = FallingMan.IN_APP_PURCHASES_SCREEN;
                    break outerloop;
                }
                if (cloud.getScreen() == FallingMan.ONE_ARMED_BANDIT_SCREEN) {
                    cloud.update(dt, 0, 1.2f);
                } else if (cloud.getScreen() == FallingMan.IN_APP_PURCHASES_SCREEN) {
                    cloud.update(dt, 0, -1.2f);
                }
            } else if (cloud.getY() < -FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM && cloud.getScreen() == FallingMan.ONE_ARMED_BANDIT_SCREEN) {
                cloudsToRemove.add(cloud);
            } else if (cloud.getScreen() == FallingMan.ONE_ARMED_BANDIT_SCREEN) {
                cloud.update(dt, 0, -1.2f);
            } else if (cloud.getY() > FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM * 2) {
                cloudsToRemove.add(cloud);
            } else {
                cloud.update(dt, 0, 1.2f);
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

        for (BodyPartBacklight bodyPartBacklight : bodyPartBacklights) {
            bodyPartBacklight.draw(game.batch);
        }

        for (Player player : players) {
            player.draw(game.batch);
            for (PlayerBodyPart playerBodyPart : player.getBodyParts()) {
                playerBodyPart.draw(game.batch);
            }
        }

        for (Player player : playersInScreenMiddle) {
            player.draw(game.batch);
            for (PlayerBodyPart playerBodyPart : player.getBodyParts()) {
                playerBodyPart.draw(game.batch);
            }
        }

        currentPlayer.draw(game.batch);
        for (PlayerBodyPart playerBodyPart : currentPlayer.getBodyParts()) {
            playerBodyPart.draw(game.batch);
        }

        if (drawPriceBackground) {
            priceBackground.draw(game.batch);
        }

        for (Button button : buttons) {
            button.draw(game.batch);
        }

        goldAndHighScoresBackground.draw(game.batch);
        goldAndHighScoresIcons.draw(game.batch);

        for (Cloud cloud : clouds) {
            cloud.draw(game.batch);
        }

        game.batch.end();

        switch (currentScreen) {
            case FallingMan.ONE_ARMED_BANDIT_SCREEN:
                dispose();
                FallingMan.currentScreen = FallingMan.ONE_ARMED_BANDIT_SCREEN;
                game.setScreen(new OneArmedBanditScreen(game, cloudsPositionForNextScreen, gamePort.getWorldHeight(), true));
                break;
            case FallingMan.IN_APP_PURCHASES_SCREEN:
                dispose();
                FallingMan.currentScreen = FallingMan.IN_APP_PURCHASES_SCREEN;
                FallingMan.gameScreen = new InAppPurchasesScreen(game, cloudsPositionForNextScreen, gamePort.getWorldHeight());
                game.setScreen(FallingMan.gameScreen);
                break;
        }

        //render box2d debug renderer
        //b2dr.render(world, gameCam.combined);
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

    public void generateMapObjects() {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, 400 / FallingMan.PPM);
        bodyHoldPlayers = world.createBody(bdef);
        shape.setAsBox(1 / FallingMan.PPM, 1 / FallingMan.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;
        bodyHoldPlayers.createFixture(fdef);
        //worldBodies.add(bodyHoldPlayers);

        /*//walls
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
        }*/
    }

    private void createFirstPlayers() {
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(FallingMan.ALL_BODY_SPRITES_LENGHT - 2), -FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM / 2, 400 / FallingMan.PPM, false));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(FallingMan.ALL_BODY_SPRITES_LENGHT - 1), 0, 400 / FallingMan.PPM, false));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(FallingMan.ALL_BODY_SPRITES_LENGHT), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM / 2, 400 / FallingMan.PPM, false));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(0), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, 400 / FallingMan.PPM, false));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(1), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM * 1.5f, 400 / FallingMan.PPM, false));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(2), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM * 2f, 400 / FallingMan.PPM, false));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(4), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM * 2.5f, 400 / FallingMan.PPM, false));

        PrismaticJointDef pJointDef = new PrismaticJointDef();
        DistanceJointDef dJointDef = new DistanceJointDef();
        for (int i = 0; i < players.size; i++) {
            Player tempPlayer = players.get(i);
            pJointDef.bodyA = bodyHoldPlayers;
            pJointDef.bodyB = tempPlayer.b2body;
            world.createJoint(pJointDef);
            if (i > 0) {
                Player previousPlayer = players.get(i - 1);
                dJointDef.bodyA = previousPlayer.b2body;
                dJointDef.bodyB = tempPlayer.b2body;
                dJointDef.length = tempPlayer.b2body.getPosition().x - previousPlayer.b2body.getPosition().x;
                world.createJoint(dJointDef);
            }
        }
    }

    private HashMap<String, Integer> createPlayerOneSpriteMap(int numberOfSprite) {
        HashMap<String, Integer> allBodyPartsSprites = new HashMap<>();
        allBodyPartsSprites.put("head", numberOfSprite);
        allBodyPartsSprites.put("belly", numberOfSprite);
        allBodyPartsSprites.put("armL", numberOfSprite);
        allBodyPartsSprites.put("foreArmL", numberOfSprite);
        allBodyPartsSprites.put("handL", numberOfSprite);
        allBodyPartsSprites.put("armR", numberOfSprite);
        allBodyPartsSprites.put("foreArmR", numberOfSprite);
        allBodyPartsSprites.put("handR", numberOfSprite);
        allBodyPartsSprites.put("thighL", numberOfSprite);
        allBodyPartsSprites.put("shinL", numberOfSprite);
        allBodyPartsSprites.put("footL", numberOfSprite);
        allBodyPartsSprites.put("thighR", numberOfSprite);
        allBodyPartsSprites.put("shinR", numberOfSprite);
        allBodyPartsSprites.put("footR", numberOfSprite);

        return allBodyPartsSprites;
    }

    public void createPlayerOnRightSide() {
        Player tempPlayer = players.get(0);
        for (Player player : players) {
            if (player.b2body.getPosition().x < tempPlayer.b2body.getPosition().x) {
                tempPlayer = player;
            }
        }
        world.destroyBody(tempPlayer.b2body);
        for (Body body : tempPlayer.getBodyPartsAll()) {
            world.destroyBody(body);
        }
        players.removeValue(tempPlayer, false);

        Player tempLastPlayer = players.get(0);
        for (Player player : players) {
            if (player.b2body.getPosition().x > tempLastPlayer.b2body.getPosition().x) {
                tempLastPlayer = player;
            }
        }

        Player newPlayer = new Player(world, this, mapHeight, createPlayerOneSpriteMap(tempLastPlayer.getHeadSpriteNumber() == FallingMan.ALL_BODY_SPRITES_LENGHT ? 0 : tempLastPlayer.getHeadSpriteNumber() + 1), tempLastPlayer.b2body.getPosition().x + FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM / 2, 400 / FallingMan.PPM, false);
        players.add(newPlayer);

        PrismaticJointDef pJointDef = new PrismaticJointDef();
        DistanceJointDef dJointDef = new DistanceJointDef();
        pJointDef.bodyA = bodyHoldPlayers;
        pJointDef.bodyB = newPlayer.b2body;
        world.createJoint(pJointDef);
        dJointDef.bodyA = tempLastPlayer.b2body;
        dJointDef.bodyB = newPlayer.b2body;
        dJointDef.length = newPlayer.b2body.getPosition().x - tempLastPlayer.b2body.getPosition().x;
        world.createJoint(dJointDef);
    }

    private void createPlayerOnLeftSide() {
        Player tempPlayer = players.get(0);
        for (Player player : players) {
            if (player.b2body.getPosition().x > tempPlayer.b2body.getPosition().x) {
                tempPlayer = player;
            }
        }
        world.destroyBody(tempPlayer.b2body);
        for (Body body : tempPlayer.getBodyPartsAll()) {
            world.destroyBody(body);
        }
        players.removeValue(tempPlayer, false);

        Player tempFirstPlayer = players.get(0);
        for (Player player : players) {
            if (player.b2body.getPosition().x < tempFirstPlayer.b2body.getPosition().x) {
                tempFirstPlayer = player;
            }
        }

        Player newPlayer = new Player(world, this, mapHeight, createPlayerOneSpriteMap(tempFirstPlayer.getHeadSpriteNumber() == 0 ? FallingMan.ALL_BODY_SPRITES_LENGHT : tempFirstPlayer.getHeadSpriteNumber() - 1), tempFirstPlayer.b2body.getPosition().x - FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM / 2, 400 / FallingMan.PPM, false);
        players.add(newPlayer);

        PrismaticJointDef pJointDef = new PrismaticJointDef();
        DistanceJointDef dJointDef = new DistanceJointDef();
        pJointDef.bodyA = bodyHoldPlayers;
        pJointDef.bodyB = newPlayer.b2body;
        world.createJoint(pJointDef);
        dJointDef.bodyA = tempFirstPlayer.b2body;
        dJointDef.bodyB = newPlayer.b2body;
        dJointDef.length = Math.abs(newPlayer.b2body.getPosition().x - tempFirstPlayer.b2body.getPosition().x);
        world.createJoint(dJointDef);
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
        return currentPlayer;
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

    public void generateWindows() {
        goldAndHighScoresBackground = new GoldAndHighScoresBackground(this, world);
        goldAndHighScoresIcons = new GoldAndHighScoresIcons(this, world, saveData.getGold(), saveData.getHighScore());
        priceBackground = new PriceBackground(this, world);
        drawPriceBackground = false;
    }

    public PriceBackground getPriceBackground() {
        return priceBackground;
    }

    public HashMap<String, Boolean> getOwnedBodySprites() {
        return ownedBodySprites;
    }
}
