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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.Coin;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.onearmbandit.OneArmBandit;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.tools.B2WorldCreator;
import com.ledzinygamedevelopment.fallingman.tools.WorldContactListener;

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

    //touch sensor
    private Body touchSensorKinematicBody;
    private Fixture touchSensorKinematicFixture;
    private Body touchSensorDynamicBody;
    private Fixture touchSensorDynamicFixture;

    private  OneArmBandit oneArmBandit;

    public PlayScreen(FallingMan game) {
        atlas = new TextureAtlas("player.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new ExtendViewport(FallingMan.MIN_WORLD_WIDTH / FallingMan.PPM, FallingMan.MIN_WORLD_HEIGHT / FallingMan.PPM,
                FallingMan.MAX_WORLD_WIDTH / FallingMan.PPM, FallingMan.MAX_WORLD_HEIGHT / FallingMan.PPM, gameCam);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("untitled0.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);
        gameCam.position.set((FallingMan.MIN_WORLD_WIDTH / 2) / FallingMan.PPM, (FallingMan.MIN_WORLD_HEIGHT / 2)  / FallingMan.PPM, 0);

        world = new World(new Vector2(0, -4f), true);
        b2dr = new Box2DDebugRenderer();

        b2WorldCreator = new B2WorldCreator(world, map);
        createTouchSensor();
        //creating player
        player = new Player(world, this);
        oneArmBandit = new OneArmBandit(this, world);
        world.setContactListener(new WorldContactListener(player));
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

            // setting touched point to collide with buttons
            setCategoryTouchSensorFilter(FallingMan.TOUCHED_POINT_BIT);
            Vector2 mouseVector = gamePort.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            touchSensorKinematicBody.setTransform(mouseVector, 0);
            touchSensorDynamicBody.setTransform(mouseVector, 0);
            //moving player
            if(Gdx.input.getX() < Gdx.graphics.getWidth() / 2f) {
                player.getBelly().getB2body().applyLinearImpulse(new Vector2(-0.5f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
            } else {
                player.getBelly().getB2body().applyLinearImpulse(new Vector2(0.5f, 0f), player.getBelly().getB2body().getWorldCenter(), true);
            }
        } else if(touchSensorKinematicFixture.getFilterData().categoryBits != FallingMan.UNTOUCHED_POINT_BIT){ // if screen untouched then set touch sensor filter to not collide with buttons
            setCategoryTouchSensorFilter(FallingMan.UNTOUCHED_POINT_BIT);
        }
    }

    public void update(float dt) {
        handleInput(dt);
        if(player.b2body.getPosition().y < FallingMan.MAX_WORLD_HEIGHT * 0.6f / FallingMan.PPM) {
            generateNewMap();
        }

        world.step(1/60f, 8, 5);

        player.update(dt);
        oneArmBandit.update(dt);
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
        oneArmBandit.draw(game.batch);
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
        b2WorldCreator = new B2WorldCreator(world, map);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FallingMan.PPM);

        //transforming player position to new map
        player.updateBodyParts();
    }

    private void createTouchSensor() {

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        touchSensorDynamicBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / FallingMan.PPM);

        fdef.shape = shape;
        //fdef.isSensor = true;
        fdef.filter.categoryBits = FallingMan.UNTOUCHED_POINT_BIT;
        fdef.filter.maskBits = FallingMan.SPIN_BIT;
        touchSensorDynamicFixture = touchSensorDynamicBody.createFixture(fdef);

        bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.KinematicBody;
        touchSensorKinematicBody = world.createBody(bdef);

        fdef = new FixtureDef();
        shape.setRadius(10 / 100f);
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = FallingMan.UNTOUCHED_POINT_BIT;
        fdef.filter.maskBits = FallingMan.SPIN_BIT;
        touchSensorKinematicFixture = touchSensorKinematicBody.createFixture(fdef);


        RopeJointDef ropeJointDef = new RopeJointDef();
        ropeJointDef.localAnchorA.x = 0;
        ropeJointDef.localAnchorB.x = 0;
        ropeJointDef.localAnchorA.y = 0;
        ropeJointDef.localAnchorB.y = 0;
        ropeJointDef.bodyA = touchSensorDynamicBody;
        ropeJointDef.bodyB = touchSensorKinematicBody;
        ropeJointDef.maxLength = 1 / FallingMan.PPM;
        world.createJoint(ropeJointDef);

    }

    public void setCategoryTouchSensorFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        filter.maskBits = FallingMan.SPIN_BIT;
        touchSensorKinematicFixture.setFilterData(filter);

        filter = new Filter();
        filter.categoryBits = filterBit;
        filter.maskBits = FallingMan.SPIN_BIT;
        touchSensorDynamicFixture.setFilterData(filter);
    }

    public B2WorldCreator getB2WorldCreator() {
        return b2WorldCreator;
    }
}
