package com.ledzinygamedevelopment.fallingman.sprites.interactiveobjects.mapobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

public interface InteractiveObjectInterface {
    public void setTouched(boolean touched);
    public void touched();
    public boolean isTouched();
    public Body getBody();
    public void draw(Batch batch);
    public void update(float dt);
    public boolean isToRemove();
}
