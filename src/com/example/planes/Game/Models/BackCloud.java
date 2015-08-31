package com.example.planes.Game.Models;

import com.example.planes.Config.BmpConfig;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.R;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 31.08.15.
 */
public class BackCloud extends SceneObject {
    private static final float MIN_H = 0.2f;
    private static final float MAX_H = 0.5f;
    private static final float DIST_KOEF = 0.2f;

    protected float vx = 0;
    protected float vy = 0;

    public BackCloud(Scene scene) {
        super(0, 0, scene, MathHelper.rand(MIN_H, MAX_H));

        setSprite(new StaticSprite(R.drawable.cloud));
        setDistanceKoef(DIST_KOEF);

        float w2 = getScene().getWorldWidth() / 2 / DIST_KOEF;
        float x = (float) (-w2 + 2 * w2*Math.random());
        float y = (float) (Math.random()* (GameConfig.worldCeiling + 0.5)) * DIST_KOEF;
        setXY(x, y);
        vx = (float) (GameConfig.cloudSpeedMin + (GameConfig.cloudSpeedMax - GameConfig.cloudSpeedMin)*Math.random());
        vy = 0;
    }

    public void onPhysicsFrame(float fps) {
        setXY(getX() + vx / fps, getY() + vy / fps);
    }
}
