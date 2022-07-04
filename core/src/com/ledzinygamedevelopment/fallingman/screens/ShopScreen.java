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
import com.ledzinygamedevelopment.fallingman.screens.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.fallingobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.treasurechest.BigChest;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.tools.GameAssetManager;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

import java.util.HashMap;

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
    private Array<Body> worldBodies;
    private Array<Player> players;
    private SaveData saveData;
    private Body bodyHoldPlayers;
    private Vector2 previusFrameTouchPos;
    private boolean createFirstPlayers;

    public ShopScreen(FallingMan game, Array<Vector2> cloudsPositionForNextScreen, float screenHeight) {
        this.game = game;
        currentScreen = FallingMan.CURRENT_SCREEN;

        assetManager = new GameAssetManager();
        assetManager.loadShopScreen();
        assetManager.getManager().finishLoading();
        //defaultAtlas = assetManager.getManager().get(assetManager.getMenuScreenDefault());
        playerAtlas = assetManager.getManager().get(assetManager.getPlayerSprite());
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


    }

    @Override
    public void show() {

    }

    private void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            for (Player player : players) {
                /*if (player.isHeadJointsExist()) {
                    player.setRemoveHeadJoint(true);
                }*/
                if (previusFrameTouchPos.x >= 0) {
                    player.b2body.setLinearVelocity((gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())).x - previusFrameTouchPos.x) * 60, 0);
                    /*for (Body body : player.getBodyPartsAll()) {
                        body.setLinearVelocity((gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())).x - previusFrameTouchPos.x) * 100, 0);
                    }*/
                }
            }
            previusFrameTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        } else {
            previusFrameTouchPos = new Vector2(-1, 0);
            for (Player player : players) {
                /*if (!player.isHeadJointsExist()) {
                    player.createHeadJoint();
                }*/
                player.b2body.setLinearVelocity(0, 0);
                //player.b2body.setAngularVelocity(0);
            }
        }
    }

    private void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 8, 5);

        boolean createPlayerOnRightSide = true;
        boolean createPlayerOnLeftSide = true;
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


        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();

        for (Player player : players) {
            player.draw(game.batch);
            for (PlayerBodyPart playerBodyPart : player.getBodyParts()) {
                playerBodyPart.draw(game.batch);
            }
        }

        game.batch.end();

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
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(7), -FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM / 2, 400 / FallingMan.PPM));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(8), 0, 400 / FallingMan.PPM));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(9), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM / 2, 400 / FallingMan.PPM));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(0), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM, 400 / FallingMan.PPM));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(1), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM * 1.5f, 400 / FallingMan.PPM));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(2), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM * 2f, 400 / FallingMan.PPM));
        players.add(new Player(world, this, mapHeight, createPlayerOneSpriteMap(4), FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM * 2.5f, 400 / FallingMan.PPM));

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

        Player newPlayer = new Player(world, this, mapHeight, createPlayerOneSpriteMap(tempLastPlayer.getHeadSpriteNumber() == 9 ? 0 : tempLastPlayer.getHeadSpriteNumber() + 1), tempLastPlayer.b2body.getPosition().x + FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM / 2, 400 / FallingMan.PPM);
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

        Player newPlayer = new Player(world, this, mapHeight, createPlayerOneSpriteMap(tempFirstPlayer.getHeadSpriteNumber() == 0 ? 9 : tempFirstPlayer.getHeadSpriteNumber() - 1), tempFirstPlayer.b2body.getPosition().x - FallingMan.PLAYER_STARTING_X_POINT / FallingMan.PPM / 2, 400 / FallingMan.PPM);
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
        return playerAtlas;
    }


}
