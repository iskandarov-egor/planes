package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Utils;

import java.util.Comparator;

/**
 * Created by egor on 18.07.15.
 */
abstract class AbstractSceneObject {
    protected float x;
    protected float y;
    protected float angle = 0;
    protected float dx = 0;
    protected float dy = 0;
    private int zindex = 0;

    protected boolean visible = true;
    private final Scene scene;

    protected Sprite sprite = null;
    protected Body body = null;
    private float h;
    private boolean removeWhenAnimDone = false;

    AbstractSceneObject(float x, float y, Scene scene, float height) {
        this.x = x;
        this.y = y;
        this.scene = scene;
        h = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
        rebuild();
    }

    public void setSprite(Sprite sprite) {

        this.sprite = sprite;
        if(sprite != null) sprite.onAssignedTo(this);
        if(sprite != null) sprite.rebuild(dx, dy, angle, h);
    }

    public void setBody(float radius) {
        setBody(new Circle(radius));
    }

    public void setBody(Body body) {
        this.body = body;
        if(this.body != null) body.rebuild(dx, dy, angle, h);
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
        angle = Utils.modpi2(angle);
        if(this.angle != angle) {
            this.angle = angle;
            rebuild(); // todo no need to rebuild sprite each phys frame
        }
    }

    private void rebuild() {
        if(sprite != null) sprite.rebuild(dx, dy, angle, h);
        if(body != null) body.rebuild(dx, dy, angle, h);
    }


    public void setXY(float x, float y) {
        setX(x);
        setY(y);
    }

    public void setDisplacement(float dx, float dy) {
        if(this.dx != dx || this.dy != dy) {
            this.dx = dx;
            this.dy = dy;
            rebuild();
        }
    }

    void onGraphicsFrame(float graphicsFPS) {
        if(sprite != null) sprite.onFrame(graphicsFPS);
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

    public void setY(float y) {
        this.y = y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setRemoveWhenAnimDone(boolean removeWhenAnimDone) {
        this.removeWhenAnimDone = removeWhenAnimDone;
    }

    private boolean removed = false;

    public void remove() {
        if(removed) throw new RuntimeException();
        removed = true;
    }

    public boolean getVisible() {
        return visible;
    }

    void draw(float x, float y, float[] transform) {
        //debug
        if(transform == null) throw new NullPointerException("transform");

        if(sprite != null) {
            if(!sprite.isLoaded()) {
                sprite.load();
                sprite.rebuild(dx, dy, angle, h);
            }
            sprite.draw(x, y, transform);
        }
    }

    public float getDy() {
        return dy;
    }

    public float getDx() {
        return dx;
    }

    public boolean isRemoveWhenAnimDone() {
        return removeWhenAnimDone;
    }

    public boolean isRemoved() {
        return removed;
    }

    public int getZindex() {
        return zindex;
    }

    public void setZindex(int zindex) {
        this.zindex = zindex;
    }

    public static class ZindexComparator implements Comparator<AbstractSceneObject> {

        private static Comparator<AbstractSceneObject> instance;

        public static Comparator<AbstractSceneObject> getInstance() {
            if(instance == null) instance = new ZindexComparator();
            return instance;
        }

        @Override
        public int compare(AbstractSceneObject t1, AbstractSceneObject t2) {
            return t1.zindex - t2.zindex;
        }



    }
}
