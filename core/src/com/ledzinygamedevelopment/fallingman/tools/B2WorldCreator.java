package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.WalkingEnemy;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.dragon.Dragon;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.SpiderWeb;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.beams.ChangingPosBeam;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.BodyPartsRestorer;
import com.ledzinygamedevelopment.fallingman.sprites.enemies.Spikes;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.HuntingEnemyCreator;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveObjectInterface;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.beams.MovingBeam;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coins.Coin;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.coins.Spin;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.teleports.Teleport;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.teleports.TeleportTarget;

public class B2WorldCreator {


    private SpinButton button;
    private Array<Body> b2bodies;
    private Array<InteractiveObjectInterface> interactiveTileObjects;
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
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / FallingMan.PPM, (rect.getY() + rect.getHeight() / 2) / FallingMan.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / FallingMan.PPM, (rect.getHeight() / 2) / FallingMan.PPM);
            fdef.shape = shape;
            fdef.shape.setRadius(shape.getRadius());
            //
            body.createFixture(fdef);
            b2bodies.add(body);
        }

        //polygon walls
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(PolygonMapObject.class)) {
            bdef = new BodyDef();
            PolygonShape polygonShape = new PolygonShape();
            float[] vertices = ((PolygonMapObject) object).getPolygon().getTransformedVertices();

            float[] worldVertices = new float[vertices.length];

            for (int i = 0; i < vertices.length; i++) {
                worldVertices[i] = vertices[i] / FallingMan.PPM;
            }
            polygonShape.set(worldVertices);

            bdef.type = BodyDef.BodyType.StaticBody;


            body = world.createBody(bdef);
            fdef = new FixtureDef();
            fdef.shape = polygonShape;
            body.createFixture(fdef);
            b2bodies.add(body);
        }

        //walls inside tower
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / FallingMan.PPM, (rect.getY() + rect.getHeight() / 2) / FallingMan.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / FallingMan.PPM, (rect.getHeight() / 2) / FallingMan.PPM);
            fdef.shape = shape;
            if (object.getProperties().get("t") != null) {
                fdef.filter.categoryBits = FallingMan.WALL_INSIDE_TOWER;
                fdef.filter.maskBits = FallingMan.INTERACTIVE_TILE_OBJECT_BIT;
            } else {
                fdef.filter.categoryBits = FallingMan.WALL_INSIDE_TOWER;
            }
            body.createFixture(fdef);
            b2bodies.add(body);
        }

        //coins
        coins = new Array<>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if (object.getProperties().get("tp") != null) {
                InteractiveObjectInterface teleport = new Teleport(world, map, rect, 2, playScreen, Integer.parseInt(String.valueOf(object.getProperties().get("tp"))));
                b2bodies.add(teleport.getBody());
                interactiveTileObjects.add(teleport);
            } else if (object.getProperties().get("tp_target") != null) {
                InteractiveObjectInterface teleportTarget = new TeleportTarget(world, map, rect, 2, playScreen, Integer.parseInt(String.valueOf(object.getProperties().get("tp_target"))));
                b2bodies.add(teleportTarget.getBody());
                interactiveTileObjects.add(teleportTarget);
                teleportsTarget.add((TeleportTarget) teleportTarget);
            } else {
                InteractiveObjectInterface coin = new Coin(world, map, rect, 2, playScreen);
                b2bodies.add(coin.getBody());
                interactiveTileObjects.add(coin);
            }
        }

        //deadly things
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveObjectInterface deadMachine = new Spikes(playScreen, world, map, rect, 3);
            b2bodies.add(deadMachine.getBody());
            interactiveTileObjects.add(deadMachine);
        }

        //moving beams
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            MovingBeam movingBeam = new MovingBeam(world, rect, playScreen);
            b2bodies.add(movingBeam.getBody());
            b2bodies.add(movingBeam.getHeadHolderBody());
            interactiveTileObjects.add(movingBeam);

        }

        //walking enemy
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            WalkingEnemy walkingEnemy = new WalkingEnemy(playScreen, world, (rect.x + rect.getWidth() / 2) / FallingMan.PPM, rect.y / FallingMan.PPM, (boolean) object.getProperties().get("right"));
            b2bodies.add(walkingEnemy.getBody());
            interactiveTileObjects.add(walkingEnemy);

        }

        //spins
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();


            InteractiveObjectInterface spin = new Spin(world, map, rect, 2, playScreen);
            b2bodies.add(spin.getBody());
            interactiveTileObjects.add(spin);
            /*InteractiveObjectInterface spins = new Spins(playScreen, world, map, rect, 3);
            b2bodies.add(spins.getBody());
            interactiveTileObjects.add(spins);*/
        }

        //restore body parts
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveObjectInterface bodyPartsRestorer = new BodyPartsRestorer(world, map, rect, 2, playScreen);
            b2bodies.add(bodyPartsRestorer.getBody());
            interactiveTileObjects.add(bodyPartsRestorer);
        }

        //dragons
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveObjectInterface dragon = new Dragon(world, rect, playScreen, Boolean.parseBoolean(String.valueOf(object.getProperties().get("right_side_fire"))));
            b2bodies.add(dragon.getBody());
            interactiveTileObjects.add(dragon);
        }

        //hunter
        for (MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveObjectInterface huntingEnemyCreator = new HuntingEnemyCreator(world, map, rect, 3, playScreen);
            b2bodies.add(huntingEnemyCreator.getBody());
            interactiveTileObjects.add(huntingEnemyCreator);
        }

        //changing pos beam
        Array<ChangingPosBeam> changingPosBeamsToConnect = new Array<>();
        Array<Vector3> beamTargets = new Array<>();
        for (MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if (object.getProperties().get("b_target") != null) {
                beamTargets.add(new Vector3(rect.x, rect.y, (int) object.getProperties().get("b_target")));
            } else {
                ChangingPosBeam changingPosBeam = new ChangingPosBeam(world, rect, playScreen, object.getProperties().get("b") != null ? (int) object.getProperties().get("b") : -1);
                b2bodies.add(changingPosBeam.getBody());
                interactiveTileObjects.add(changingPosBeam);
                changingPosBeamsToConnect.add(changingPosBeam);
            }
        }
        for (ChangingPosBeam changingPosBeam : changingPosBeamsToConnect) {
            for (Vector3 beamTarget : beamTargets) {
                if (beamTarget.z == changingPosBeam.getBeamNumber()) {
                    changingPosBeam.setEndMovingPos(new Vector2(beamTarget.x, beamTarget.y));
                }
            }
        }
        for (ChangingPosBeam changingPosBeam : changingPosBeamsToConnect) {
            if (changingPosBeam.getEndMovingPos() == null) {
                interactiveTileObjects.removeValue(changingPosBeam, false);
                b2bodies.removeValue(changingPosBeam.getBody(), false);
                world.destroyBody(changingPosBeam.getBody());
            }
        }

        //spider webs
        for (MapObject object : map.getLayers().get(15).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveObjectInterface spiderWeb = new SpiderWeb(playScreen, world, map, rect, 3);
            b2bodies.add(spiderWeb.getBody());
            interactiveTileObjects.add(spiderWeb);
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

    public Array<InteractiveObjectInterface> getInteractiveTileObjects() {
        return interactiveTileObjects;
    }

    public Array<Coin> getCoins() {
        return coins;
    }

    public Array<TeleportTarget> getTeleportsTarget() {
        return teleportsTarget;
    }
}
