package com.example.planes.Game.Models;

import com.example.planes.Engine.Scene.SceneObject;

/**
 * Created by egor on 19.07.15.
 */
public class Cloud implements Prop {
    private SceneObject so;
    private float vx = 0;
    private float vy = 0;

    public Cloud(SceneObject so, float vx, float vy) {
        this.so = so;
        this.vx = vx;
        this.vy = vy;
    }

    public void onPhysicsFrame(float fps) {
        so.setXY(so.getX() + vx/fps, so.getY() + vy/fps);
    }


    public SceneObject getSceneObject() {
        return so;
    }
}
