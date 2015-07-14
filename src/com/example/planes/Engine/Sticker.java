package com.example.planes.Engine;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Sprite.Sprite;
import com.example.planes.Engine.Sprite.Square;

/**
 * Created by egor on 12.07.15.
 */
public class Sticker {
    private float x;
    private float y;
    private Sticker(){}
    private Sprite sprite = null;
    private Body body = null;
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

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setBody(float radius) {
        //debug
        if(radius <= 0) throw new IllegalArgumentException("radius");

        body = new Circle(radius);
    }

    public boolean isPointInside(float x, float y) {
        if(body == null) throw new NullPointerException("no body");

        return body.isPointInside(x - this.x, y - this.y);
    }
}
