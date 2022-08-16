package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.beams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.ledzinygamedevelopment.fallingman.FallingMan;
import com.ledzinygamedevelopment.fallingman.screens.PlayScreen;

public class BeamRoute extends Sprite {

    public BeamRoute(PlayScreen playScreen, Vector2 startPos, Vector2 endPos, Vector2 position, float scaleX) {

        setBounds(0, 0, 256 / FallingMan.PPM, 32 / FallingMan.PPM);
        setRegion(new TextureRegion(playScreen.getDefaultAtlas().findRegion("beam_route"), 0, 0, 256, 32));
        setPosition(position.x - getWidth() / 2, position.y);
        setOrigin(getWidth(), 0);
        setScale((float) Math.sqrt(Math.pow((endPos.x - startPos.x), 2) + Math.pow((endPos.y - startPos.y), 2)) / 256, 1);
        Vector2 v1 = startPos.cpy().sub(startPos);
        Vector2 v2 = endPos.cpy().sub(startPos);
        float angle1 = v1.angleDeg();
        float angle2 = v2.angleDeg();

        float angleBetween = Math.abs(angle1 - angle2);
//If the angle is more then 180 then comparing the other way around would be shorter.
        if (angleBetween > 180)
            angleBetween = 360 - angleBetween;
        setRotation(angleBetween - 180);
        Gdx.app.log("d", "");
    }
}
