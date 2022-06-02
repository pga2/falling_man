package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.scenes.HUD;
import com.ledzinygamedevelopment.fallingman.screens.windows.DefaultWindow;
import com.ledzinygamedevelopment.fallingman.sprites.fallingfromwallsobjects.Rock;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.PlayAgainButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObject;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OneArmBandit;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.Roll;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.tools.B2WorldCreator;
import com.ledzinygamedevelopment.fallingman.tools.PlayerVectors;
import com.ledzinygamedevelopment.fallingman.tools.WorldContactListener;

import java.util.LinkedList;
import java.util.Random;

public class PlayScreen implements GameScreen {

    private HUD hud;
    private FallingMan game;
    private TextureAtlas atlas;

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
    private boolean spinState;
    private int numberOfSpins;
    private float loseOneArmedBanditEndTime;
    private boolean loseOneArmedBandit;

    //Rocks falling from sky
    private Array<Rock> rocks;

    private LinkedList<Float> allFPSData;

    private Array<DefaultWindow> defaultWindows;
    private boolean loadNewGame;

    public PlayScreen(FallingMan game, PlayerVectors playerVectors) {
        atlas = new TextureAtlas("player.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("untitled1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);
        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2) / FallingMan.PPM, 0);

        world = new World(new Vector2(0, -4f), true);
        b2dr = new Box2DDebugRenderer();

        b2WorldCreator = new B2WorldCreator(this, world, map);
        //creating player
        player = new Player(world, this);
        playerVectors.setNewPlayerVectorsFromPreviusMap(player);
        gameOver = false;
        world.setContactListener(new WorldContactListener(player, this));

        //createOneArmedBanditObjects();
        rolls = new Array<>();
        oneArmBandits = new Array<>();
        buttons = new Array<>();
        smallRolls = new Array<>();
        spinState = false;
        loseOneArmedBanditEndTime = 0;
        loseOneArmedBandit = false;
        //Rocks falling from sky
        rocks = new Array<>();
        for (int i = 0; i < 200; i++) {
            rocks.add(new Rock(this, world));
        }
        allFPSData = new LinkedList<>();
        //Gdx.app.log("roll y", String.valueOf(roll.getX()));
        //gameCam.zoom = 5;
        hud = new HUD(game.batch, this);

        defaultWindows = new Array<>();
        loadNewGame = false;
        //gameCam.zoom = 0.3f;

        //player.createHeadJoint();
        //player.b2body.applyLinearImpulse(new Vector2(100f, 0f), player.b2body.getWorldCenter(), true);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
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
                    if(button.getClass().equals(SpinButton.class)) {
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
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 8, 5);

        if (player.b2body.getPosition().y < FallingMan.MAX_WORLD_HEIGHT * 0.6f / FallingMan.PPM) {
            generateNewMap();
        }
        for (InteractiveTileObject interactiveTileObject : b2WorldCreator.getInteractiveTileObjects()) {
            if (interactiveTileObject.isTouched()) {
                interactiveTileObject.touched();
                interactiveTileObject.setTouched(false);
            }
        }
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


        player.update(dt);
        hud.update(dt, player.b2body.getPosition().y);
        Vector2 playerPos = new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);
        for (Rock rock : rocks) {
            rock.update(dt, playerPos, spinState);
        }

        //oneArmBandit.update(dt);
        gameCam.position.y = player.b2body.getPosition().y;

        gameCam.update();
        renderer.setView(gameCam);

        if (gameOver) {
            if (defaultWindows.size == 0) {
                defaultWindows.add(new DefaultWindow(this, world));
                player.createHeadJoint();
                hud.newStageGameOver();
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

        //render box2d debug renderer
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Rock rock : rocks) {
            rock.draw(game.batch);
        }
        for (PlayerBodyPart bodyPart : player.getBodyParts()) {
            bodyPart.draw(game.batch);
        }
        for (Roll roll : rolls) {

            roll.draw(game.batch);
        }
        for (OnePartRoll smallRoll : smallRolls) {
            smallRoll.draw(game.batch);
        }
        for (OneArmBandit oneArmBandit : oneArmBandits) {
            oneArmBandit.draw(game.batch);
        }
        for (DefaultWindow window : defaultWindows) {
            window.draw(game.batch);
        }
        for (Button button : buttons) {
            button.draw(game.batch);
        }

        game.batch.end();
        game.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();

        if (loadNewGame) {
            dispose();
            game.setScreen(new PlayScreen(game, new PlayerVectors(player, false)));
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }

    public void generateNewMap() {

        //generating new map
        String mapName = "untitled" + new Random().nextInt(2) + ".tmx";
        for (Body body : b2WorldCreator.getB2bodies()) {
            world.destroyBody(body);
        }
        map = mapLoader.load(mapName);
        b2WorldCreator = new B2WorldCreator(this, world, map);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);

        //transforming player position to new map
        Vector2 playerPosPrevious = new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);
        player.updateBodyParts();

        for (Rock rock : rocks) {
            rock.generateMapRockUpdate(playerPosPrevious);
        }
        hud.setPreviousDist(hud.getPreviousDist() + hud.getDistance());
        hud.setDistance(0);
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
                                hud.setGold(hud.getGold() + new Random().nextInt(4000) + 8000);
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
        player.setRemoveHeadJointsAndButton(true);
        smallRolls = new Array<>();
        rolls = new Array<>();
        spinState = false;
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

    public void setSpinState(boolean spinState) {
        this.spinState = spinState;
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

    public Array<Button> getButtons() {
        return buttons;
    }
}
