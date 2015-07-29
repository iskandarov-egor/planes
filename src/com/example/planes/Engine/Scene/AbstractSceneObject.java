package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Utils;

/**
 * Created by egor on 18.07.15.
 */
abstract class AbstractSceneObject {
    protected float x;
    protected float y;
    protected float angle = 0;
    protected float dx = 0;
    protected float dy = 0;
    protected AbstractSceneObject parent = null;
    private final Scene scene;

    protected Sprite sprite = null;
    protected Body body = null;
    AbstractSceneObject(float x, float y, Scene scene) {
        this.x = x;
        this.y = y;
        this.scene = scene;
    }
    public float getX() {
        return x;
    }


    public float getY() {
        return y;
    }

    public float getAbsoluteX() {
        if(parent != null) return parent.getAbsoluteX() + x;
        return x;
    }

    public float getAbsoluteY() {
        if(parent != null) return parent.getAbsoluteY() + y;
        return y;
    }

    public void setSprite(Sprite sprite) {

        this.sprite = sprite;
        if(sprite != null) sprite.rebuild(dx, dy, angle);
    }

    public void setBody(float radius) {
        this.body = new Circle(radius);
    }

    public float getRadius() {
        if(sprite == null) return 0;
        return sprite.getRadius();
    }

    public boolean isPointInside(float x, float y) {
        if(body == null) throw new NullPointerException("no body");

        return body.isPointInside(x - this.x, y - this.y);
    }

    public void setAngle(float angle) {
        if(this.angle != angle) {
            this.angle = angle;
            rebuild();
        }
    }

    private void rebuild() {
        if(sprite != null) sprite.rebuild(dx, dy, angle);
        if(body != null) body.rebuild(dx, dy, angle);
    }


    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setDisplacement(float dx, float dy) {
        if(this.dx != dx || this.dy != dy) {
            this.dx = dx;
            this.dy = dy;
            rebuild();
        }
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Scene getScene() {
        return scene;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getAngle() {
        return angle;
    }
}
