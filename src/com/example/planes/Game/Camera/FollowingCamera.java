package com.example.planes.Game.Camera;

import com.example.planes.Game.Models.Plane;

/**
 * Created by egor on 19.07.15.
 */
public class FollowingCamera extends Camera{
    private Plane plane = null;
    private float distance = 0.8f;
    private float x, y;

    public FollowingCamera(Plane plane) {
        this.plane = plane;
        x = plane.getSceneObject().getX();
        y = plane.getSceneObject().getY();
    }

    @Override
    public void onFrame(float fps) {
        float period = plane.getSceneObject().getScene().getPeriod();
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

        plane.getSceneObject().getScene().getViewport().setPosition(x, y);
    }
}
