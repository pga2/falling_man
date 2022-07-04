package com.ledzinygamedevelopment.fallingman.tools.entities;

public class B2SteeringEntityContainer {

    private B2dSteeringEntity entity;
    private B2dSteeringEntity target;

    public B2SteeringEntityContainer(B2dSteeringEntity entity, B2dSteeringEntity target) {
        this.entity = entity;
        this.target = target;
    }

    public B2dSteeringEntity getEntity() {
        return entity;
    }

    public B2dSteeringEntity getTarget() {
        return target;
    }
}
