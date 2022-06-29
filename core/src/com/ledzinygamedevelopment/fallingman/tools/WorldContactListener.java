package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.GameScreen;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.DeadMachine;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObject;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects.InteractiveTileObjectInterface;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;

public class WorldContactListener implements ContactListener {

    private Player player;
    private GameScreen gameScreen;

    public WorldContactListener(Player player, GameScreen gameScreen) {

        this.player = player;
        this.gameScreen = gameScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case FallingMan.PLAYER_HEAD_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == FallingMan.PLAYER_HEAD_BIT) {
                    ((InteractiveTileObjectInterface) fixB.getUserData()).setTouched(true);
                } else {
                    ((InteractiveTileObjectInterface) fixA.getUserData()).setTouched(true);
                }
                break;
            case FallingMan.PLAYER_HEAD_BIT | FallingMan.DEAD_MACHINE_BIT:
                if (fixA.getFilterData().categoryBits == FallingMan.PLAYER_HEAD_BIT) {
                    ((DeadMachine) fixB.getUserData()).touched();
                } else {
                    ((DeadMachine) fixA.getUserData()).touched();
                }
                break;
            case FallingMan.PLAYER_BELLY_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == FallingMan.PLAYER_BELLY_BIT) {
                    ((InteractiveTileObjectInterface) fixB.getUserData()).setTouched(true);
                } else {
                    ((InteractiveTileObjectInterface) fixA.getUserData()).setTouched(true);
                }
                break;
            case FallingMan.PLAYER_ARM_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == FallingMan.PLAYER_ARM_BIT) {
                    ((InteractiveTileObjectInterface) fixB.getUserData()).setTouched(true);
                } else {
                    ((InteractiveTileObjectInterface) fixA.getUserData()).setTouched(true);
                }
                break;
            case FallingMan.PLAYER_FORE_ARM_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == FallingMan.PLAYER_FORE_ARM_BIT) {
                    ((InteractiveTileObjectInterface) fixB.getUserData()).setTouched(true);
                } else {
                    ((InteractiveTileObjectInterface) fixA.getUserData()).setTouched(true);
                }
                break;
            case FallingMan.PLAYER_HAND_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == FallingMan.PLAYER_HAND_BIT) {
                    ((InteractiveTileObjectInterface) fixB.getUserData()).setTouched(true);
                } else {
                    ((InteractiveTileObjectInterface) fixA.getUserData()).setTouched(true);
                }
                break;
            case FallingMan.PLAYER_THIGH_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == FallingMan.PLAYER_THIGH_BIT) {
                    ((InteractiveTileObjectInterface) fixB.getUserData()).setTouched(true);
                } else {
                    ((InteractiveTileObjectInterface) fixA.getUserData()).setTouched(true);
                }
                break;
            case FallingMan.PLAYER_SHIN_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == FallingMan.PLAYER_SHIN_BIT) {
                    ((InteractiveTileObjectInterface) fixB.getUserData()).setTouched(true);
                } else {
                    ((InteractiveTileObjectInterface) fixA.getUserData()).setTouched(true);
                }
                break;
            case FallingMan.PLAYER_FOOT_BIT | FallingMan.INTERACTIVE_TILE_OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == FallingMan.PLAYER_FOOT_BIT) {
                    ((InteractiveTileObjectInterface) fixB.getUserData()).setTouched(true);
                } else {
                    ((InteractiveTileObjectInterface) fixA.getUserData()).setTouched(true);
                }
                break;
            case FallingMan.PLAYER_HEAD_BIT | FallingMan.ROCK_BIT:
            case FallingMan.PLAYER_FOOT_BIT | FallingMan.ROCK_BIT:
            case FallingMan.PLAYER_BELLY_BIT | FallingMan.ROCK_BIT:
            case FallingMan.PLAYER_ARM_BIT | FallingMan.ROCK_BIT:
            case FallingMan.PLAYER_FORE_ARM_BIT | FallingMan.ROCK_BIT:
            case FallingMan.PLAYER_HAND_BIT | FallingMan.ROCK_BIT:
            case FallingMan.PLAYER_THIGH_BIT | FallingMan.ROCK_BIT:
            case FallingMan.PLAYER_SHIN_BIT | FallingMan.ROCK_BIT:
                gameScreen.setGameOver(true);
                break;
            case FallingMan.PLAYER_FOOT_BIT | FallingMan.WALL_INSIDE_TOWER:
            case FallingMan.PLAYER_BELLY_BIT | FallingMan.WALL_INSIDE_TOWER:
            case FallingMan.PLAYER_ARM_BIT | FallingMan.WALL_INSIDE_TOWER:
            case FallingMan.PLAYER_FORE_ARM_BIT | FallingMan.WALL_INSIDE_TOWER:
            case FallingMan.PLAYER_HAND_BIT | FallingMan.WALL_INSIDE_TOWER:
            case FallingMan.PLAYER_THIGH_BIT | FallingMan.WALL_INSIDE_TOWER:
            case FallingMan.PLAYER_SHIN_BIT | FallingMan.WALL_INSIDE_TOWER:
                if (fixA.getFilterData().categoryBits == FallingMan.WALL_INSIDE_TOWER) {
                    ((PlayerBodyPart) fixB.getUserData()).setTouchWall(true);
                } else {
                    ((PlayerBodyPart) fixA.getUserData()).setTouchWall(true);
                }
                break;

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
