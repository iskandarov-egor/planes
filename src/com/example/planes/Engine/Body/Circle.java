package com.example.planes.Engine.Body;

/**
 * Created by egor on 10.07.15.
 */
public class Circle extends Body {
    public Circle(float radius) {
        if(radius <= 0) throw new IllegalArgumentException("radius");
        this.radius = radius;
    }

    public float radius;

    @Override
    public boolean intersects(Body body, float dx, float dy, float period) {
        //debug
        if(body == null) throw new NullPointerException("body is null");
        if(period < 0) throw new IllegalArgumentException("period");


        if(body instanceof Circle) {
            float d = radius + ((Circle)body).radius;
            dx = Math.abs(dx);
            dy = Math.abs(dy);

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

    @Override
    public boolean isPointInside(float dx, float dy) {
        return Math.hypot(dx, dy) < radius;
    }

    @Override
    public void rebuild(float dx, float dy, float angle, float h) {
        //
    }


}
