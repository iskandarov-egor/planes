package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Utils;

/**
 * Created by egor on 12.07.15.
 */
public class Sticker {
    protected float x;
    protected float y;
    protected float angle = 0;
    private Sticker(){}
    private Sprite sprite = null;
    private Body body = null;
    public Sticker(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void removeSprite() {
        sprite = null;
    }

    public void setAngle(float angle){
        this.angle = Utils.mod(angle, Utils.PI2);
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

    void draw(float[] transformM) {
        //debug
        if(sprite == null) throw new RuntimeException("no sprite");

        if(sprite != null) sprite.draw(x, y, angle, transformM);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setBody(float radius) {
        //debug
        if(radius <= 0) throw new IllegalArgumentException("radius");

        body = new Circle(radius);
    }

    public float getRadius() {
        if(sprite == null) return 0;
        return sprite.getRadius();
    }

    public boolean isPointInside(float x, float y) {
        if(body == null) throw new NullPointerException("no body");

        return body.isPointInside(x - this.x, y - this.y);
    }
}
