package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coins.Spark;

public class HuntingEnemyCreator extends Sprite implements InteractiveObjectInterface  {


    private final PlayScreen playScreen;
    private Body body;
    private Fixture fixture;
    private FixtureDef fdef;
    private boolean touched;
    //private Animation animation;
    //private float animationTimer;
    private boolean draw;
    private boolean rotationRight;

    public HuntingEnemyCreator(World world, TiledMap map, Rectangle bounds, int mapLayer, PlayScreen playScreen) {

        /*Array<TextureRegion> textureRegions = new Array<>();
        for (int i = 1; i < 9; i++) {
            textureRegions.add(new TextureRegion(playScreen.getDefaultAtlas().findRegion("spider_map" + i), 0, 0, 96, 96));
        }*/

        //animation = new Animation(0.1f, textureRegions);
        //animationTimer = 0.0001f;

        BodyDef bdef = new BodyDef();
        fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / FallingMan.PPM, (bounds.getY() + bounds.getHeight() / 2) / FallingMan.PPM);
        body = world.createBody(bdef);
        shape.setAsBox((bounds.getWidth() / 2)  / FallingMan.PPM, (bounds.getHeight() / 2)  / FallingMan.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;
        fixture = body.createFixture(fdef);
        touched = false;
        this.playScreen = playScreen;
        fixture.setUserData(this);
        setCategoryFilter(FallingMan.INTERACTIVE_TILE_OBJECT_BIT);

        setBounds(0, 0, 96 / FallingMan.PPM, 96 / FallingMan.PPM);
        setRegion(new TextureRegion(playScreen.getDefaultAtlas().findRegion("spider_map1"), 0, 0, 96, 96));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);

         draw = true;
         rotationRight = true;
    }

    @Override
    public void draw(Batch batch) {
        if (draw) {
            super.draw(batch);
        }
    }

    @Override
    public void update(float dt) {
        if (draw) {
            //setRegion(getFrame(dt));
            setRotation(getRotation() + 180 * dt);
        }
    }

    @Override
    public boolean isToRemove() {
        return false;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {

    }

    @Override
    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    @Override
    public void touched() {
        if (playScreen.getSaveData().getSounds()) {
            playScreen.getAssetManager().getTouchSpiderSound().play();
        }
        draw = false;
        for (int i = 0; i < 50; i++) {
            playScreen.getSparks().add(new Spark(playScreen, body.getPosition().x, body.getPosition().y, (byte) 3, false));
        }
        setCategoryFilter(FallingMan.DESTROYED_BIT);
        /*for(TiledMapTileLayer.Cell cell : getCells()) {
            try {
                cell.setTile(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        playScreen.addHuntingSpiderAI(new Vector2(getBody().getPosition().x, getBody().getPosition().y + (FallingMan.MAX_WORLD_HEIGHT * 0.5f) / FallingMan.PPM + 128 / FallingMan.PPM));
    }

    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public Body getBody() {
        return body;
    }


    /*private TextureRegion getFrame(float dt) {
        animationTimer += dt;
        return (TextureRegion) animation.getKeyFrame(animationTimer, true);
    }*/


    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
