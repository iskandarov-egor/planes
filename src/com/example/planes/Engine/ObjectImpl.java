package com.example.planes.Engine;


import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Sprite.OpenGLShape;
import com.example.planes.Engine.Sprite.Sprite;

/**
 * Created by egor on 01.07.15.
 */
final class ObjectImpl implements SceneObject {
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

    public ObjectImpl(float x, float y, SceneImpl scene) {
        //debug
        if(scene == null) throw new NullPointerException("scene");

        this.x = x;
        this.y = y;
    }

    private ObjectImpl(){} //


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

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        SceneImpl scene = null;
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

    public void setParent(SceneObject parent) {
        // todo написать
    }


    @Override
    public void setBody(float radius) {
        //debug
        if(radius <= 0) throw new IllegalArgumentException("radius");

        body = new Circle(radius);
    }

    public float getRadius() {
        if(sprite == null) return 0;
        return sprite.getRadius();
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

    public void setAngle(float angle){
        this.angle = Utils.mod(angle, Utils.PI2);
    }

    @Override
    public ObjectImpl getImpl(){
        return this;
    }


    public void onGraphicsFrame(float graphicsFPS) {
        if(sprite != null) sprite.onFrame(graphicsFPS);
    }
}
