package com.example.planes.Game.Camera;

import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.Viewport;
import com.example.planes.Engine.Utils;
import com.example.planes.Game.Models.Plane;


/**
 * Created by egor on 19.07.15.
 */
public class FollowingCamera extends Camera{
    private Plane plane = null;
    private float distance = GameConfig.cameraDistance;
    private float x, y;
    private float zoom = 1;
    private Viewport view;
    public FollowingCamera(Plane plane) {
        x = plane.getSceneObject().getX();
        y = plane.getSceneObject().getY();
        view = plane.getSceneObject().getScene().getViewport();
        this.plane = plane;
    }

    @Override
    public void onFrame(float fps) {
        if(plane == null) return;
        float period = plane.getSceneObject().getScene().getWorldWidth();
        float px = plane.getSceneObject().getX();
        float py = plane.getSceneObject().getY();
        float vx = plane.getVx();
        float vy = plane.getVy();
        float v = (float) Math.hypot(vx, vy);
        float dx = px + vx*distance/0.5f;
        float dy = py + vy*distance/0.5f;
        while(Math.abs(dx - x) > Math.abs(dx - x - period)) dx -= period;
        while(Math.abs(dx - x) > Math.abs(dx - x + period)) dx += period;

        x += (dx - x) * 2 / fps;
        y += (dy - y) * 2 / fps;

        while(x > period/2) x -= period;
        while(x < -period/2) x += period;


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
