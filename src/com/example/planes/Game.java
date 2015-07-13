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
    Camera camera = new Camera();

    public Game() {
        Log.d("hey", "Game()");
        //1. create scene
        scene = Engine.getScene();
        Engine.setGraphicsFPS(GameConfig.FPS);
        Engine.setPhysicsFPS(GameConfig.PHYSICS_FPS);
        scene.setHorizontalPeriod(1f);

        //2. create objects
        SceneObject squareObject = scene.createObject(0, 0, planesGroup);
        Square square = new Square();
        square.setColor(1, 1, 1);
        squareObject.setSprite(square);
        squareObject.setAngleSpeed(0 * MathHelper.PI2);
        squareObject.setSpeed(0, 0);
        squareObject.addBody(0.1f);

        SceneObject triangleObject = scene.createObject(0.7f, 0 * 0.3f, planesGroup);
        triangleObject.setSprite(new Triangle());
        triangleObject.setAngle(MathHelper.PI);
        triangleObject.setAngleSpeed(MathHelper.PI2); // 2*PI per second
        triangleObject.setSpeed(-0.1f, 0);
        triangleObject.addBody(0.1f);
        Engine.getViewport().setZoom(1f);

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
        Engine.getCollisionManager().addCollisionListener(planesAndPlanes);

        //5. listen to onFrame
        Engine.setOnGraphicsFrameCallback(new Runnable() {
            @Override
            public void run() {
                camera.onFrame(); // update camera
            }
        });
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
