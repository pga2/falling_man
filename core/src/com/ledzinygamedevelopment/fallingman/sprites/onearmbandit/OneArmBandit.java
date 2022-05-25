package com.ledzinygamedevelopment.fallingman.sprites.onearmbandit;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class OneArmBandit extends Sprite {
    private TextureRegion playerHeadTexture;
    private PlayScreen playScreen;
    private World world;
    private BodyDef bdef;
    private Body b2body;
    private FixtureDef fdef;

    public OneArmBandit(PlayScreen playScreen, World world) {
        super(playScreen.getAtlas().findRegion("one-armed_bandit"));
        this.playScreen = playScreen;
        this.world = world;
        playerHeadTexture = new TextureRegion(getTexture(), 0, 0, 1056, 416);
        setBounds(0, 0, 1056 / FallingMan.PPM, 416 / FallingMan.PPM);
        setRegion(playerHeadTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
        defineBody();
    }

    public void defineBody() {
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
    }
}
