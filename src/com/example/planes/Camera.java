package com.example.planes;

import com.example.planes.Engine.Engine;
import com.example.planes.Engine.Viewport;
import com.example.planes.Utils.MathHelper;
import com.example.planes.Utils.Vector;

/**
 * Created by egor on 13.07.15.
 */
public class Camera {
    private Vector position = new Vector(0.4f, 0);
    private Vector zoom = new Vector(1, 0.4f);
    public Camera() {
    }

    public void onFrame(){

        MathHelper.rotate(position, 0, 0, MathHelper.PI2 / 600, 0.4f); // 1 оборот в 10 секунд
        MathHelper.rotate(zoom, 1, 0, MathHelper.PI2 / 600, 0.4f);
        Engine.getViewport().setZoom(zoom.x);
        Engine.getViewport().setPosition(position.x, position.y);
    }


}
