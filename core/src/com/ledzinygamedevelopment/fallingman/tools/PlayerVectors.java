package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.sprites.player.Player;
import com.ledzinygamedevelopment.fallingman.sprites.player.bodyparts.PlayerBodyPart;

import java.util.HashMap;

public class PlayerVectors {

    private Player player;
    private boolean setVectorFromPreviusScreen;
    private Body playerB2body;
    private HashMap<String, Vector3> bodyPartsCords;

    public PlayerVectors(Player player, boolean setVectorFromPreviusScreen) {

        this.player = player;
        playerB2body = player.b2body;
        this.setVectorFromPreviusScreen = setVectorFromPreviusScreen;
    }

    public void calculatePlayerVectors(int mapHeight) {

        //transforming player position to new map (unvisible body parts)
        float playerHeadPreviusY = playerB2body.getPosition().y;
        playerB2body.setTransform(playerB2body.getPosition().x, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM, playerB2body.getAngle());
        bodyPartsCords = new HashMap<>();
        bodyPartsCords.put("head", new Vector3(playerB2body.getPosition().x, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM, playerB2body.getAngle()));
        for (Body body : player.getBodyPartsAll()) {
            String bodyPart;
            //calculating distance between body part and player
            float yDiff;

            if (body.getPosition().y < playerHeadPreviusY) {
                yDiff = -Math.abs(Math.abs(body.getPosition().y) - Math.abs(playerHeadPreviusY));
            } else {

                yDiff = Math.abs(Math.abs(body.getPosition().y) - Math.abs(playerHeadPreviusY));
            }

            float previosBodyAngle = playerB2body.getAngle();
            //Teleporting body part
            //body.setTransform(body.getPosition().x, FallingMan.PLAYER_STARTING_Y_POINT / FallingMan.PPM + yDiff, body.getAngle());

            for (PlayerBodyPart bodyPart1 : player.getBodyParts()) {
                if (bodyPart1.getBodyPartNameFromBody(body) != "unknown") {
                    bodyPartsCords.put(bodyPart1.getBodyPartNameFromBody(body), new Vector3(body.getPosition().x, (mapHeight - FallingMan.MAX_WORLD_HEIGHT / 2f) / FallingMan.PPM + yDiff, body.getAngle()));
                }
            }
        }
    }

    public void setNewPlayerVectorsFromPreviusMap(Player newPlayer) {
        if (setVectorFromPreviusScreen) {
            newPlayer.b2body.setTransform(bodyPartsCords.get("head").x, bodyPartsCords.get("head").y, bodyPartsCords.get("head").z);

            for (Body body : newPlayer.getBodyPartsAll()) {
                PlayerBodyPart bodyPart = ((PlayerBodyPart) body.getUserData());
                for (String key : bodyPartsCords.keySet()) {
                    if (bodyPart != null) {
                        if (key.equals(bodyPart.getBodyPartName())) {
                            body.setTransform(bodyPartsCords.get(key).x, bodyPartsCords.get(key).y, bodyPartsCords.get(key).z);
                        }
                    }
                }
            }
        }
    }



}
