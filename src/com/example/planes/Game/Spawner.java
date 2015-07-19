package com.example.planes.Game;

import android.graphics.Point;
import com.example.planes.Config.GameConfig;
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
    private Scene scene;

    public Spawner(Scene scene) {
        if(scene == null) throw new NullPointerException("scene");
        this.scene = scene;
    }

    public SceneObject createGround() {
        Point groundWH = Helper.getDrawableWH(R.drawable.ground);
        float groundH = Helper.getScreenRatio() * 2 * GameConfig.worldPeriod / groundWH.x * groundWH.y;
        SceneObject ground = scene.createObject(0, -1 + groundH/2);
        ground.setSprite(new StaticSprite(R.drawable.ground, groundH));
        return ground;
    }

    public Plane createPlane(float x, float y) {
        SceneObject so = scene.createObject(x, y);
        so.setSprite(new StaticSprite(R.drawable.plane_stub, 0.1f));
        so.setBody(0.1f);
        return new Plane(so, 0.16f, 0);
    }

    public Cloud createCloud(int i) {
            SceneObject so = scene.createObject(((i*171717 + 6487)%1111)/500f-1, ((i*191991 + 1358)%1111)/1000f);
            so.setSprite(new StaticSprite(R.drawable.cloud, 0.2f));
            return new Cloud(so, 0.1f, 0);
    }
}
