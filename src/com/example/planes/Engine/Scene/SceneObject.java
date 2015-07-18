package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Utils;

/**
 * Created by egor on 15.07.15.
 */
public class SceneObject extends AbstractSceneObject{
    protected SceneObject parent = null;

    public SceneObject(float x, float y) {
        super(x, y);
    }



    public boolean intersects(SceneObject object, float period) {
        //debug
        if(object == null) throw new NullPointerException("lalala");
        if(period < 0) throw new IllegalArgumentException("period");

        if(body == null || object.body == null ) throw new NullPointerException("no body");
        return body.intersects(object.body, object.getAbsoluteX() - getAbsoluteX(),
                object.getAbsoluteY() - getAbsoluteY(), period);
    }



    public void setParent(SceneObject parent) {
        // todo написать
    }

    void draw(float x, float y, float[] transform) {
        //debug
        if(transform == null) throw new NullPointerException("transform");

        if(sprite != null) {
            if(!sprite.loaded) {
                sprite.load();
                sprite.rebuild(dx, dy, angle);
            }
            sprite.draw(x, y, transform);
        }
    }

    void onGraphicsFrame(float graphicsFPS) {
        if(sprite != null) sprite.onFrame(graphicsFPS);
    }
}
