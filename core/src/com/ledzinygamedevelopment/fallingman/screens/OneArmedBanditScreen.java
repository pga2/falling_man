package com.ledzinygamedevelopment.fallingman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.windows.GoldAndHighScoresBackground;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OneArmBandit;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.Roll;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.SpinsAmountLine;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.SpinsBackground;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.tools.SaveData;

import java.util.Random;

public class OneArmedBanditScreen implements GameScreen {


    private byte currentScreen;
    private FallingMan game;
    private TextureAtlas atlas;
    private OrthographicCamera gameCam;
    private ExtendViewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Array<Button> buttons;
    private boolean firstTouch;
    private Vector2 previousTouchPos;
    private Vector2 lastTouchPos;

    private SaveData saveData;
    private Array<OneArmBandit> oneArmBandits;
    private Array<Roll> rolls;
    private SpinButton spinButton;
    private boolean startRolling;
    private float rollingTime;
    private Array<OnePartRoll> smallRolls;
    private boolean winOneArmedBandit;
    private float winOneArmedBanditTime;
    private boolean winOneArmedBanditScaleUp;
    private boolean createOneArmedBanditBundle;
    private int numberOfSpins;
    private float loseOneArmedBanditEndTime;
    private boolean loseOneArmedBandit;
    private Array<SpinsBackground> spinsBackgrounds;
    private Array<SpinsAmountLine> spinsAmountLines;

    public OneArmedBanditScreen(FallingMan game) {
        atlas = new TextureAtlas("player.pack");
        this.game = game;
        currentScreen = FallingMan.CURRENT_SCREEN;
        atlas = new TextureAtlas("player.pack");
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("one_armed_bandit_maps/one_armed_bandit_map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);

        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2) / FallingMan.PPM, (gamePort.getWorldHeight() * FallingMan.PPM / 2) / FallingMan.PPM, 0);
        world = new World(new Vector2(0, -4f), true);
        b2dr = new Box2DDebugRenderer();
        buttons = new Array<>();
        previousTouchPos = new Vector2();
        lastTouchPos = new Vector2();
        firstTouch = true;
        saveData = new SaveData();

        rolls = new Array<>();
        oneArmBandits = new Array<>();
        buttons = new Array<>();
        smallRolls = new Array<>();
        createOneArmedBanditBundle = true;
        loseOneArmedBanditEndTime = 0;
        loseOneArmedBandit = false;

