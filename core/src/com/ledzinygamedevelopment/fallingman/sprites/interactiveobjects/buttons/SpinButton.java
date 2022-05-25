package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ledzinygamedevelopment.fallingman.FallingMan;

public class SpinButton extends Button{


    public SpinButton(Rectangle bounds, World world, TiledMap map, int mapLayer) {
        super(bounds, world, map, mapLayer);
    }

    @Override
    public void defineButton(Rectangle bounds) {

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((bounds.getWidth() / 2)  / FallingMan.PPM, (bounds.getHeight() / 2)  / FallingMan.PPM);

        bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / FallingMan.PPM, (bounds.getY() + bounds.getHeight() / 2) / FallingMan.PPM);

        b2body = world.createBody(bdef);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = FallingMan.SPIN_BIT;
        fdef.filter.maskBits = FallingMan.TOUCHED_POINT_BIT;
        //fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public Body getB2body() {
        return b2body;
    }


}
