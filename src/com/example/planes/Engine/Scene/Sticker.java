package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Utils;

/**
 * Created by egor on 12.07.15.
 */
public class Sticker extends AbstractSceneObject{

    Sticker(float x, float y, Scene scene) {
        super(x, y, scene);
    }

    public void setParent(Sticker parent) {
        // todo
    }

    void draw(float[] transformM) {
        //debug
        if(sprite == null) throw new RuntimeException("no sprite");

        if(sprite != null) {
            if(!sprite.loaded) {
                sprite.load();
            }
            sprite.draw(x, y, transformM);
        }
    }






}
