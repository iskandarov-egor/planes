package com.example.planes;

import android.app.Application;
import android.util.Log;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Sprite.Square;
import com.example.planes.Engine.Sprite.Triangle;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 10.07.15.
 */
public class MyApplication extends Application {
    Scene scene;
    ObjectGroup planesGroup = new ObjectGroup();
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO Put your application initialization code here.
        try {
            //1. create scene
            scene = new SceneImpl();
            scene.setGraphicsFPS(GameConfig.FPS);
            scene.setPhysicsFPS(GameConfig.PHYSICS_FPS);
            scene.setHorizontalPeriod(1f);
            scene.zoom(1);

            //2. create objects
            SceneObject plane = scene.createObject(0, 0, planesGroup);
            plane.setSprite(new Square());
            plane.setAngleSpeed(0*MathHelper.PI2);
            plane.setSpeed(0, 0);
            SceneObject plane2 = scene.createObject(0.7f, 0*0.3f, planesGroup);
            plane2.setSprite(new Triangle());
            plane2.setAngle(MathHelper.PI);
            plane2.setAngleSpeed(MathHelper.PI2); // 2*PI per second
            plane2.setSpeed(-0.1f, 0);
            plane.addBody(0.1f);
            plane2.addBody(0.1f);
            plane.setColor(1, 1, 1);

            //3. create collision listeners
            CollisionListener planesAndPlanes = new CollisionListener(planesGroup, planesGroup);
            planesAndPlanes.setOnCollisionStart(new CollisionProcessor() {
                @Override
                public void process(SceneObject object, SceneObject other) {
                    scene.setBackgroundColor(1, 0, 0);
                }
            });
            planesAndPlanes.setOnCollisionEnd(new CollisionProcessor() {
                @Override
                public void process(SceneObject object, SceneObject other) {
                    scene.setBackgroundColor(0, 0, 1);
                }
            });
            scene.addCollisionListener(planesAndPlanes);
        } catch (Exception e) {
            Log.e("error", Log.getStackTraceString(e));
        }
    }

    public Scene getScene() {
        return scene;
    }
}