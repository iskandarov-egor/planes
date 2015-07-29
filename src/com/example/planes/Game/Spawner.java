package com.example.planes.Game;

import android.graphics.Point;
import com.example.planes.Config.Config;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneButton;
import com.example.planes.Engine.Scene.SceneObject;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.Game.Models.Plane;
import com.example.planes.Game.Models.Cloud;
import com.example.planes.R;
import com.example.planes.Utils.Helper;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 19.07.15.
 */
public class Spawner {
    private final Game game;
    private final Scene scene;

    public Spawner(Game game) {
        scene = game.getEngine().getScene();
        this.game = game;
        if(scene == null) throw new NullPointerException("scene");
    }

    public SceneObject createGround() {
        Point groundWH = Helper.getDrawableWH(R.drawable.ground);
        float groundH = Helper.getScreenRatio() * 2 * GameConfig.worldPeriod / groundWH.x * groundWH.y;
        SceneObject ground = scene.createObject(0, -1 + groundH/2);
        ground.setSprite(new StaticSprite(R.drawable.ground, groundH));
        return ground;
    }

    public Plane createPlane(float x, float y) {
        return new Plane(scene, x, y, 0.16f, 0);
    }

    public Cloud createCloud(int i) {
            SceneObject so = scene.createObject(((i*171717 + 6487)%1111)/500f-1, ((i*191991 + 1358)%1111)/1000f);
            so.setSprite(new StaticSprite(R.drawable.cloud, 0.2f));
            return new Cloud(so, 0.1f, 0);
    }

    public SceneButton createButtonDown() {
        SceneButton buttonDown = scene.createButton(-Helper.getScreenRatio() + Config.btnMargin, -Config.btnMargin);
        buttonDown.setSprite(new StaticSprite(R.drawable.btn_up, Config.btnRadius));
        buttonDown.setAngle(MathHelper.PI);
        buttonDown.setBody(Config.btnRadius);
        return buttonDown;
    }

    public SceneButton createButtonUp() {
        SceneButton buttonUp = scene.createButton(-Helper.getScreenRatio() + Config.btnMargin, Config.btnMargin);
        buttonUp.setSprite(new StaticSprite(R.drawable.btn_up, Config.btnRadius));
        buttonUp.setBody(Config.btnRadius);
        return buttonUp;
    }


    public SceneButton createButtonFire() {
        SceneButton btn = scene.createButton(Helper.getScreenRatio() - Config.btnMargin, Config.btnMargin);
        btn.setSprite(new StaticSprite(R.drawable.btn_fire, Config.btnRadius));
        btn.setBody(Config.btnRadius);
        return btn;
    }
}
