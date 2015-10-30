package com.example.planes.Game.Models;

import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.R;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 19.07.15.
 */
public class Cloud extends SceneObject {
    private static final float MIN_H = 0.2f;
    private static final float MAX_H = 0.5f;

    protected float vx = 0;
    protected float vy = 0;

    public Cloud(Scene scene, float koef) {
        super(0, 0, scene, MathHelper.rand(MIN_H, MAX_H));

        setSprite(new StaticSprite(R.drawable.cloud));
        setDistanceKoef(koef);

        float w2 = getScene().getWorldWidth() / 2 / koef;
        float x = (float) (-w2 + 2 * w2*Math.random());
        float y = (float) (Math.random()* (GameConfig.worldCeiling + 0.5)) / koef;
        setXY(x, y);
        vx = (float) (GameConfig.cloudSpeedMin + (GameConfig.cloudSpeedMax - GameConfig.cloudSpeedMin)*Math.random());
        vy = 0;
    }

    public void onPhysicsFrame(float fps) {
        setXY(getX() + vx / fps, getY() + vy / fps);
    }
}
