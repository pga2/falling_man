package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.Coin;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.DeadMachine;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObject;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.Spins;

public class B2WorldCreator {

    private SpinButton button;
    private Array<Body> b2bodies;
    private Array<InteractiveTileObject> interactiveTileObjects;

    public B2WorldCreator(PlayScreen playScreen, World world, TiledMap map) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        b2bodies = new Array<>();
        interactiveTileObjects = new Array<>();

        //walls
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / FallingMan.PPM, (rect.getY() + rect.getHeight() / 2) / FallingMan.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2)  / FallingMan.PPM, (rect.getHeight() / 2)  / FallingMan.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
            b2bodies.add(body);
        }
        //coins
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject coin = new Coin(world, map, rect, 2);
            b2bodies.add(coin.getBody());
            interactiveTileObjects.add(coin);
        }

        //deadly things
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject deadMachine = new DeadMachine(world, map, rect, 3);
            b2bodies.add(deadMachine.getBody());
            interactiveTileObjects.add(deadMachine);
        }

        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject spins = new Spins(playScreen, world, map, rect, 3);
            b2bodies.add(spins.getBody());
            interactiveTileObjects.add(spins);
        }

        /*//one-armed bandit spin button
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            button = new SpinButton(rect, world, map, 1);
        }*/
    }

    public Array<Body> getB2bodies() {
        return b2bodies;
    }

    public SpinButton getButton() {
        return button;
    }

    public Array<InteractiveTileObject> getInteractiveTileObjects() {
        return interactiveTileObjects;
    }
}
