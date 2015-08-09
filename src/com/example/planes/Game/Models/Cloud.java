package com.example.planes.Game.Models;

import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.R;

/**
 * Created by egor on 19.07.15.
 */
public class Cloud extends SceneObject implements Prop {
    private float vx = 0;
    private float vy = 0;

    public Cloud(Scene scene, float x, float y, float vx, float vy) {
        super(x, y, scene);
        this.vx = vx;
        this.vy = vy;
        setSprite(new StaticSprite(R.drawable.cloud, 0.2f));
    }

    public void onPhysicsFrame(float fps) {
        setXY(getX() + vx / fps, getY() + vy / fps);
    }

}
