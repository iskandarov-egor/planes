package com.example.planes.Engine.Body;

/**
 * Created by egor on 13.08.15.
 */
public class Point extends Body {

    @Override
    public boolean isPointInside(float dx, float dy) {
        return dx == 0 & dy == 0;
    }

    @Override
    public void rebuild(float dx, float dy, float angle, float h) {

    }

    @Override
    protected float getLeft() {
        return 0;
    }

    @Override
    protected float getRight() {
        return 0;
    }
}
