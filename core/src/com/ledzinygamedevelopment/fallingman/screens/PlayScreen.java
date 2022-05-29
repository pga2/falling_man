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
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.Button;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObject;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OneArmBandit;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OnePartRoll;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.Roll;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;
import com.ledzinygamedevelopment.fallingman.tools.B2WorldCreator;
import com.ledzinygamedevelopment.fallingman.tools.WorldContactListener;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Random;

public class PlayScreen implements Screen {

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
    private boolean temp;

    public PlayScreen(FallingMan game) {
        atlas = new TextureAtlas("player.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("untitled1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);
        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2)  / FallingMan.PPM, 0);

        world = new World(new Vector2(0, -4f), true);
        b2dr = new Box2DDebugRenderer();

        b2WorldCreator = new B2WorldCreator(this, world, map);
        //creating player
        player = new Player(world, this);
        world.setContactListener(new WorldContactListener(player));

        //createOneArmedBanditObjects();
        rolls = new Array<>();
        oneArmBandits = new Array<>();
        buttons = new Array<>();
        smallRolls = new Array<>();
        //Gdx.app.log("roll y", String.valueOf(roll.getX()));
        //gameCam.zoom = 5;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        //pc
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -5) {
            player.getBelly().getB2body().applyLinearImpulse(new Vector2(-0.5f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 5) {
            player.getBelly().getB2body().applyLinearImpulse(new Vector2(0.5f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.getBelly().getB2body().applyLinearImpulse(new Vector2(0f, 0.5f), player.getBelly().getB2body().getWorldCenter(), true);
        }

        //mobile
        if(Gdx.input.isTouched()) {

            // check if buttons click
            Vector2 mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            for (Button button : buttons) {
                if(button.mouseOver(mouseVector) && !button.isLocked()) {
                    button.touched();
                    button.setClicked(true);
                    temp = true;
                }
            }
            System.out.println(buttons.size);


            //moving player
            if(Gdx.input.getX() < Gdx.graphics.getWidth() / 2f) {
                player.getBelly().getB2body().applyLinearImpulse(new Vector2(-0.5f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
            } else {
                player.getBelly().getB2body().applyLinearImpulse(new Vector2(0.5f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
            }
        } else {
            for(Button button : buttons) {
                if(button.isClicked() && !button.isLocked()) {
                    //player.setRemoveHeadJointsAndButton(true);

                    setStartRolling(true);
                    for(Roll roll : rolls) {
                        roll.setRollingCurrently(true);
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
        world.step(1/60f, 8, 5);

        if(player.b2body.getPosition().y < FallingMan.MAX_WORLD_HEIGHT * 0.6f / FallingMan.PPM) {
            generateNewMap();
        }
        for(InteractiveTileObject interactiveTileObject : b2WorldCreator.getInteractiveTileObjects()) {
            if (interactiveTileObject.isTouched()) {
                interactiveTileObject.touched();
            }
        }
        if(startRolling) {
            spinButton.setLocked(true);
            startRolling(dt);
        } else if(winOneArmedBandit) {
            winOneArmedBandit(dt);
        }



        player.update(dt);
        //oneArmBandit.update(dt);
        gameCam.position.y = player.b2body.getPosition().y;

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
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(PlayerBodyPart bodyPart : player.getBodyParts()) {
            bodyPart.draw(game.batch);
        }
        for(Roll roll : rolls) {

            roll.draw(game.batch);
        }
        for(OnePartRoll smallRoll : smallRolls) {
            smallRoll.draw(game.batch);
        }
        for(OneArmBandit oneArmBandit : oneArmBandits) {
            oneArmBandit.draw(game.batch);
        }
        for(Button button : buttons) {
            button.draw(game.batch);
        }
        game.batch.end();
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
        for(Body body : b2WorldCreator.getB2bodies()) {
            world.destroyBody(body);
        }
        map = mapLoader.load(mapName);
        b2WorldCreator = new B2WorldCreator(this, world, map);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);

        //transforming player position to new map
        player.updateBodyParts();
    }

    public void startRolling(float dt) {
        Random random = new Random();
        for(int i = 0; i < rolls.size; i++) {
            Roll roll = rolls.get(i);
            if(roll.isRollingCurrently()) {
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
                        if(i == 2) {
                            int figureNumber = rolls.get(0).getCurrentTextureNumber();
                            boolean allFiguresTheSame = true;
                            for(Roll rollFigureCheck : rolls) {
                                if(figureNumber != rollFigureCheck.getCurrentTextureNumber()) {
                                    allFiguresTheSame = false;
                                }
                            }
                            if(allFiguresTheSame) {
                                removeButton(spinButton);
                                winOneArmedBandit = true;
                                smallRolls.add(new OnePartRoll(this, 300 / FallingMan.PPM, player.b2body.getPosition().y - 1040 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                smallRolls.add(new OnePartRoll(this, 620 / FallingMan.PPM, player.b2body.getPosition().y - 1040 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                smallRolls.add(new OnePartRoll(this, 950 / FallingMan.PPM, player.b2body.getPosition().y - 1040 / FallingMan.PPM, 192 / FallingMan.PPM, 192 / FallingMan.PPM, rolls.get(0).getCurrentTextureNumber()));
                                rolls = new Array<>();
                            } else {
                                spinButton.setLocked(false);
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
            if(winOneArmedBanditTime > 10) {
                winOneArmedBanditTime = 0;
                oneArmBandits = new Array<>();
                player.setRemoveHeadJointsAndButton(true);
                smallRolls = new Array<>();
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
}
