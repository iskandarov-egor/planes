package com.example.planes.Engine;


import com.example.planes.Engine.Body.Body;

/**
 * Created by egor on 01.07.15.
 */
class ObjectImpl extends Sticker implements SceneObject {
    private float vx = 0;
    private float vy = 0;
    private float x = 0;
    private float y = 0;

    private float angle = 0;
    private float angleSpeed = 0;
    private Sprite sprite = null;
    private Body body = null;
    private ObjectImpl parent = null;
    private boolean removed = false;

    public ObjectImpl(float x, float y) {
        super(x, y);
        this.x = x;
        this.y = y;
    }

    public void onPhysicsFrame(float horizPeriod, float physicsFPS) {
        //debug
        if(horizPeriod < 0) throw new IllegalArgumentException("period");

        angle = Utils.mod(angle + angleSpeed / physicsFPS, Utils.PI2);

        x += vx / physicsFPS;
        y += vy / physicsFPS;

        if(horizPeriod != 0) {
            while (x > horizPeriod / 2) x -= horizPeriod;
            while (x < -horizPeriod / 2) x += horizPeriod;
        }
    }

    public void removeSprite() {
        sprite = null;
    }

    public void setSpeed(float vx, float vy) {
        //debug
        if(Math.abs(vx) > 444444) throw new RuntimeException("что то не так");
        if(Math.abs(vy) > 444444) throw new RuntimeException("что то не так");

        this.vx = vx;
        this.vy = vy;
    }


    public float getAbsoluteX() {
        if(parent != null) return parent.getAbsoluteX() + x;
        return x;
    }

    public float getAbsoluteY() {
        if(parent != null) return parent.getAbsoluteY() + y;
        return y;
    }

//    public Vector getSpeed() {
//        if(parent != null) return MathHelper.vectorSum(parent.getSpeed(), speed);
//        return speed;
//    }



    public boolean intersects(ObjectImpl object, float period) {
        //debug
        if(object == null) throw new NullPointerException("lalala");
        if(period < 0) throw new IllegalArgumentException("period");

        if(!hasBody() || !object.hasBody()) throw new NullPointerException("no body");
        if(removed || object.removed) return false;
        return body.intersects(object.body, object.getAbsoluteX() - getAbsoluteX(),
                object.getAbsoluteY() - getAbsoluteY(), period);
    }

    public boolean hasBody(){
        return body != null;
    }

    public void setParent(SceneImpl parent) {
        // todo написать
    }

    public void draw(float x, float y, float[] transform) {
        //debug
        if(transform == null) throw new NullPointerException("transform");

        if(sprite != null) sprite.draw(x, y, angle, transform);
    }

    public void setRemoved(boolean removed) {
        //debug
        if(removed) throw new RuntimeException("already removed");

        this.removed = removed;
    }

    public void setAngleSpeed(float angleSpeed) {
        //debug
        if(Math.abs(angleSpeed) > 444) throw new RuntimeException("что то не так");


        this.angleSpeed = angleSpeed;
    }


    @Override
    public ObjectImpl getImpl(){
        return this;
    }


    public void onGraphicsFrame(float graphicsFPS) {
        if(sprite != null) sprite.onFrame(graphicsFPS);
    }
}
