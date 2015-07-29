package com.example.planes.Engine.Scene;


/**
 * Created by egor on 10.07.15.
 */
public abstract class Sprite {

    boolean loaded = false;
    protected float w;
    protected float h;
    //Картинка кароч
    public Sprite() {

    }

    abstract void draw(float x, float y, float[] transform);
    abstract void onFrame(float graphicsFPS);

    public abstract float getRadius();
    public abstract void rebuild(float dx, float dy, float angle);

    public abstract void load();

    public float getH() {
        return h;
    }

    public float getW() {
        return w;
    }
}