        spinsBackgrounds = new Array<>();
        spinsAmountLines = new Array<>();
    }


    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            if (firstTouch) {
                previousTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                firstTouch = false;
            }
            // check if buttons click
            Vector2 mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            for (Button button : buttons) {
                if (button.mouseOver(mouseVector) && !button.isLocked()) {
                    button.touched();
                    button.setClicked(true);
                }
            }
            lastTouchPos = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        } else {
            if (!firstTouch) {
                if (previousTouchPos.x - lastTouchPos.x > 100 / FallingMan.PPM) {
                    currentScreen = FallingMan.MENU_SCREEN;
                }
            }
            for (Button button : buttons) {
                if (button.isClicked() && !button.isLocked()) {
                    if (button.getClass().equals(SpinButton.class)) {
                        setStartRolling(true);
                        for (Roll roll : rolls) {
                            roll.setRollingCurrently(true);
                        }
                    }
                    button.notTouched();
                    button.setClicked(false);
                }
            }
            firstTouch = true;
        }
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 8, 5);


        if (startRolling) {
            spinButton.setLocked(true);
            startRolling(dt);
        } else if (winOneArmedBandit) {
            winOneArmedBandit(dt);
        } else if (loseOneArmedBandit) {
            loseOneArmedBanditEndTime += dt;
            if (loseOneArmedBanditEndTime > 0.5f) {
                //removeOneArmedBanditbundle();
                loseOneArmedBanditEndTime = 0;
                loseOneArmedBandit = false;
            }
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

        //render box2d debug renderer
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();

        for (Roll roll : rolls) {

            roll.draw(game.batch);
        }
        for (OnePartRoll smallRoll : smallRolls) {
            smallRoll.draw(game.batch);
        }
        for (SpinsBackground spinsBackground : spinsBackgrounds) {
            spinsBackground.draw(game.batch);
        }
        for (SpinsAmountLine spinsAmountLine : spinsAmountLines) {
            spinsAmountLine.draw(game.batch);
        }
        for (OneArmBandit oneArmBandit : oneArmBandits) {
            oneArmBandit.draw(game.batch);
        }
        for (Button button : buttons) {
            button.draw(game.batch);
        }
        game.batch.end();

        switch (currentScreen) {
            case FallingMan.MENU_SCREEN:
                dispose();
                game.setScreen(new MenuScreen(game));
                break;
        }

        if (createOneArmedBanditBundle) {
            createOneArmedBanditObjects();
            createOneArmedBanditBundle = false;
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

    public void startRolling(float dt) {
        Random random = new Random();
        for (int i = 0; i < rolls.size; i++) {
            Roll roll = rolls.get(i);
            if (roll.isRollingCurrently()) {
                int nextRollPosDiffY = (random.nextInt(20) + 40);
                if (roll.getY() - nextRollPosDiffY / FallingMan.PPM > gamePort.getWorldHeight() / 2) {

                    roll.setPosition(roll.getX(), roll.getY() - nextRollPosDiffY / FallingMan.PPM);
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
                                spinButton.setLocked(true);
                                winOneArmedBandit = true;
                                smallRolls.add(new OnePartRoll(this, 300 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 424 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                smallRolls.add(new OnePartRoll(this, 620 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 424 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                smallRolls.add(new OnePartRoll(this, 950 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 424 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                rolls = new Array<>();
                            } else {
                                spinButton.setLocked(false);
                                saveData.removeSpin();
                                if (saveData.getNumberOfSpins() <= 0) {
                                    loseOneArmedBandit = true;
                                    //removeButton(spinButton);
                                    spinButton.setLocked(true);
                                }
                            }
                            spinsAmountLines.get(0).setScale((float) (Math.min(saveData.getNumberOfSpins(), 50)) * 1.0057f, 1);
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
            if (winOneArmedBanditTime > 3) {
                winOneArmedBanditTime = 0;
                winOneArmedBandit = false;
                break;
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
            }
        }
        if (!winOneArmedBandit) {
            int sizeOfSmallRolls = smallRolls.size;
            for (int i = 0; i < sizeOfSmallRolls; i++) {
                OnePartRoll smallRoll = smallRolls.get(0);
                if (!(saveData.getNumberOfSpins() <= 0)) {
                    spinButton.setLocked(false);
                }
                rolls.add(new Roll(this, world, smallRoll.getX(), gamePort.getWorldHeight() / 2 + 424 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM, smallRoll.getCurrentTextureNumber()));
                smallRolls.removeValue(smallRoll, false);
            }
        } else {
            winOneArmedBanditTime += dt;
        }
    }

    public void removeOneArmedBanditbundle() {
        winOneArmedBanditTime = 0;
        oneArmBandits = new Array<>();
        smallRolls = new Array<>();
        rolls = new Array<>();
        createOneArmedBanditBundle = false;
        startRolling = false;
    }

    public void createOneArmedBanditObjects() {
        oneArmBandits.add(new OneArmBandit(this, world, gamePort.getWorldHeight() / 2));
        spinsBackgrounds.add(new SpinsBackground(this, world, oneArmBandits.get(0).getX() + 50 / FallingMan.PPM, oneArmBandits.get(0).getY() + 103 / FallingMan.PPM));
        spinsAmountLines.add(new SpinsAmountLine(this, world, oneArmBandits.get(0).getX() + 50 / FallingMan.PPM, oneArmBandits.get(0).getY() + 103 / FallingMan.PPM));
        spinsAmountLines.get(0).setScale((float) (Math.min(saveData.getNumberOfSpins(), 50)) * 1.0057f, 1);
        buttons = new Array<>();
        spinButton = new SpinButton(this, world, 448 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 760 / FallingMan.PPM, 544 / FallingMan.PPM, 192 / FallingMan.PPM);
        buttons.add(spinButton);
        startRolling = false;
        rollingTime = 0;
        Gdx.app.log("world height", String.valueOf(gamePort.getScreenHeight()));
        Roll roll1 = new Roll(this, world, 300 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 424 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM);
        Roll roll2 = new Roll(this, world, 620 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 424 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM);
        Roll roll3 = new Roll(this, world, 950 / FallingMan.PPM, gamePort.getWorldHeight() / 2 + 424 / FallingMan.PPM, 192 / FallingMan.PPM, 576 / FallingMan.PPM);
        rolls = new Array<>();
        rolls.add(roll1);
        rolls.add(roll2);
        rolls.add(roll3);
        winOneArmedBandit = false;
        winOneArmedBanditTime = 0;
        winOneArmedBanditScaleUp = true;

        smallRolls = new Array<>();

        if (saveData.getNumberOfSpins() <= 0) {
            spinButton.setLocked(true);
        }
    }

    @Override
    public TextureAtlas getAtlas() {
        return atlas;
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

    public void setStartRolling(boolean startRolling) {
        this.startRolling = startRolling;
    }

    public void removeButton(Button button) {
        buttons.removeValue(button, false);
    }

    public void setNumberOfSpins(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

}
