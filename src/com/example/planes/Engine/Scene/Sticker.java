package com.example.planes.Engine.Scene;

import com.example.planes.Engine.Body.Body;
import com.example.planes.Engine.Body.Circle;
import com.example.planes.Engine.Utils;

/**
 * Created by egor on 12.07.15.
 */
public class Sticker extends AbstractSceneObject{

    Sticker(float x, float y, Scene scene, float height) {
        super(x, y, scene, height);
    }



    public void setParent(Sticker parent) {
        // todo
    }

    void draw(float[] transformM) {
        super.draw(x, y, transformM);
    }


}
