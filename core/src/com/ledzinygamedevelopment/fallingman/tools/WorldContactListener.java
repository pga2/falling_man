package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.Coin;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.DeadMachine;
import com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.buttons.SpinButton;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;

public class WorldContactListener implements ContactListener {

    private Player player;

    public WorldContactListener(Player player) {

        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case FallingMan.PLAYER_HEAD_BIT | FallingMan.COIN_BIT:
                if(fixA.getFilterData().categoryBits == FallingMan.PLAYER_HEAD_BIT) {
                    ((Coin) fixB.getUserData()).touched();
                } else {
                    ((Coin) fixA.getUserData()).touched();
                }
                break;
            case FallingMan.PLAYER_HEAD_BIT | FallingMan.DEAD_MACHINE_BIT:
                if(fixA.getFilterData().categoryBits == FallingMan.PLAYER_HEAD_BIT) {
                    ((DeadMachine) fixB.getUserData()).touched();
                } else {
                    ((DeadMachine) fixA.getUserData()).touched();
                }
                break;
            /*case FallingMan.TOUCHED_POINT_BIT | FallingMan.SPIN_BIT:

                if(fixA.getFilterData().categoryBits == FallingMan.TOUCHED_POINT_BIT) {
                    Gdx.app.log("dead machine", "dasdsa");

                    ((SpinButton) fixB.getUserData()).removeCells();
                    player.setRemoveHeadJointsAndButton(true);
                } else {
                    Gdx.app.log("dead machine", "dasdsa");

                    ((SpinButton) fixA.getUserData()).removeCells();
                    player.setRemoveHeadJointsAndButton(true);
                }
                break;*/
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
