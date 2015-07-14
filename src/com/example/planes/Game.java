package com.example.planes;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Sprite.Square;
import com.example.planes.Engine.Sprite.Triangle;
import com.example.planes.Utils.MathHelper;
import com.example.planes.Utils.Vector;

/**
 * Created by egor on 12.07.15.
 */
public class Game {
    Scene scene;
    ObjectGroup planesGroup = new ObjectGroup();
    StupidTestCamera camera;
    Sticker button;

    public Game() {
        Log.d("hey", "Game()");
        //1. create scene
        scene = Engine.getScene();
        Engine.setGraphicsFPS(GameConfig.FPS);
        Engine.setPhysicsFPS(GameConfig.PHYSICS_FPS);
        scene.setHorizontalPeriod(1.5f);
        scene.setBackgroundColor(0, 0, 1);

        //2. create objects
        SceneObject squareObject = scene.createObject(0, 0, planesGroup);
        Square square = new Square();
        square.setColor(1, 1, 1);

        squareObject.setSprite(square);
        squareObject.setBody(0.1f);

        SceneObject triangleObject = scene.createObject(0.7f, 0 * 0.3f, planesGroup);
        triangleObject.setSprite(new Triangle());
        triangleObject.setAngle(MathHelper.PI);
        triangleObject.setAngleSpeed(MathHelper.PI2); // 2*PI per second
        triangleObject.setSpeed(-0.1f, 0);
        triangleObject.setBody(0.1f);

        //3. create buttons
        button = scene.createSticker(-getScreenRatio() + 0.2f, 1 - 0.2f);
        button.setSprite(new Square());
        button.setBody(0.1f);

        //4. create collision listeners
        CollisionListener listener = new CollisionListener(planesGroup, planesGroup);
        listener.setOnCollisionStart(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                scene.setBackgroundColor(1, 0, 0);
            }
        });
        listener.setOnCollisionEnd(new CollisionProcessor() {
            @Override
            public void process(SceneObject object, SceneObject other) {
                scene.setBackgroundColor(0, 0, 1);
            }
        });
        Engine.getCollisionManager().addCollisionListener(listener);

        //5. listen to onFrame
        camera = new StupidTestCamera(scene);
        Engine.setOnGraphicsFrameCallback(new Runnable() {
            @Override
            public void run() {
                camera.onFrame(); // update camera
            }
        });

        //6. listen to touch events
        Engine.setTouchEventListener(new TouchEventListener() {
            @Override
            public boolean onTouchEvent(MotionEvent e) {
                return onTouch(e);
            }
        });
    }


    private boolean onTouch(MotionEvent e) {
        Vector pos = screenToEngine(e.getRawX(), e.getRawY());
        float x = pos.x;
        float y = pos.y;
        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(button.isPointInside(x, y)) {
                    scene.setBackgroundColor(0, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(button.isPointInside(x, y)) {
                    scene.setBackgroundColor(0, 1, 1);
                    return true;
                }
                break;
        }
        return false;
    }

    private float getScreenRatio() {
        WindowManager wm = (WindowManager) MyApplication.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x < size.y) throw new RuntimeException("что то не так"); //debug
        return (float)size.x / size.y;
    }

    private Vector screenToEngine(float x, float y) {
        return new Vector(2*(x / getScreenHeight() - 0.5f*getScreenRatio()),
                            2*(0.5f - y / getScreenHeight()));
    }

    private int getScreenHeight(){
        WindowManager wm = (WindowManager) MyApplication.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x < size.y) throw new RuntimeException("что то не так"); //debug
        return size.y;
    }

    public Scene getScene() {
        return scene;
    }
}
