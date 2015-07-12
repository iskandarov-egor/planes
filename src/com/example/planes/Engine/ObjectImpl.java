package com.example.planes.Engine;


import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Sprite.OpenGLShape;
import com.example.planes.Engine.Sprite.Sprite;

/**
 * Created by egor on 01.07.15.
 */
final class ObjectImpl implements SceneObject {
    private SceneImpl scene = null;
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

        this.scene = scene;
        this.x = x;
        this.y = y;
    }

    private ObjectImpl(){} //


    public void onPhysicsFrame(float horizPeriod) {
        //debug
        if(horizPeriod < 0) throw new IllegalArgumentException("period");

        angle = Utils.mod(angle + angleSpeed, Utils.PI2);

        x += vx;
        y += vy;

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
        this.vx = vx / scene.getPhysicsFPS();
        this.vy = vy / scene.getPhysicsFPS();
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

        if(!hasBody() || !object.hasBody()
                || removed || object.removed) return false; // removed тут только для быстродействия
        return body.intersects(object.body, period);
    }

    public boolean hasBody(){
        return body != null;
    }

    public Body getBody() {
        return body;
    }

    public void setParent(SceneObject parent) {
        // todo написать
    }

    public ObjectImpl getParent() {
        return parent;
    }


    @Override
    public void addBody(float radius) {
        //debug
        if(radius <= 0) throw new IllegalArgumentException("radius");

        body = new Circle(this, radius);
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

        int fps = scene.getPhysicsFPS();
        if(fps <= 0) throw new RuntimeException("fps");
        this.angleSpeed = angleSpeed / scene.getPhysicsFPS();
    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    @Override
    public ObjectImpl getImpl(){
        return this;
    }



}
