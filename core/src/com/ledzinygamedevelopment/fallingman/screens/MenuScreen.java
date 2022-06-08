package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.screens.windows.GoldAndHighScoresIcons;
import com.ledzinygamedevelopment.fallingman.sprites.fallingfromwallsobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.HighScoresButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.PlayButton;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.tools.PlayerVectors;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;
import com.ledzinygamedevelopment.fallingman.tools.WorldContactListener;

import java.util.Random;

public class MenuScreen implements GameScreen {

    private byte currentScreen;
    private Array<Rock> rocks;
    private OrthographicCamera gameCam;
    private ExtendViewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;
    private TextureAtlas atlas;
    private FallingMan game;
    private Array<Body> worldBodies;
    private Array<Button> buttons;
    private boolean loadNewGame;
    private GoldAndHighScoresIcons goldAndHighScoresIcons;
    private GoldAndHighScoresBackground goldAndHighScoresBackground;
    private SaveData saveData;

    public MenuScreen(FallingMan game) {
        this.game = game;
        currentScreen = FallingMan.CURRENT_SCREEN;
        atlas = new TextureAtlas("player.pack");
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("menu_map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);
        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2) / FallingMan.PPM, 0);

        world = new World(new Vector2(0, -4f), true);
        b2dr = new Box2DDebugRenderer();

        worldBodies = new Array<>();
        generateMapObjects();

        player = new Player(world, this);
        world.setContactListener(new WorldContactListener(player, this));

        rocks = new Array<>();
        for (int i = 0; i < 200; i++) {
            rocks.add(new Rock(this, world));
        }
        generateWindows();
        buttons = new Array<>();
        buttons.add(new PlayButton(this, world, (FallingMan.MIN_WORLD_WIDTH / 2 - 320) / FallingMan.PPM, player.b2body.getPosition().y + 200 / FallingMan.PPM, 640 / FallingMan.PPM, 224 / FallingMan.PPM));
        buttons.add(new HighScoresButton(this, world, (FallingMan.MIN_WORLD_WIDTH / 2 - 320) / FallingMan.PPM, player.b2body.getPosition().y + 424 / FallingMan.PPM, 640 / FallingMan.PPM, 224 / FallingMan.PPM));
        //player.b2body.applyLinearImpulse(new Vector2(3, 0), player.b2body.getWorldCenter(), true);
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {

            // check if buttons click
            Vector2 mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            for (Button button : buttons) {
                if (button.mouseOver(mouseVector) && !button.isLocked()) {
                    button.touched();
                    button.setClicked(true);
                }
            }
        } else {
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

        gameCam.position.y = player.b2body.getPosition().y;
        gameCam.update();
        renderer.setView(gameCam);

        player.b2body.applyLinearImpulse(new Vector2((new Random().nextInt(11) - 5) / 100f, 0), player.b2body.getWorldCenter(), true);

        player.update(dt);

        goldAndHighScoresBackground.update(dt, player.b2body.getPosition());
        goldAndHighScoresIcons.update(dt, player.b2body.getPosition());

        for (Rock rock : rocks) {
            rock.update(dt, player.b2body.getPosition(), false);
        }
        if (player.b2body.getLinearVelocity().y < -20) {
            player.b2body.setLinearVelocity(new Vector2(player.b2body.getLinearVelocity().x, -20));
        }
        for (Button button : buttons) {
            button.update(dt, new Vector2((FallingMan.MIN_WORLD_WIDTH / 2 - 320) / FallingMan.PPM, player.b2body.getPosition().y + button.getyPosPlayerDiff() / FallingMan.PPM));
        }


        //checking if should generate new map
        if (player.b2body.getPosition().y < FallingMan.MAX_WORLD_HEIGHT * 0.6f / FallingMan.PPM) {
            generateNewMap();
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

        //render box2d debug renderer
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
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
        game.batch.end();


        switch (currentScreen) {
            case FallingMan.ONE_ARMED_BANDIT_SCREEN:
                dispose();
                game.setScreen(new OneArmedBanditScreen(game));
                break;
            case FallingMan.PLAY_SCREEN:
                PlayerVectors playerVectors = new PlayerVectors(player, true);
                playerVectors.calculatePlayerVectors();
                dispose();
                game.setScreen(new PlayScreen(game, playerVectors));
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
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }

    @Override
    public TextureAtlas getAtlas() {
        return atlas;
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

    public void generateNewMap() {

        //transforming player position to new map
        Vector2 playerPosPrevious = new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);
        player.updateBodyParts();

        //transforming rocks position to new map
        for (Rock rock : rocks) {
            rock.generateMapRockUpdate(playerPosPrevious);
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
        saveData = new SaveData();
        goldAndHighScoresBackground = new GoldAndHighScoresBackground(this, world);
        goldAndHighScoresIcons = new GoldAndHighScoresIcons(this, world, saveData.getGold(), saveData.getHighScore());
    }
}
