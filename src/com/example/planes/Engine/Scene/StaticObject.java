package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Utils;

/**
 * Created by egor on 15.07.15.
 */
public class StaticObject {
    protected float x;
    protected float y;
    protected float angle = 0;
    private Sprite sprite = null;
    private Body body = null;
    protected StaticObject parent = null;

    private StaticObject(){}

    public StaticObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getAbsoluteX() {
        if(parent != null) return parent.getAbsoluteX() + x;
        return x;
    }

    public float getAbsoluteY() {
        if(parent != null) return parent.getAbsoluteY() + y;
        return y;
    }

    public void onPhysicsFrame(float horizPeriod, float physicsFPS) {

    }

    public boolean intersects(StaticObject object, float period) {
        //debug
        if(object == null) throw new NullPointerException("lalala");
        if(period < 0) throw new IllegalArgumentException("period");

        if(!hasBody() || !object.hasBody()) throw new NullPointerException("no body");
        return body.intersects(object.body, object.getAbsoluteX() - getAbsoluteX(),
                object.getAbsoluteY() - getAbsoluteY(), period);
    }

    public boolean hasBody(){
        return body != null;
    }

    public void setParent(StaticObject parent) {
        // todo написать
    }

    void draw(float x, float y, float[] transform) {
        //debug
        if(transform == null) throw new NullPointerException("transform");

        if(sprite != null) sprite.draw(x, y, getModAngle(), transform);
    }

    private float getModAngle() {
        // todo
        return Utils.mod(angle, Utils.PI2);
    }

    public float getRadius() {
        if(sprite == null) return 0;
        return sprite.getRadius();
    }

    void onGraphicsFrame(float graphicsFPS) {
        if(sprite != null) sprite.onFrame(graphicsFPS);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setBody(float radius) {
        this.body = new Circle(radius);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
