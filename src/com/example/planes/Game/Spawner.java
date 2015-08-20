package com.example.planes.Game;

import android.graphics.Point;
import com.example.planes.Config.BmpConfig;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.Game.Models.Plane;
import com.example.planes.R;
import com.example.planes.Utils.Helper;

/**
 * Created by egor on 19.07.15.
 */
public class Spawner {
    private final Round game;
    private final Scene scene;

    public Spawner(Round game) {
        scene = game.getScene();
        this.game = game;
        if(scene == null) throw new NullPointerException("scene");
    }

    public SceneObject createGround() {
        Point groundWH = Helper.getDrawableWH(R.drawable.ground);
        float w = scene.getWorldWidth();
        float groundH = w / groundWH.x * groundWH.y;
        SceneObject ground = scene.createObject(0, -1 + groundH / 2, groundH);
        ground.setSprite(new StaticSprite(R.drawable.ground));
        float hw = groundWH.x * 0.5f / groundWH.y;
        float[] x = {-hw, hw, hw, -hw};
        float[] y = {-0.5f, -0.5f, 0.5f - BmpConfig.groundLevel, 0.5f - BmpConfig.groundLevel};
        //ground.setBody(new ComplexPolygon(x, y));
        ground.setBody(0.1f);
        return ground;
    }

    public Plane createPlane(float x, float y) {
        return new Plane(scene, x, y, 0f, 0);
    }



}
