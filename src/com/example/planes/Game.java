package com.example.planes;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Sprite.Square;
import com.example.planes.Engine.Sprite.Triangle;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 12.07.15.
 */
public class Game {
    Scene scene;
    ObjectGroup planesGroup = new ObjectGroup();

    public Game(){
        Log.d("hey", "Game()");
        //1. create scene
        scene = Engine.getScene();
        scene.setGraphicsFPS(GameConfig.FPS);
        scene.setPhysicsFPS(GameConfig.PHYSICS_FPS);
        scene.setHorizontalPeriod(1f);
        scene.zoom(1);

        //2. create objects
        SceneObject plane = scene.createObject(0, 0, planesGroup);
        Square square = new Square();
        square.setColor(1, 1, 1);
        plane.setSprite(square);
        plane.setAngleSpeed(0* MathHelper.PI2);
        plane.setSpeed(0, 0);
        SceneObject plane2 = scene.createObject(0.7f, 0 * 0.3f, planesGroup);
        plane2.setSprite(new Triangle());
        plane2.setAngle(MathHelper.PI);
        plane2.setAngleSpeed(MathHelper.PI2); // 2*PI per second
        plane2.setSpeed(-0.1f, 0);
        plane.addBody(0.1f);
        plane2.addBody(0.1f);


        //3. create buttons
        Sticker button = scene.createSticker(-getScreenRatio() + 0.2f, 1 - 0.2f);

        //4. create collision listeners
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
    }

    public void start() {

    }

    private float getScreenRatio() {
        WindowManager wm = (WindowManager) MyApplication.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (float)size.x / size.y;
    }

    public Scene getScene() {
        return scene;
    }
}
