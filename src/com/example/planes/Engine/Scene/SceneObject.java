package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Utils;
import com.example.planes.Utils.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 15.07.15.
 */
public class SceneObject extends AbstractSceneObject{
    protected SceneObject parent = null;
    private List<ObjectGroup> groups = new ArrayList<>(1);
    private List<SceneObject> children = new ArrayList<>(0);

    public SceneObject(float x, float y, Scene scene, float height) {
        super(x, y, scene, height);
    }


    public void onPhysicsFrame(float fps) {

    }

    void onAddedToGroup(ObjectGroup group) {
        groups.add(group);
    }

    public boolean intersects(SceneObject object, float period) {
        //debug
        if(object == null) throw new NullPointerException("lalala");
        if(period < 0) throw new IllegalArgumentException("period");

        if(body == null || object.body == null ) throw new NullPointerException("no body");
        if(body.intersects(object.body, object.getAbsoluteX() - getAbsoluteX() + object.dx - dx,
                object.getAbsoluteY() - getAbsoluteY() + object.dy - dy, period)) {
            return true;
        }
        return false;
    }

    public void setParent(SceneObject parent) {
        // todo написать
        if (parent == this) throw new IllegalArgumentException();
        if (children.contains(parent)) throw new RuntimeException("you sure?");
        if (parent.getParent() == this) throw new IllegalArgumentException("nope");
        if(this.parent != null) throw new RuntimeException("not implemented");
        this.parent = parent;
        parent.children.add(this);
        relativePos.x = x;
        relativePos.y = y;
    }

    @Override
    public void setX(float x) {
        float horizPeriod = getScene().getWorldWidth();
        if(horizPeriod != 0) {
            while (x > horizPeriod / 2) x -= horizPeriod;
            while (x < -horizPeriod / 2) x += horizPeriod;
        }
        this.x = x;
    }

    @Override
    public void remove() {
        if(isRemoved()) throw new RuntimeException("sure?");
        super.remove();
        getScene().removeObject(this);
        for(ObjectGroup group : groups) {
            group.onObjectRemoved(this);
        }

        for(SceneObject so : children) {
            so.remove();
        }

        groups.clear();
    }

    public SceneObject getParent() {
        return parent;
    }

    public float getAbsoluteX() {
        if(parent != null) return parent.getAbsoluteX() + relativePos.x;
        return x;
    }

    public float getAbsoluteY() {
        if(parent != null) return parent.getAbsoluteY() + relativePos.y;
        return y;
    }

    public boolean hasParent() {
        return parent != null;
    }


    Vector relativePos = new Vector();
    @Override
    public void setAngle(float angle) {
        super.setAngle(angle);
        for (SceneObject so : children) {
            so.setAngle(angle);
            so.relativePos.set(so.x, so.y);
            so.relativePos.rotate(angle);
        }
    }
}
