package com.example.planes.Game;

import android.graphics.Point;
import com.example.planes.Config.Config;
import com.example.planes.Engine.Body.ComplexPolygon;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.Game.Models.Plane;
import com.example.planes.Game.Models.Cloud;
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
        float[] y = {-0.5f, -0.5f, 0.5f - Config.groundLevel, 0.5f - Config.groundLevel};
        //ground.setBody(new ComplexPolygon(x, y));
        ground.setBody(0.1f);
        return ground;
    }

    public Plane createPlane(float x, float y) {
        return new Plane(scene, x, y, 0f, 0);
    }

    public Cloud createCloud(int i) {
        float x = ((i*171717 + 6487)%1111)/500f-1;
        float y = ((i * 191991 + 1358) % 1111) / 1000f;
        Cloud cloud = new Cloud(scene, x, y, 0.1f, 0);
        scene.addObject(cloud);
        return cloud;
    }

}
