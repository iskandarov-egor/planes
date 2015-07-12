package com.example.planes.Engine;

import com.example.planes.Engine.Sprite.Sprite;
import com.example.planes.Engine.Sprite.Square;

/**
 * Created by egor on 12.07.15.
 */
public class Sticker {
    private float x;
    private float y;
    private Sticker(){}
    private Sprite sprite = new Square();
    public Sticker(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void draw(float[] transformM) {
        //debug
        if(sprite == null) throw new RuntimeException("no sprite");

        if(sprite != null) sprite.draw(x, y, 0, transformM);
    }
}
