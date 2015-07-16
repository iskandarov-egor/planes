package com.example.planes;

import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Utils.MathHelper;
import com.example.planes.Utils.Vector;

/**
 * Created by egor on 13.07.15.
 */
public class StupidTestCamera {
    private Vector position = new Vector(0.3f, 0);
    private Vector zoom = new Vector(0, 0.6f);
    private Scene scene;
    public StupidTestCamera(Scene scene) {
        this.scene = scene;
    }

    private StupidTestCamera(){}

    public void onFrame(){
        MathHelper.rotate(position, 0, 0, MathHelper.PI2 / 600); // 1 оборот в 10 секунд
        MathHelper.rotate(zoom, 0, 0, MathHelper.PI2 / 600);
        float z = (zoom.x > 0)?1+zoom.x:(1/(1-zoom.x));
        scene.getViewport().setZoom(z);
        scene.getViewport().setPosition(position.x, position.y);
    }


}
