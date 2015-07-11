package com.example.planes.Engine.Sprite;

import com.example.planes.Utils.AndroidCanvas;

/**
 * Created by egor on 10.07.15.
 */
public abstract class Sprite {
    private float radius;

    //Картинка кароч
    public Sprite() {

    }

    public abstract void draw(float x, float y, float angle, float[] transform);
    public abstract void onFrame();

    public float getRadius() {
        return radius;
    }
}
