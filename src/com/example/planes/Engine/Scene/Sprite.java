package com.example.planes.Engine.Scene;


/**
 * Created by egor on 10.07.15.
 */
public abstract class Sprite {

    //Картинка кароч
    public Sprite() {

    }

    abstract void draw(float x, float y, float angle, float[] transform);
    abstract void onFrame(float graphicsFPS);

    public abstract float getRadius();
}