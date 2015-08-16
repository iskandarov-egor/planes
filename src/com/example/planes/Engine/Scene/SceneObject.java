package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 15.07.15.
 */
public class SceneObject extends AbstractSceneObject{
    protected SceneObject parent = null;
    private List<ObjectGroup> groups = new ArrayList<>(1);

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
        super.remove();
        getScene().removeObject(this);
        for(ObjectGroup group : groups) {
            group.onObjectRemoved(this);
        }

        groups.clear();
    }
}
