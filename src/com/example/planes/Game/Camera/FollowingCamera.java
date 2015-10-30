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
    private float distance = 2*GameConfig.maxCameraDistance;
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

    int c = 0;
    @Override
    public void onFrame(float fps) {
        //if(true) return;
        fps = 1f;
        if(plane == null) return;
        float period = plane.getScene().getWorldWidth();
        float px = plane.getX();
        float py = plane.getY();
        float vx = plane.getVx();
        float vy = plane.getVy();
        float v = (float) Math.hypot(vx, vy);
        float dx = px + vx*distance/Plane.velolim;
        float dy = py + vy*distance/Plane.velolim; // todo make smooth
        c++;
        if (c % 120 == 0) Log.d("velolim", String.valueOf(vx / Plane.getMaxV()));
//        while(Math.abs(dx - x) > Math.abs(dx - x - period)) dx -= period; clouds
//        while(Math.abs(dx - x) > Math.abs(dx - x + period)) dx += period;


        float d = (float) Math.hypot(dx, dy);

        vx = Math.min(dx - x, ((dx - x) * 0.15f) / fps);
        vy = Math.min(dx - x, ((dy - y) * 0.15f) / fps);
        x += vx;
        y += vy;
//        x = px;
//        y = py;
//        x = dx;
//        y = dy;

//        while(x > period/2) x -= period; clouds
//        while(x < -period/2) x += period;


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
