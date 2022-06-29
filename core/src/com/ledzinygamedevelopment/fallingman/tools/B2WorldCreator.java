package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.Gdx;
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
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.BodyPartsRestorer;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coin.Coin;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.DeadMachine;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObject;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.Spins;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.teleports.Teleport;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.teleports.TeleportTarget;

import java.util.Calendar;

public class B2WorldCreator {


    private SpinButton button;
    private Array<Body> b2bodies;
    private Array<InteractiveTileObject> interactiveTileObjects;
    private Array<Coin> coins;
    private Array<TeleportTarget> teleportsTarget;

    public B2WorldCreator(PlayScreen playScreen, World world, TiledMap map) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        b2bodies = new Array<>();
        interactiveTileObjects = new Array<>();
        teleportsTarget = new Array<>();

        //walls
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / FallingMan.PPM, (rect.getY() + rect.getHeight() / 2) / FallingMan.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2)  / FallingMan.PPM, (rect.getHeight() / 2)  / FallingMan.PPM);
            fdef.shape = shape;
            //
            body.createFixture(fdef);
            b2bodies.add(body);
        }

        //walls inside tower
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / FallingMan.PPM, (rect.getY() + rect.getHeight() / 2) / FallingMan.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2)  / FallingMan.PPM, (rect.getHeight() / 2)  / FallingMan.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = FallingMan.WALL_INSIDE_TOWER;
            body.createFixture(fdef);
            b2bodies.add(body);
        }
        //coins
        coins = new Array<>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if (object.getProperties().get("tp") != null) {
                Gdx.app.log("tp in b2world creator", String.valueOf(object.getProperties().get("tp")));
                InteractiveTileObject teleport = new Teleport(world, map, rect, 2, playScreen, Integer.parseInt(String.valueOf(object.getProperties().get("tp"))));
                b2bodies.add(teleport.getBody());
                interactiveTileObjects.add(teleport);
            } else if (object.getProperties().get("tp_target") != null) {
                InteractiveTileObject teleportTarget = new TeleportTarget(world, map, rect, 2, playScreen, Integer.parseInt(String.valueOf(object.getProperties().get("tp_target"))));
                b2bodies.add(teleportTarget.getBody());
                interactiveTileObjects.add(teleportTarget);
                teleportsTarget.add((TeleportTarget) teleportTarget);
            } else {
                Coin coin = new Coin(world, map, rect, 2, playScreen);
                b2bodies.add(coin.getBody());
                coins.add(coin);
            }
        }

        //deadly things
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject deadMachine = new DeadMachine(world, map, rect, 3);
            b2bodies.add(deadMachine.getBody());
            interactiveTileObjects.add(deadMachine);
        }

        //spins
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject spins = new Spins(playScreen, world, map, rect, 3);
            b2bodies.add(spins.getBody());
            interactiveTileObjects.add(spins);
        }

        //restore body parts
        for(MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject bodyPartsRestorer = new BodyPartsRestorer(world, map, rect, 2, playScreen);
            b2bodies.add(bodyPartsRestorer.getBody());
            interactiveTileObjects.add(bodyPartsRestorer);
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

    public Array<Coin> getCoins() {
        return coins;
    }

    public Array<TeleportTarget> getTeleportsTarget() {
        return teleportsTarget;
    }
}
