package com.example.planes.Game.Models;

import com.example.planes.Config.Config;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.R;

/**
 * Created by egor on 19.07.15.
 */
public class Cloud extends SceneObject implements Prop {
    protected float vx = 0;
    protected float vy = 0;

    public Cloud(Scene scene, int sprite) {
        super(0, 0, scene, Config.cloudHeight);
        if(sprite <= 0) throw new IllegalArgumentException();
        place();
        setSprite(new StaticSprite(sprite));
    }

    public void onPhysicsFrame(float fps) {
        setXY(getX() + vx / fps, getY() + vy / fps);
    }

    protected void place() {
        float w2 = getScene().getWorldWidth() / 2;
        float x = (float) (-w2 + 2 * w2*Math.random());
        float y = (float) (Math.random()* (GameConfig.worldCeiling + 0.5));
        setXY(x, y);
        vx = (float) (GameConfig.cloudSpeedMin + (GameConfig.cloudSpeedMax - GameConfig.cloudSpeedMin)*Math.random());
        vy = 0;
    }
}
