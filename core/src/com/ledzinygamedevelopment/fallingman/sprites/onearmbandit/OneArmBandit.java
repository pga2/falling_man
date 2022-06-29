package com.ledzinygamedevelopment.fallingman.sprites.onearmbandit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;

public class OneArmBandit extends Sprite {
    //private TextureRegion oneArmedBanditTexture;
    private GameScreen gameScreen;
    private World world;
    private BodyDef bdef;
    private Body b2body;
    private FixtureDef fdef;

    public OneArmBandit(GameScreen gameScreen, World world, float posY) {
        //super(gameScreen.getAtlas().findRegion("one_armed_bandit"));
        this.gameScreen = gameScreen;
        this.world = world;
        //oneArmedBanditTexture = new TextureRegion(getTexture(), 1315, 3033, 1056, 1040);
        setBounds(0, 0, 1056 / FallingMan.PPM, 1040 / FallingMan.PPM);

        setRegion(gameScreen.getDefaultAtlas().findRegion("one_armed_bandit"), 0, 0, 1056, 1040);
        setPosition(192 / FallingMan.PPM, posY);
        //setOrigin(getWidth() / 2, getHeight() / 2);
        //defineBody();
    }

    /*public void defineBody() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1056  / FallingMan.PPM / 2, 416  / FallingMan.PPM / 2);

        bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(192 / FallingMan.PPM + getWidth() / 2, 7488 / FallingMan.PPM + getHeight() / 2);

        b2body = world.createBody(bdef);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 0.1f;
        fdef.filter.categoryBits = FallingMan.DEFAULT_BIT;
        //fdef.filter.maskBits = FallingMan.TOUCHED_POINT_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }*/
}
