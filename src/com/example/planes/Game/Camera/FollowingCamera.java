package com.example.planes.Game.Camera;

import android.util.Log;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.Viewport;
import com.example.planes.Engine.Utils;
import com.example.planes.Game.Models.Plane;


/**
 * Created by egor on 19.07.15.
 */
public class FollowingCamera extends Camera{
    private Plane plane = null;
    private float distance = GameConfig.maxCameraDistance;
    private float x, y, vx, vy;
    private float zoom = 1;
    private Viewport view;
    public FollowingCamera(Plane plane) {
        x = plane.getX();
        y = plane.getY();
        view = plane.getScene().getViewport();
        this.plane = plane;
        vx = 0;
        vy = 0;
    }

    @Override
    public void onFrame(float fps) {


        if(plane == null) return;
        float period = plane.getScene().getWorldWidth();
        float px = plane.getX();
        float py = plane.getY();
        float vx = plane.getVx();
        float vy = plane.getVy();
        float dx = px + vx + vx*distance/Plane.velolim;
        float dy = py + vy + vy*distance/Plane.velolim;


        vx = ((dx - x)) / fps;
        vy = ((dy - y)) / fps;
        x += vx;
        y += vy;


        float bottom = (-1 + y)*zoom;

        if(bottom < -1) y = -1/zoom +1;
        view.setPosition(x, y);
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        view.setZoom(zoom);
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }
}
