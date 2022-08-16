package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.beams;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;

public class ChangingPosBeam extends Sprite implements InteractiveObjectInterface {
    private World world;
    private Rectangle bounds;
    private PlayScreen playScreen;
    private boolean touched;
    private Fixture fixture;
    private Body body;
    private boolean toRemove;
    private int beamNumber;
    private Vector2 endMovingPos;
    private Vector2 startMovingPos;
    private BeamRoute beamRoute;

    public ChangingPosBeam(World world, Rectangle bounds, PlayScreen playScreen, int beamNumber) {
        this.world = world;
        this.bounds = bounds;
        this.playScreen = playScreen;
        this.beamNumber = beamNumber;
        startMovingPos = new Vector2(bounds.x, bounds.y);

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.KinematicBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / FallingMan.PPM, (bounds.getY() + bounds.getHeight() / 2) / FallingMan.PPM);
        body = world.createBody(bdef);
        shape.setAsBox((bounds.getWidth() / 2)  / FallingMan.PPM, (bounds.getHeight() / 2)  / FallingMan.PPM);
        fdef.shape = shape;
        fdef.density = bounds.getWidth() / 256 / 20;
        fdef.filter.categoryBits = FallingMan.WALL_INSIDE_TOWER;
        fixture = body.createFixture(fdef);
        touched = false;

        setBounds(0, 0, 256 / FallingMan.PPM, 32 / FallingMan.PPM);
        setRegion(new TextureRegion(playScreen.getDefaultAtlas().findRegion("moving_beam"), 0, 0, 256, 32));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setScale(bounds.getWidth() / 256, getScaleY());
        toRemove = false;
    }

    @Override
    public void setTouched(boolean touched) {

    }

    @Override
    public void touched() {

    }

    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void draw(Batch batch) {
        beamRoute.draw(batch);
        super.draw(batch);
    }

    @Override
    public void update(float dt) {
        if (playScreen.isGameOver()
                && (body.getPosition().y > playScreen.getPlayer().b2body.getPosition().y - 1000 / FallingMan.PPM
                && body.getPosition().y < playScreen.getPlayer().b2body.getPosition().y + 1000 / FallingMan.PPM)) {
            toRemove = true;
            playScreen.getB2WorldCreator().getInteractiveTileObjects().removeValue(this, false);
            playScreen.getB2WorldCreator().getB2bodies().removeValue(body, false);
            world.destroyBody(body);
        } else {
            if (body.getLinearVelocity().x < 0) {
                if (body.getPosition().x - ((256 / FallingMan.PPM) * getScaleX()) / 2 < (Math.min(endMovingPos.x / FallingMan.PPM, startMovingPos.x / FallingMan.PPM))) {
                    body.setLinearVelocity(-body.getLinearVelocity().x, -body.getLinearVelocity().y);
                }
            } else if (body.getLinearVelocity().x > 0){
                if (body.getPosition().x - ((256 / FallingMan.PPM) * getScaleX()) / 2 > (Math.max(endMovingPos.x / FallingMan.PPM, startMovingPos.x / FallingMan.PPM))) {
                    body.setLinearVelocity(-body.getLinearVelocity().x, -body.getLinearVelocity().y);
                }
            } else if (body.getLinearVelocity().y < 0) {
                    if (body.getPosition().y - ((32 / FallingMan.PPM) * getScaleY()) / 2 < (Math.min(endMovingPos.y / FallingMan.PPM, startMovingPos.y / FallingMan.PPM))) {
                        body.setLinearVelocity(-body.getLinearVelocity().x, -body.getLinearVelocity().y);
                    }
                } else if (body.getLinearVelocity().y > 0){
                    if (body.getPosition().y - ((32 / FallingMan.PPM) * getScaleY()) / 2 > (Math.max(endMovingPos.y / FallingMan.PPM, startMovingPos.y / FallingMan.PPM))) {
                        body.setLinearVelocity(-body.getLinearVelocity().x, -body.getLinearVelocity().y);
                    }
            }
        }


        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(body.getAngle()));
    }

    @Override
    public boolean isToRemove() {
        return toRemove;
    }

    @Override
    public void setChangeDirection(boolean changeDirection) {

    }

    public int getBeamNumber() {
        return beamNumber;
    }

    public Vector2 getEndMovingPos() {
        return endMovingPos;
    }

    public void setEndMovingPos(Vector2 endMovingPos) {
        this.endMovingPos = endMovingPos;
        body.setLinearVelocity(new Vector2(
                (getEndMovingPos().x - startMovingPos.x) / 100,
                (getEndMovingPos().y - startMovingPos.y) / 100
        ));
        beamRoute = new BeamRoute(playScreen,
                startMovingPos,
                endMovingPos,
                new Vector2(getX(), getY()),
                getScaleX());
    }
}
