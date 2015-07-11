package com.example.planes.Engine.Body;

import com.example.planes.Engine.SceneObject;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 10.07.15.
 */
public class Circle extends Body {
    public Circle(SceneObject owner, float radius) {
        super(owner);
        if(radius <= 0) throw new IllegalArgumentException("radius");
        this.radius = radius;
    }

    public float radius;

    @Override
    public boolean intersects(Body body, float period) {
        //**debug
        if(body == null) throw new NullPointerException("body is null");
        if(period < 0) throw new IllegalArgumentException("period");


        if(body instanceof Circle) {
            float d = radius + ((Circle)body).radius;
            float dx = Math.abs(owner.getAbsoluteX() - body.getOwner().getAbsoluteX());
            float dy = Math.abs(owner.getAbsoluteY() - body.getOwner().getAbsoluteY());

            if(period > 0) {
                while (dx >= period) {
                    dx -= period;
                }
                if (dx > period / 2) dx = -(dx - period);
            }
            return (Math.hypot(dx, dy) < d);
        } else {
            throw new RuntimeException("Circle don't know how to intersect a non-circle");
        }
    }
}
