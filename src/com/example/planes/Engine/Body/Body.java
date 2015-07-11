package com.example.planes.Engine.Body;

import com.example.planes.Engine.SceneObject;

/**
 * Created by egor on 02.07.15.
 */
public abstract class Body {

    public SceneObject owner;

    public Body(SceneObject owner) {
        if(owner == null) throw new NullPointerException("owner is null");
        this.owner = owner;
    }

    private Body(){}

    public abstract boolean intersects(Body body, float period);

    public SceneObject getOwner() {
        return owner;
    }

}
