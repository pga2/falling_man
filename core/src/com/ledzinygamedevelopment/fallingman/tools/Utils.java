package com.ledzinygamedevelopment.fallingman.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;

public class Utils {

    public static float getDeltaTimeX1(float dt) {
        return 60 * dt;
    }

    public static float getDistBetweenBodies(Body body1, Body body2) {
        float distX = Math.abs(Math.abs(body1.getPosition().x) - Math.abs(body2.getPosition().x));
        float distY = Math.abs(Math.abs(body1.getPosition().y) - Math.abs(body2.getPosition().y));
        float dist = (float) Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
        return dist;
    }

}
